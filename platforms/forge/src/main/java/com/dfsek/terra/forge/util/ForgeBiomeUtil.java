package com.dfsek.terra.forge.util;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent.RegisterHelper;

import com.dfsek.terra.mod.util.BiomeUtil;
import com.dfsek.terra.mod.util.MinecraftUtil;


public final class ForgeBiomeUtil extends BiomeUtil {
    static RegisterHelper<Biome> helper;
    
    public static void registerBiomes(RegisterHelper<Biome> helper) {
        ForgeBiomeUtil.helper = helper;
        registerBiomes();
    }
    
    protected static RegistryKey<net.minecraft.world.biome.Biome> registerBiome(Identifier identifier,
                                                                                net.minecraft.world.biome.Biome biome) {
        helper.register(MinecraftUtil.registerKey(identifier).getValue(), biome);
        return ForgeRegistries.BIOMES.getHolder(identifier)
                                     .orElseThrow()
                                     .getKey()
                                     .orElseThrow();
    }
}
