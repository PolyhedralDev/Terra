package com.dfsek.terra.bukkit.nms.v1_19_R1;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.nms.v1_19_R1.util.FertilizableUtil;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class NMSInjectListener implements Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMSInjectListener.class);
    private static final Set<World> INJECTED = new HashSet<>();
    private static final ReentrantLock INJECT_LOCK = new ReentrantLock();
    
    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        if(!INJECTED.contains(event.getWorld()) &&
           event.getWorld().getGenerator() instanceof BukkitChunkGeneratorWrapper bukkitChunkGeneratorWrapper) {
            INJECT_LOCK.lock();
            INJECTED.add(event.getWorld());
            LOGGER.info("Preparing to take over the world: {}", event.getWorld().getName());
            CraftWorld craftWorld = (CraftWorld) event.getWorld();
            ServerLevel serverWorld = craftWorld.getHandle();
            
            ConfigPack pack = bukkitChunkGeneratorWrapper.getPack();
            
            ChunkGenerator vanilla = serverWorld.getChunkSource().getGenerator();
            NMSBiomeProvider provider = new NMSBiomeProvider(pack.getBiomeProvider(), craftWorld.getSeed());
            NMSChunkGeneratorDelegate custom = new NMSChunkGeneratorDelegate(vanilla, pack, provider, craftWorld.getSeed());
    
            custom.conf = vanilla.conf; // world config from Spigot
    
            serverWorld.getChunkSource().chunkMap.generator = custom;
    
            LOGGER.info("Successfully injected into world.");
    
            INJECT_LOCK.unlock();
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockGrow(BlockGrowEvent event) {
        event.setCancelled(onGrow(event));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFertilize(BlockFertilizeEvent event) {
        event.setCancelled(onGrow(event));
    }
    
    public boolean onGrow(BlockEvent event) {
        Block block = event.getBlock();
        Vector3Int pos = Vector3Int.of(block.getX(), block.getY(), block.getZ());
        ServerWorld world = BukkitAdapter.adapt(block.getWorld());
        return FertilizableUtil.grow(world, new Random(), pos, ResourceLocation.tryParse(block.getType().getKey().asString()));
    }
}
