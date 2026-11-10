package com.dfsek.terra.bukkit.generator;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.bukkit.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.bukkit.attribute.PaperVanilla;
import com.dfsek.terra.bukkit.attribute.WyckBridge;


public final class PaperGenerators {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaperGenerators.class);

    private PaperGenerators() {
    }

    public static void install(World world, BukkitChunkGeneratorWrapper wrapper) {
        try {
            wrapper.bind(world);
            ServerLevel level = PaperVanilla.level(world);
            ChunkGenerator paper = level.getChunkSource().getGenerator();
            if(paper instanceof PaperChunkGenerator) return;
            long seed = level.getSeed();
            PaperBiomeSource biomes = new PaperBiomeSource(wrapper,
                PaperBiomeSource.registered(level.registryAccess().lookupOrThrow(Registries.BIOME)), seed);
            WyckBridge.installGenerator(world, new PaperChunkGenerator(paper, wrapper, biomes, seed));
        } catch(RuntimeException e) {
            LOGGER.warn("Could not replace Paper's generator in world \"{}\". Falling back to it: no structure beards, slower biome lookups",
                world.getName(), e);
        }
    }
}
