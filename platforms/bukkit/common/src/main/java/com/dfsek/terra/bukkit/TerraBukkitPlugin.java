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

import io.papermc.paper.ServerBuildInfo;
import io.papermc.paper.ServerBuildInfo.StringRepresentation;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.MetaPack;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.bukkit.attribute.PaperVanilla;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.listeners.CommonListener;
import com.dfsek.terra.bukkit.listeners.DimensionListener;
import com.dfsek.terra.bukkit.world.BiomeHandler;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class TerraBukkitPlugin extends JavaPlugin {
    private static final Logger logger = LoggerFactory.getLogger(TerraBukkitPlugin.class);
    private final Map<String, com.dfsek.terra.api.world.chunk.generation.ChunkGenerator> generatorMap = new HashMap<>();
    private BukkitPlatform platform;

    @Override
    public void onEnable() {
        logServerVersion();

        platform = new BukkitPlatform(this);
        platform.getEventManager().callEvent(new PlatformInitializationEvent()); // Loads config packs
        platform.attributeParents().beginLoad(PaperVanilla.context(PaperVanilla.server().registryAccess()));
        BiomeHandler.registerBiomes(platform);
        platform.dimensions().registerPacks(platform.getRawConfigRegistry().entries());
        BiomeHandler.addToVanillaTags(platform);

        try {
            PaperCommandManager<CommandSender> commandManager = getCommandSenderPaperCommandManager();

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

        Bukkit.getPluginManager().registerEvents(new CommonListener(platform), this); // Register master event listener
        Bukkit.getPluginManager().registerEvents(new DimensionListener(platform.dimensions()), this);
    }

    @NotNull
    private PaperCommandManager<CommandSender> getCommandSenderPaperCommandManager() throws Exception {
        PaperCommandManager<CommandSender> commandManager = PaperCommandManager.builder(SenderMapper.create(
                BukkitAdapter::adapt,
                BukkitAdapter::adapt
            ))
            .executionCoordinator(ExecutionCoordinator.asyncCoordinator())
            .buildOnEnable(this);

        commandManager.brigadierManager().setNativeNumberSuggestions(false);

        return commandManager;
    }

    public BukkitPlatform getPlatform() {
        return platform;
    }

    private void logServerVersion() {
        ServerBuildInfo buildInfo = ServerBuildInfo.buildInfo();
        logger.info("Running on Minecraft version {} with server implementation {} {}.", buildInfo.minecraftVersionName(),
            buildInfo.brandName(), buildInfo.asString(StringRepresentation.VERSION_SIMPLE));
    }

    @Override
    public @Nullable
    ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, String id) {
        if(id == null || id.trim().isEmpty()) { return null; }
        ConfigPack pack = platform.getConfigRegistry().getByID(id).orElse(null);
        if(pack == null) {
            MetaPack metaPack = platform.getMetaConfigRegistry().getByID(id).orElseThrow(
                () -> new IllegalArgumentException("No such config pack or metapack \"" + id + "\""));
            return new BukkitChunkGeneratorWrapper(metaPack, platform.getWorldHandle().air());
        }
        var delegate = generatorMap.computeIfAbsent(worldName, name -> pack.getGeneratorProvider().newInstance(pack));
        return new BukkitChunkGeneratorWrapper(delegate, pack, platform.getWorldHandle().air());
    }

    public GlobalRegionScheduler getGlobalRegionScheduler() {
        return getServer().getGlobalRegionScheduler();
    }
}
