package com.dfsek.terra.bukkit;

import com.dfsek.terra.bukkit.config.VanillaBiomeProperties;
import com.dfsek.terra.bukkit.world.BukkitBiomeInfo;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import com.dfsek.terra.bukkit.world.BiomeHandler;
import com.dfsek.terra.registry.master.ConfigRegistry;

import dev.wyck.biome.Biome;
import dev.wyck.keys.ResourceKey;
import dev.wyck.registry.bootstrap.BootstrapBiomeRegistry;
import dev.wyck.registry.bootstrap.Composer;
import dev.wyck.tags.TagSet;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Bootstrapper implements PluginBootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrapper.class);

    private static final Map<ResourceKey, List<ResourceKey>> terraBiomeMap = new HashMap<>();

    @Override
    public void bootstrap(BootstrapContext context) {
        BootstrapBiomeRegistry registry = BootstrapBiomeRegistry.compose(context, Composer.DATAPACK);

        registry.deferring(() -> {
            registerBiomes(context, ); // TODO: Get raw config registry instance here
        });
    }

    public static void registerBiomes(BootstrapContext context, ConfigRegistry configRegistry) {
        BootstrapBiomeRegistry biomeRegistry = BootstrapBiomeRegistry.compose(context, Composer.DATAPACK);

        // Register the terra biomes to the registry
        configRegistry.forEach(pack -> pack.getRegistry(com.dfsek.terra.api.world.biome.Biome.class).forEach((key, biome) -> {
            try {
                BukkitPlatformBiome platformBiome = (BukkitPlatformBiome) biome.getPlatformBiome();
                ResourceKey vanillaMinecraftKey = ResourceKey.of(platformBiome.getHandle().getKey());
                VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);
                Biome wyckBiome = BiomeHandler.createBiome(vanillaMinecraftKey, vanillaBiomeProperties);

                ResourceKey delegateKey = ResourceKey.of("terra", BiomeHandler.createBiomeID(pack, key));

                biomeRegistry.queue(wyckBiome);

                platformBiome.getContext().put(new BukkitBiomeInfo(delegateKey));

                // TODO: Reimplement VillagerTypes - ideally this could be made into an EnvironmentAttribute in Wyck
                //                Map<ResourceKey<Biome>, ResourceKey<VillagerType>> villagerMap = Reflection.VILLAGER_TYPE.getByBiome();
                //
                //                villagerMap.put(delegateKey,
                //                    Objects.requireNonNullElse(vanillaBiomeProperties.getVillagerType(),
                //                        villagerMap.getOrDefault(delegateKey, VillagerType.PLAINS)));

                terraBiomeMap.computeIfAbsent(vanillaMinecraftKey, i -> new ArrayList<>()).add(delegateKey.identifier());

                LOGGER.debug("Registered biome: " + delegateKey);
            } catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }));

        // Add each terra biome to the same tag sets as it's associated vanilla biome
        terraBiomeMap.forEach((vb, terraBiomes) -> {
            Biome vanilla = BiomeHandler.getBiome(vb);
            if(vanilla != null) {
                terraBiomes.forEach(tb -> {
                    Biome terra = BiomeHandler.getBiome(tb);
                    if(terra != null) {
                        LOGGER.debug("{} (vanilla for {}): {}",
                            vanilla.resourceKey().identifier(),
                            terra.resourceKey().identifier(),
                            vanilla.tags().toList());

                        vanilla.tags() // TODO: Waiting on implementation from Wyck
                            .forEach(tag -> TagSet.ofBiomes(tag.key(), terra).append());
                    } else {
                        LOGGER.error("No such biome: {}", tb);
                    }
                });
            } else {
                LOGGER.error("No vanilla biome: {}", vb);
            }
        });
    }
}
