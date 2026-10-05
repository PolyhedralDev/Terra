package com.dfsek.terra.bukkit.nms;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;


public class NMSInjectListener implements Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMSInjectListener.class);
    private static final Set<World> INJECTED = new HashSet<>();
    private static final ReentrantLock INJECT_LOCK = new ReentrantLock();
    private final NMSVersionBindings bindings;

    public NMSInjectListener(NMSVersionBindings bindings) {
        this.bindings = bindings;
    }

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        if(!INJECTED.contains(event.getWorld()) &&
           event.getWorld().getGenerator() instanceof BukkitChunkGeneratorWrapper bukkitChunkGeneratorWrapper) {
            INJECT_LOCK.lock();
            try {
                LOGGER.info("Preparing to take over the world: {}", event.getWorld().getName());
                CraftWorld craftWorld = (CraftWorld) event.getWorld();
                ServerLevel serverWorld = craftWorld.getHandle();

                ConfigPack pack = bukkitChunkGeneratorWrapper.getPack();

                ChunkGenerator vanilla = serverWorld.getChunkSource().getGenerator();
                NMSBiomeProvider provider = bindings.createBiomeProvider(pack.getBiomeProvider(), craftWorld.getSeed());
                ChunkMap chunkMap = serverWorld.getChunkSource().chunkMap;
                WorldGenContext worldGenContext = Reflection.CHUNKMAP.getWorldGenContext(chunkMap);
                Reflection.CHUNKMAP.setWorldGenContext(chunkMap, new WorldGenContext(
                    worldGenContext.level(),
                    bindings.createChunkGenerator(vanilla, pack, provider, craftWorld.getSeed()),
                    worldGenContext.structureManager(),
                    worldGenContext.lightEngine(),
                    worldGenContext.mainThreadExecutor(),
                    worldGenContext.unsavedListener()
                ));

                INJECTED.add(event.getWorld());
                LOGGER.info("Successfully injected into world.");

            } finally {
                INJECT_LOCK.unlock();
            }
        }
    }
}
