package com.dfsek.terra.bukkit.nms;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;

import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NMSInjectListener implements Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMSInjectListener.class);
    
    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        if (event.getWorld().getGenerator() instanceof BukkitChunkGeneratorWrapper bukkitChunkGeneratorWrapper) {
            LOGGER.info("A great evil has fallen upon this land: {}", event.getWorld().getName());
            CraftWorld craftWorld = (CraftWorld) event.getWorld();
            WorldServer serverWorld = craftWorld.getHandle();

            ConfigPack pack = bukkitChunkGeneratorWrapper.getPack();
    
            ChunkGenerator vanilla = serverWorld.k().g();
            NMSBiomeProvider provider = new NMSBiomeProvider(pack.getBiomeProvider(), vanilla.e(), craftWorld.getSeed());
            NMSChunkGeneratorDelegate custom = new NMSChunkGeneratorDelegate(vanilla, pack, provider,
                                                                                                craftWorld.getSeed(), craftWorld);
            
            custom.conf = vanilla.conf; // world config from Spigot
            
            serverWorld.k().a.u = custom;

            LOGGER.info("Successfully injected into world.");
        }
    }
}
