package com.dfsek.terra.bukkit;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.bukkit.command.BukkitCommandAdapter;
import com.dfsek.terra.bukkit.command.FixChunkCommand;
import com.dfsek.terra.bukkit.command.SaveDataCommand;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.listeners.CommonListener;
import com.dfsek.terra.bukkit.listeners.PaperListener;
import com.dfsek.terra.bukkit.listeners.SpigotListener;
import com.dfsek.terra.bukkit.util.PaperUtil;
import com.dfsek.terra.bukkit.util.VersionUtil;
import com.dfsek.terra.commands.CommandUtil;
import com.dfsek.terra.commands.TerraCommandManager;


public class TerraBukkitPlugin extends JavaPlugin {
    private static final Logger logger = LoggerFactory.getLogger(TerraBukkitPlugin.class);
    
    private final TerraPluginImpl terraPlugin = new TerraPluginImpl(this);
    private final Map<String, com.dfsek.terra.api.world.generator.ChunkGenerator> generatorMap = new HashMap<>();
    private final Map<String, ConfigPack> worlds = new HashMap<>();
    
    @Override
    public void onDisable() {
        BukkitChunkGeneratorWrapper.saveAll();
    }
    
    @Override
    public void onEnable() {
        if(!doVersionCheck()) {
            return;
        }
        
        terraPlugin.getEventManager().callEvent(new PlatformInitializationEvent());
        
        new Metrics(this, 9017); // Set up bStats.
        
        PluginCommand cmd = Objects.requireNonNull(getCommand("terra"));
        
        CommandManager manager = new TerraCommandManager(terraPlugin);
        
        
        try {
            CommandUtil.registerAll(manager);
            manager.register("save-data", SaveDataCommand.class);
            manager.register("fix-chunk", FixChunkCommand.class);
        } catch(MalformedCommandException e) { // This should never happen.
            logger.error("""
                         TERRA HAS BEEN DISABLED
                                                  
                         Errors occurred while registering commands.
                         Please report this to Terra.
                         """.strip(), e);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        
        BukkitCommandAdapter command = new BukkitCommandAdapter(manager);
        
        cmd.setExecutor(command);
        cmd.setTabCompleter(command);
        
        
        long save = terraPlugin.getTerraConfig().getDataSaveInterval();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, BukkitChunkGeneratorWrapper::saveAll, save,
                                                         save); // Schedule population data saving
        Bukkit.getPluginManager().registerEvents(new CommonListener(terraPlugin), this); // Register master event listener
        PaperUtil.checkPaper(this);
        
        try {
            Class.forName("io.papermc.paper.event.world.StructureLocateEvent"); // Check if user is on Paper version with event.
            Bukkit.getPluginManager().registerEvents(new PaperListener(terraPlugin), this); // Register Paper events.
        } catch(ClassNotFoundException e) {
            /*
              The command
              
                  fmt -w 72 -g 72 -u text | \
                  boxes -a cmd -p a1h3 -t 4e -d jstone -s82 | \
                  sed -Ee 's/\+-+\*\//|------------------------------------------------------------------------------|/g' \
                  -e 's/^\s*(.*)$/"\1\\n"/g' -e 's/\///g' -e 's/\*|\+/./g' -e 's/$/ +/g' -e '/^"\| {3}-{72} {3}\|\\n" \+$/d'
              
              was used to create these boxes. Leaving this here for if we want to create more/modify them.
             */
            if(VersionUtil.getSpigotVersionInfo().isPaper()) { // logging with stack trace to be annoying.
                logger.warn("""
                            .------------------------------------------------------------------------------.
                            |                                                                              |
                            |      You are using an outdated version of Paper. This version does not       |
                            |       contain StructureLocateEvent. Terra will now fall back to Spigot       |
                            |       events. This will prevent cartographer villagers from spawning,        |
                            |       and cause structure location to not function. If you want these        |
                            |      functionalities, update to the latest build of Paper. If you use a      |
                            |      fork, update to the latest version, then if you still receive this      |
                            |             message, ask the fork developer to update upstream.              |
                            |                                                                              |
                            |------------------------------------------------------------------------------|
                            """.strip(), e);
            } else {
                logger.warn("""
                            .------------------------------------------------------------------------------.
                            |                                                                              |
                            |    Paper is not in use. Falling back to Spigot events. This will prevent     |
                            |    cartographer villagers from spawning, and cause structure location to     |
                            |      not function. If you want these functionalities (and all the other      |
                            |     benefits that Paper offers), upgrade your server to Paper. Find out      |
                            |                         more at https://papermc.io/                          |
                            |                                                                              |
                            |------------------------------------------------------------------------------|
                            """.strip(), e);
                
                Bukkit.getPluginManager().registerEvents(new SpigotListener(terraPlugin), this); // Register Spigot event listener
            }
        }
    }
    
