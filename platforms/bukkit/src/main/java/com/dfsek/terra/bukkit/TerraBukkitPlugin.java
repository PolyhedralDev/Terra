package com.dfsek.terra.bukkit;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.language.Language;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.bukkit.command.command.TerraCommand;
import com.dfsek.terra.bukkit.command.command.structure.LocateCommand;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.handles.BukkitItemHandle;
import com.dfsek.terra.bukkit.handles.BukkitWorldHandle;
import com.dfsek.terra.bukkit.listeners.CommonListener;
import com.dfsek.terra.bukkit.listeners.PaperListener;
import com.dfsek.terra.bukkit.listeners.SpigotListener;
import com.dfsek.terra.bukkit.util.PaperUtil;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.BukkitBiome;
import com.dfsek.terra.bukkit.world.BukkitTree;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.debug.DebugLogger;
import com.dfsek.terra.registry.ConfigRegistry;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.generation.MasterChunkGenerator;
import io.papermc.lib.PaperLib;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.TreeType;
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
    private final Map<String, MasterChunkGenerator> generatorMap = new HashMap<>();
    private final Map<World, TerraWorld> worldMap = new HashMap<>();
    private final Map<String, ConfigPack> worlds = new HashMap<>();
    private final ConfigRegistry registry = new ConfigRegistry();
    private final PluginConfig config = new PluginConfig();
    private final ItemHandle itemHandle = new BukkitItemHandle();
    private WorldHandle handle = new BukkitWorldHandle();
    private final GenericLoaders genericLoaders = new GenericLoaders(this);
    private DebugLogger debugLogger;

    public static final Version BUKKIT_VERSION;

    static {
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        if(ver.contains("1_16")) BUKKIT_VERSION = Version.V1_16;
        else if(ver.contains("1_15")) BUKKIT_VERSION = Version.V1_15;
        else if(ver.contains("1_14")) BUKKIT_VERSION = Version.V1_14;
        else if(ver.contains("1_13")) BUKKIT_VERSION = Version.V1_13;
        else BUKKIT_VERSION = Version.UNKNOWN;
    }


    public void reload() {
        Map<World, TerraWorld> newMap = new HashMap<>();
        worldMap.forEach((world, tw) -> {
            ((MasterChunkGenerator) ((BukkitChunkGeneratorWrapper) world.getGenerator().getHandle()).getHandle()).getCache().clear();
            String packID = tw.getConfig().getTemplate().getID();
            newMap.put(world, new TerraWorld(world, registry.get(packID), this));
        });
        worldMap.clear();
        worldMap.putAll(newMap);
    }

    @Override
    public ItemHandle getItemHandle() {
        return itemHandle;
    }

    @Override
    public String platformName() {
        return "Bukkit";
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
    public void packPreLoadCallback(ConfigPack pack) {
        for(TreeType value : TreeType.values()) {
            pack.getTreeRegistry().add(BukkitAdapter.TREE_TRANSFORMER.translate(value), new BukkitTree(value, this));
        }
    }

    @Override
    public DebugLogger getDebugLogger() {
        return debugLogger;
    }

    @Override
    public void onDisable() {
        BukkitChunkGeneratorWrapper.saveAll();
    }

    @Override
    public void onEnable() {
        debugLogger = new DebugLogger(getLogger());

        getLogger().info("Running on version " + BUKKIT_VERSION);
        if(BUKKIT_VERSION.equals(Version.UNKNOWN)) {
            getLogger().warning("Terra is running on an unknown Bukkit version. Proceed with caution.");
        }

        saveDefaultConfig();

        Metrics metrics = new Metrics(this, 9017); // Set up bStats.
        metrics.addCustomChart(new Metrics.SingleLineChart("worlds", worldMap::size)); // World number chart.

        config.load(this); // Load master config.yml
        LangUtil.load(config.getLanguage(), this); // Load language.
        debugLogger.setDebug(isDebug());

        registry.loadAll(this); // Load all config packs.

        PluginCommand c = Objects.requireNonNull(getCommand("terra"));
        TerraCommand command = new TerraCommand(this); // Set up main Terra command.
        c.setExecutor(command);
        c.setTabCompleter(command);

        LocateCommand locate = new LocateCommand(command);
        PluginCommand locatePl = Objects.requireNonNull(getCommand("locate"));
        locatePl.setExecutor(locate); // Override locate command. Once Paper accepts StructureLocateEvent this will be unneeded on Paper implementations.
        locatePl.setTabCompleter(locate);


        long save = config.getDataSaveInterval();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, BukkitChunkGeneratorWrapper::saveAll, save, save); // Schedule population data saving
        Bukkit.getPluginManager().registerEvents(new CommonListener(this), this); // Register master event listener
        PaperUtil.checkPaper(this);

        if(PaperLib.isPaper()) {
            try {
                Class.forName("io.papermc.paper.event.world.StructureLocateEvent"); // Check if user is on Paper version with event.
                Bukkit.getPluginManager().registerEvents(new PaperListener(this), this); // Register Paper events.
            } catch(ClassNotFoundException e) {
                registerSpigotEvents(true); // Outdated Paper version.
            }
        } else {
            registerSpigotEvents(false);
        }
    }

    private void registerSpigotEvents(boolean outdated) {
        if(outdated) {
            getLogger().severe("You are using an outdated version of Paper.");
            getLogger().severe("This version does not contain StructureLocateEvent.");
            getLogger().severe("Terra will now fall back to Spigot events.");
            getLogger().severe("This will prevent cartographer villagers from spawning,");
            getLogger().severe("and cause structure location to not function.");
            getLogger().severe("If you want these functionalities, update to the latest build of Paper.");
            getLogger().severe("If you use a fork, update to the latest version, then if you still");
            getLogger().severe("receive this message, ask the fork developer to update upstream.");
        } else {
            getLogger().severe("Paper is not in use. Falling back to Spigot events.");
            getLogger().severe("This will prevent cartographer villagers from spawning,");
            getLogger().severe("and cause structure location to not function.");
            getLogger().severe("If you want these functionalities (and all the other");
            getLogger().severe("benefits that Paper offers), upgrade your server to Paper.");
            getLogger().severe("Find out more at https://papermc.io/");
        }

        Bukkit.getPluginManager().registerEvents(new SpigotListener(this), this); // Register Spigot event listener
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new BukkitChunkGeneratorWrapper(generatorMap.computeIfAbsent(worldName, name -> {
            if(!registry.contains(id)) throw new IllegalArgumentException("No such config pack \"" + id + "\"");
            ConfigPack pack = registry.get(id);
            worlds.put(worldName, pack);
            return new MasterChunkGenerator(registry.get(id), this, pack.getSamplerCache());
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
        if(!TerraWorld.isTerraWorld(w))
            throw new IllegalArgumentException("Not a Terra world! " + w.getGenerator());
        if(!worlds.containsKey(w.getName())) {
            getLogger().warning("Unexpected world load detected: \"" + w.getName() + "\"");
            return new TerraWorld(w, ((MasterChunkGenerator) w.getGenerator().getHandle()).getConfigPack(), this);
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

    public enum Version {
        V1_13(13),

        V1_14(14),

        V1_15(15),

        V1_16(16),

        UNKNOWN(Integer.MAX_VALUE); // Assume unknown version is latest.

        private final int index;

        Version(int index) {
            this.index = index;
        }

        /**
         * Gets if this version is above or equal to another.
         *
         * @param other Other version
         * @return Whether this version is equal to or later than other.
         */
        public boolean above(Version other) {
            return this.index >= other.index;
        }
    }
}
