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

package com.dfsek.terra.bukkit.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Wolf.Variant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.hooks.MultiverseGeneratorPluginHook;
import com.dfsek.terra.bukkit.world.BukkitBiomeInfo;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;


/**
 * Listener for events on all implementations.
 */
public class CommonListener implements Listener {
    private static final Logger logger = LoggerFactory.getLogger(CommonListener.class);
    private static final List<SpawnReason> WOLF_VARIANT_SPAWN_REASONS = List.of(
        SpawnReason.SPAWNER, SpawnReason.TRIAL_SPAWNER, SpawnReason.SPAWNER_EGG, SpawnReason.NATURAL
    );
    private final Platform platform;

    public CommonListener(Platform platform) {
        this.platform = platform;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if(event.getPlugin().getName().equals("Multiverse-Core")) {
            try {
                Class.forName("org.mvplugins.multiverse.core.MultiverseCoreApi");
                MultiverseGeneratorPluginHook.register(platform);
            } catch(ClassNotFoundException e) {
                logger.debug("Multiverse v5 is not installed.");
            } catch(IllegalStateException e) {
                logger.error("Failed to register Terra generator plugin to multiverse.", e);
            }
        }
    }

    private void applyWolfVariant(Wolf wolf) {
        // Doesn't apply if variant has already been applied
        if(wolf.getVariant() != Variant.PALE) {
            return;
        }

        World world = wolf.getWorld();
        if(!(world.getGenerator() instanceof BukkitChunkGeneratorWrapper wrapper)) {
            return;
        }

        ConfigPack pack = platform.getConfigRegistry().get(wrapper.getPack().getRegistryKey()).orElse(null);
        if(pack == null) {
            return;
        }

        NamespacedKey biomeKey = wolf.getWorld().getBiome(wolf.getLocation()).getKey();
        pack.getBiomeProvider().stream()
            .filter(biome -> {
                NamespacedKey key = ((BukkitPlatformBiome) biome.getPlatformBiome()).getContext()
                    .get(BukkitBiomeInfo.class)
                    .biomeKey();
                return key.equals(biomeKey);
            })
            .findFirst()
            .ifPresent(biome -> {
                NamespacedKey vanillaBiomeKey = ((BukkitPlatformBiome) biome.getPlatformBiome()).getHandle().getKey();
                switch(vanillaBiomeKey.toString()) {
                    case "minecraft:snowy_taiga" -> wolf.setVariant(Variant.ASHEN);
                    case "minecraft:old_growth_pine_taiga" -> wolf.setVariant(Variant.BLACK);
                    case "minecraft:old_growth_spruce_taiga" -> wolf.setVariant(Variant.CHESTNUT);
                    case "minecraft:grove" -> wolf.setVariant(Variant.SNOWY);
                    case "minecraft:forest" -> wolf.setVariant(Variant.WOODS);
                }
            });
    }

    @EventHandler
    public void onWolfSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Wolf wolf)) {
            return;
        }

        if (!WOLF_VARIANT_SPAWN_REASONS.contains(event.getSpawnReason())) {
            logger.debug("Ignoring wolf spawned with reason: " + event.getSpawnReason());
            return;
        }

        applyWolfVariant(wolf);
    }

    @EventHandler
    public void onChunkGenerate(ChunkLoadEvent event) {
        if (!event.isNewChunk()) {
            return;
        }

        for (Entity entity : event.getChunk().getEntities()) {
            if (entity instanceof Wolf wolf) {
                applyWolfVariant(wolf);
            }
        }
    }
}
