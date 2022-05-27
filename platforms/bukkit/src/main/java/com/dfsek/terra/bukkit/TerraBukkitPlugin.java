/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.bukkit;

import cloud.commandframework.brigadier.CloudBrigadierManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.listeners.CommonListener;
import com.dfsek.terra.bukkit.listeners.PaperListener;
import com.dfsek.terra.bukkit.listeners.SpigotListener;
import com.dfsek.terra.bukkit.util.PaperUtil;
import com.dfsek.terra.bukkit.util.VersionUtil;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class TerraBukkitPlugin extends JavaPlugin {
    private static final Logger logger = LoggerFactory.getLogger(TerraBukkitPlugin.class);
    
    private final PlatformImpl platform = new PlatformImpl(this);
    private final Map<String, com.dfsek.terra.api.world.chunk.generation.ChunkGenerator> generatorMap = new HashMap<>();
    
    @Override
    public void onEnable() {
        if(!doVersionCheck()) {
            return;
        }
        
        platform.getEventManager().callEvent(new PlatformInitializationEvent());
        
        
        try {
            PaperCommandManager<CommandSender> commandManager = new PaperCommandManager<>(this,
                                                                                          CommandExecutionCoordinator.simpleCoordinator(),
                                                                                          BukkitAdapter::adapt,
                                                                                          BukkitAdapter::adapt);
            if(commandManager.queryCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
                commandManager.registerBrigadier();
                final CloudBrigadierManager<?, ?> brigManager = commandManager.brigadierManager();
                if(brigManager != null) {
                    brigManager.setNativeNumberSuggestions(false);
                }
            }
            
            if(commandManager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                commandManager.registerAsynchronousCompletions();
            }
            
            platform.getEventManager().callEvent(new CommandRegistrationEvent(commandManager));
            
        } catch(Exception e) { // This should never happen.
            logger.error("""
                         TERRA HAS BEEN DISABLED
                                                  
                         Errors occurred while registering commands.
                         Please report this to Terra.
                         """.strip(), e);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        
        Bukkit.getPluginManager().registerEvents(new CommonListener(), this); // Register master event listener
        PaperUtil.checkPaper(this);
        
        try {
            Class.forName("io.papermc.paper.event.world.StructureLocateEvent"); // Check if user is on Paper version with event.
            Bukkit.getPluginManager().registerEvents(new PaperListener(platform), this); // Register Paper events.
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
                
                Bukkit.getPluginManager().registerEvents(new SpigotListener(platform), this); // Register Spigot event listener
            }
        }
    }
    
    @SuppressWarnings({ "deprecation", "AccessOfSystemProperties" })
    private boolean doVersionCheck() {
        logger.info("Running on Minecraft version {} with server implementation {}.", VersionUtil.getMinecraftVersionInfo(),
                    Bukkit.getServer().getName());
        
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
                Bukkit.getPluginManager().disablePlugin(this);
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
    ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, String id) {
        return new BukkitChunkGeneratorWrapper(generatorMap.computeIfAbsent(worldName, name -> {
            ConfigPack pack = platform.getConfigRegistry().getByID(id).orElseThrow(
                    () -> new IllegalArgumentException("No such config pack \"" + id + "\""));
            return pack.getGeneratorProvider().newInstance(pack);
        }), platform.getRawConfigRegistry().getByID(id).orElseThrow(), platform.getWorldHandle().air());
    }
}