    @SuppressWarnings({ "deprecation", "AccessOfSystemProperties" })
    private boolean doVersionCheck() {
        logger.info("Running on Minecraft version {} with server implementation {}.", VersionUtil.getMinecraftVersionInfo(), Bukkit.getServer().getName());
        
        if(!VersionUtil.getSpigotVersionInfo().isSpigot())
            logger.error("YOU ARE RUNNING A CRAFTBUKKIT OR BUKKIT SERVER. PLEASE UPGRADE TO PAPER.");
        
        if(VersionUtil.getSpigotVersionInfo().isMohist()) {
            if(System.getProperty("IKnowMohistCausesLotsOfIssuesButIWillUseItAnyways") == null) {
                Runnable runnable = () -> { // scary big block of text
                    logger.error("""
                                 .----------------------------------------------------------------------------------.
                                 |                                                                                  |
                                 |                                ⚠ !! Warning !! ⚠                                 |
                                 |                                                                                  |
                                 |                         You are currently using Mohist.                          |
                                 |                                                                                  |
                                 |                                Do not use Mohist.                                |
                                 |                                                                                  |
                                 |   The concept of combining the rigid Bukkit platform, which assumes a 100%       |
                                 |   Vanilla server, with the flexible Forge platform, which allows changing        |
                                 |   core components of the game, simply does not work. These platforms are         |
                                 |   incompatible at a conceptual level, the only way to combine them would         |
                                 |   be to make incompatible changes to both. As a result, Mohist's Bukkit          |
                                 |   API implementation is not compliant. This will cause many plugins to           |
                                 |   break. Rather than fix their platform, Mohist has chosen to distribute         |
                                 |   unofficial builds of plugins they deem to be "fixed". These builds are not     |
                                 |   "fixed", they are simply hacked together to work with Mohist's half-baked      |
                                 |   Bukkit implementation. To distribute these as "fixed" versions implies that:   |
                                 |       - These builds are endorsed by the original developers. (They are not)     |
                                 |       - The issue is on the plugin's end, not Mohist's. (It is not. The issue    |
                                 |       is that Mohist chooses to not create a compliant Bukkit implementation)    |
                                 |   Please, do not use Mohist. It causes issues with most plugins, and rather      |
                                 |   than fixing their platform, Mohist has chosen to distribute unofficial         |
                                 |   hacked-together builds of plugins, calling them "fixed". If you want           |
                                 |   to use a server API with Forge mods, look at the Sponge project, an            |
                                 |   API that is designed to be implementation-agnostic, with first-party           |
                                 |   support for the Forge mod loader. You are bound to encounter issues if         |
                                 |   you use Terra with Mohist. We will provide NO SUPPORT for servers running      |
                                 |   Mohist. If you wish to proceed anyways, you can add the JVM System Property    |
                                 |   "IKnowMohistCausesLotsOfIssuesButIWillUseItAnyways" to enable the plugin. No   |
                                 |   support will be provided for servers running Mohist.                           |
                                 |                                                                                  |
                                 |                   Because of this **TERRA HAS BEEN DISABLED**.                   |
                                 |                    Do not come ask us why it is not working.                     |
                                 |                                                                                  |
                                 |----------------------------------------------------------------------------------|
                                 """.strip());
                };
                runnable.run();
                Bukkit.getScheduler().scheduleAsyncDelayedTask(this, runnable, 200L);
                // Bukkit.shutdown(); // we're not *that* evil
                setEnabled(false);
                return false;
            } else {
                logger.warn("""
                            You are using Mohist, so we will not give you any support for issues that may arise.
                            Since you enabled the "IKnowMohistCausesLotsOfIssuesButIWillUseItAnyways" flag, we won't disable Terra. But be warned.
                            
                            > I felt a great disturbance in the JVM, as if millions of plugins suddenly cried out in stack traces and were suddenly silenced.
                            > I fear something terrible has happened.
                            > - Astrash
                            """.strip());
            }
        }
        return true;
    }
    
    @Override
    public @Nullable
    ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new BukkitChunkGeneratorWrapper(generatorMap.computeIfAbsent(worldName, name -> {
            if(!terraPlugin.getConfigRegistry().contains(id)) throw new IllegalArgumentException("No such config pack \"" + id + "\"");
            ConfigPack pack = terraPlugin.getConfigRegistry().get(id);
            worlds.put(worldName, pack);
            return pack.getGeneratorProvider().newInstance(pack);
        }));
    }
}
