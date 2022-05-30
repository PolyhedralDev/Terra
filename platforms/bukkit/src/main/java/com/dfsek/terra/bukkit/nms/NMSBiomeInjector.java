package com.dfsek.terra.bukkit.nms;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.bukkit.config.VanillaBiomeProperties;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import com.dfsek.terra.registry.master.ConfigRegistry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryWritable;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.data.RegistryGeneration;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import net.minecraft.world.level.biome.BiomeFog.GrassColor;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.biome.BiomeSettingsMobs;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Objects;


public class NMSBiomeInjector {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMSBiomeInjector.class);
    
    public static void registerBiomes(ConfigRegistry configRegistry) {
        CraftServer craftserver = (CraftServer) Bukkit.getServer();
        DedicatedServer dedicatedserver = craftserver.getServer();
        
        IRegistryWritable<BiomeBase> biomeRegistry = (IRegistryWritable<BiomeBase>) dedicatedserver
                .aU() // getRegistryManager
                .b( // getRegistry
                    IRegistry.aP // biome registry key
                  );
        
        try {
            LOGGER.info("Hacking biome registry...");
            Field frozen = RegistryMaterials.class.getDeclaredField("bL"); // registry frozen field
            frozen.setAccessible(true);
            frozen.set(biomeRegistry, false);
            
            configRegistry.forEach(pack -> pack.getRegistry(Biome.class).forEach((key, biome) -> {
                try {
                    BukkitPlatformBiome platformBiome = (BukkitPlatformBiome) biome.getPlatformBiome();
                    NamespacedKey vanillaBukkitKey = platformBiome.getHandle().getKey();
                    BiomeBase platform = createBiome(
                            biome,
                            biomeRegistry.a(new MinecraftKey(vanillaBukkitKey.getNamespace(), vanillaBukkitKey.getKey()))
                                                    );
                    
                    ResourceKey<BiomeBase> delegateKey = ResourceKey.a(IRegistry.aP, new MinecraftKey("terra", createBiomeID(pack, key)));
                    
                    RegistryGeneration.a(RegistryGeneration.i, delegateKey, platform);
                    Holder<BiomeBase> resourceKey = biomeRegistry.a(delegateKey, platform, Lifecycle.stable());
                    platformBiome.setResourceKey(resourceKey);
                    
                    LOGGER.info("Registered biome: " + delegateKey);
                } catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }));
            
            frozen.set(biomeRegistry, true); // freeze registry again :)
        } catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    private static BiomeBase createBiome(Biome biome, BiomeBase vanilla)
    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        BiomeBase.a builder = new BiomeBase.a(); // Builder
        
        Field f = BiomeBase.class.getDeclaredField("l"); // category
        f.setAccessible(true);
        builder.a((BiomeBase.Geography) f.get(vanilla))
               .a(vanilla.c()); // getPrecipitation
        
        
        Field biomeSettingMobsField = BiomeBase.class.getDeclaredField("k"); // spawn settings
        biomeSettingMobsField.setAccessible(true);
        BiomeSettingsMobs biomeSettingMobs = (BiomeSettingsMobs) biomeSettingMobsField.get(vanilla);
        builder.a(biomeSettingMobs);
        
        Field biomeSettingGenField = BiomeBase.class.getDeclaredField("j");
        biomeSettingGenField.setAccessible(true);
        BiomeSettingsGeneration biomeSettingGen = (BiomeSettingsGeneration) biomeSettingGenField.get(vanilla);
        builder.a(biomeSettingGen)
                .a(vanilla.c())
                .b(vanilla.h()) // precipitation
                .a(vanilla.i()); // temp
        
        
        BiomeFog.a effects = new BiomeFog.a(); // Builder
        effects.a(GrassColor.a); // magic
        
        VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);
        
        // fog
        effects.a(Objects.requireNonNullElse(vanillaBiomeProperties.getFogColor(), vanilla.f()));
        
        // water
        effects.b(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.k()));
        
        // water fog
        effects.c(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterFogColor(), vanilla.l()));
        
        // sky
        effects.d(Objects.requireNonNullElse(vanillaBiomeProperties.getSkyColor(), vanilla.a()));
        
        
        // foliage
        effects.e(Objects.requireNonNullElse(vanillaBiomeProperties.getFoliageColor(), vanilla.g()));
        
        // grass
        effects.f(Objects.requireNonNullElse(vanillaBiomeProperties.getGrassColor(), vanilla.j().g().a(0, 0, 0)));
        
        builder.a(effects.a()); // build()
        
        return builder.a(); // build()
    }
    
    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID()
                   .toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }
}