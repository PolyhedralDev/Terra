package com.dfsek.terra.bukkit.listeners;

import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityBreedEvent;

import java.util.Set;

import com.dfsek.terra.bukkit.BukkitPlatform;
import com.dfsek.terra.bukkit.world.BiomeHandler;


public class BiomeListener implements Listener {
    private static final Set<SpawnReason> BIOME_TYPED_VILLAGER_REASONS = Set.of(
        SpawnReason.SPAWNER_EGG, SpawnReason.DISPENSE_EGG, SpawnReason.SPAWNER, SpawnReason.COMMAND);

    private final BukkitPlatform platform;

    public BiomeListener(BukkitPlatform platform) {
        this.platform = platform;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onResourcesReloaded(ServerResourcesReloadedEvent event) {
        BiomeHandler.addToVanillaTags(platform.getRawConfigRegistry());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        SpawnReason reason = event.getSpawnReason();
        if(event.getEntity() instanceof Villager villager && BIOME_TYPED_VILLAGER_REASONS.contains(reason)) {
            applyBiomeType(villager);
        } else if(event.getEntity() instanceof ZombieVillager zombieVillager && reason != SpawnReason.INFECTION) {
            Villager.Type type = biomeType(zombieVillager);
            if(type != null && zombieVillager.getVillagerType() == Villager.Type.PLAINS) {
                zombieVillager.setVillagerType(type);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreed(EntityBreedEvent event) {
        if(event.getEntity() instanceof Villager child && isNotPlains(event.getMother()) && isNotPlains(event.getFather())) {
            applyBiomeType(child);
        }
    }

    private static void applyBiomeType(Villager villager) {
        Villager.Type type = biomeType(villager);
        if(type != null && villager.getVillagerType() == Villager.Type.PLAINS) {
            villager.setVillagerType(type);
        }
    }

    private static Villager.Type biomeType(Entity entity) {
        Location location = entity.getLocation();
        if(!entity.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) return null;
        return BiomeHandler.getVillagerType(entity.getWorld().getBiome(location));
    }

    private static boolean isNotPlains(LivingEntity parent) {
        return parent instanceof Villager villager && villager.getVillagerType() != Villager.Type.PLAINS;
    }
}
