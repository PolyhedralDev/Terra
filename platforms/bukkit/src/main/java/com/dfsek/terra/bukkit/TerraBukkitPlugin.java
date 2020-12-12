package com.dfsek.terra.bukkit;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.GenericLoaders;
import com.dfsek.terra.api.gaea.lang.Language;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.inventory.ItemHandle;
import com.dfsek.terra.api.generic.world.Biome;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.bukkit.command.command.TerraCommand;
import com.dfsek.terra.bukkit.command.command.structure.LocateCommand;
import com.dfsek.terra.bukkit.generator.BukkitChunkGenerator;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.listeners.EventListener;
import com.dfsek.terra.bukkit.listeners.SpigotListener;
import com.dfsek.terra.bukkit.util.PaperUtil;
import com.dfsek.terra.bukkit.world.BukkitBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.PluginConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.generation.TerraChunkGenerator;
import com.dfsek.terra.registry.ConfigRegistry;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class TerraBukkitPlugin extends JavaPlugin implements TerraPlugin {
    private final Map<String, TerraChunkGenerator> generatorMap = new HashMap<>();
    private final Map<World, TerraWorld> worldMap = new HashMap<>();
    private final Map<String, ConfigPack> worlds = new HashMap<>();
    private final ConfigRegistry registry = new ConfigRegistry();
    private final PluginConfig config = new PluginConfig();
    private WorldHandle handle = new BukkitWorldHandle();
    private final GenericLoaders genericLoaders = new GenericLoaders(this);


    public void reload() {
        Map<World, TerraWorld> newMap = new HashMap<>();
        worldMap.forEach((world, tw) -> {
            String packID = tw.getConfig().getTemplate().getID();
            newMap.put(world, new TerraWorld(world, registry.get(packID), this));
        });
        worldMap.clear();
        worldMap.putAll(newMap);
    }

    @Override
    public ItemHandle getItemHandle() {
        return null;
    }

    public void setHandle(WorldHandle handle) {
        getLogger().warning("|-------------------------------------------------------|");
        getLogger().warning("A third-party addon has injected a custom WorldHandle!");
        getLogger().warning("If you encounter issues, try *without* the addon before");
        getLogger().warning("reporting to Terra. Report issues with the addon to the");
        getLogger().warning("addon's maintainers!");
        getLogger().warning("|-------------------------------------------------------|");
        this.handle = handle;
    }

    @Override
    public void onDisable() {
        TerraChunkGenerator.saveAll();
    }

    @Override
    public void onEnable() {
        Debug.setLogger(getLogger()); // Set debug logger.

        saveDefaultConfig();

        Metrics metrics = new Metrics(this, 9017); // Set up bStats.
        metrics.addCustomChart(new Metrics.SingleLineChart("worlds", worldMap::size)); // World number chart.

        config.load(this); // Load master config.yml
        LangUtil.load(config.getLanguage(), this); // Load language.
        Debug.setDebug(isDebug());

        registry.loadAll(this); // Load all config packs.

        PluginCommand c = Objects.requireNonNull(getCommand("terra"));
        TerraCommand command = new TerraCommand(this); // Set up main Terra command.
        c.setExecutor(command);
        c.setTabCompleter(command);

        LocateCommand locate = new LocateCommand(command, false);
        PluginCommand locatePl = Objects.requireNonNull(getCommand("locate"));
        locatePl.setExecutor(locate); // Override locate command. Once Paper accepts StructureLocateEvent this will be unneeded on Paper implementations.
        locatePl.setTabCompleter(locate);


        long save = config.getDataSaveInterval();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, TerraChunkGenerator::saveAll, save, save); // Schedule population data saving

        Bukkit.getPluginManager().registerEvents(new EventListener(this), this); // Register master event listener
        Bukkit.getPluginManager().registerEvents(new SpigotListener(this), this); // Register Spigot event listener, once Paper accepts StructureLocateEvent PR Spigot and Paper events will be separate.

        PaperUtil.checkPaper(this);
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new BukkitChunkGeneratorWrapper(generatorMap.computeIfAbsent(worldName, name -> {
            if(!registry.contains(id)) throw new IllegalArgumentException("No such config pack \"" + id + "\"");
            worlds.put(worldName, registry.get(id));
            return new TerraChunkGenerator(registry.get(id), this);
        }));
    }

    @Override
    public boolean isDebug() {
        return config.isDebug();
    }


    @Override
    public Language getLanguage() {
        return LangUtil.getLanguage();
    }

    public ConfigRegistry getRegistry() {
        return registry;
    }

    public TerraWorld getWorld(World w) {
        if(!(w.getGenerator() instanceof BukkitChunkGenerator))
            throw new IllegalArgumentException("Not a Terra world! " + w.getGenerator());
        if(!worlds.containsKey(w.getName())) {
            getLogger().warning("Unexpected world load detected: \"" + w.getName() + "\"");
            return new TerraWorld(w, ((TerraChunkGenerator) w.getGenerator()).getConfigPack(), this);
        }
        return worldMap.computeIfAbsent(w, world -> new TerraWorld(w, worlds.get(w.getName()), this));
    }

    @NotNull
    @Override
    public PluginConfig getTerraConfig() {
        return config;
    }

    @Override
    public WorldHandle getWorldHandle() {
        return handle;
    }


    @Override
    public void register(TypeRegistry registry) {
        registry
                .registerLoader(BlockData.class, (t, o, l) -> handle.createBlockData((String) o))
                .registerLoader(MaterialData.class, (t, o, l) -> handle.createMaterialData((String) o))
                .registerLoader(Biome.class, (t, o, l) -> new BukkitBiome(org.bukkit.block.Biome.valueOf((String) o)))
                .registerLoader(EntityType.class, (t, o, l) -> EntityType.valueOf((String) o));
        genericLoaders.register(registry);
    }
}
