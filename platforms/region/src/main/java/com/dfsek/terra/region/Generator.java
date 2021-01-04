package com.dfsek.terra.region;

import com.dfsek.terra.StandalonePlugin;
import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.generation.MasterChunkGenerator;
import com.dfsek.terra.generation.math.SamplerCache;
import com.dfsek.terra.platform.DirectChunkData;
import com.dfsek.terra.platform.DirectWorld;
import com.dfsek.terra.platform.GenWrapper;
import com.dfsek.terra.population.CavePopulator;
import com.dfsek.terra.population.FloraPopulator;
import com.dfsek.terra.population.OrePopulator;
import com.dfsek.terra.population.StructurePopulator;
import com.dfsek.terra.population.TreePopulator;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Generator {
    private final long seed;
    FloraPopulator floraPopulator;
    StructurePopulator structurePopulator;
    TreePopulator treePopulator;
    OrePopulator orePopulator;
    CavePopulator cavePopulator;
    MasterChunkGenerator generator;

    public Generator(long seed, StandalonePlugin plugin) {
        plugin.load();
        floraPopulator = new FloraPopulator(plugin);
        structurePopulator = new StructurePopulator(plugin);
        treePopulator = new TreePopulator(plugin);
        orePopulator = new OrePopulator(plugin);
        cavePopulator = new CavePopulator(plugin);
        generator = new MasterChunkGenerator(plugin.getRegistry().get("DEFAULT"), plugin, new SamplerCache(plugin));
        this.seed = seed;
    }

    public void generate() throws IOException {

        int rad = 64;
        System.out.println("Total mem: " + Runtime.getRuntime().maxMemory() / 1024 / 1024 / 1024 + "GB");


        GenWrapper wrapper = new GenWrapper(generator);
        DirectWorld world = new DirectWorld(seed, wrapper);

        long l = System.nanoTime();
        int count = 0;

        for(int cx = -rad; cx <= rad; cx++) {
            for(int cz = -rad; cz <= rad; cz++) {
                DirectChunkData chunkData = (DirectChunkData) world.getChunkAt(cx, cz);
                generator.generateChunkData(world, null, cx, cz, chunkData);

                cavePopulator.populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, world.getSeed())), chunkData);
                structurePopulator.populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, world.getSeed())), chunkData);
                orePopulator.populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, world.getSeed())), chunkData);
                floraPopulator.populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, world.getSeed())), chunkData);
                treePopulator.populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, world.getSeed())), chunkData);
                count++;

                if(count % 200 == 0) {
                    long n = System.nanoTime();

                    System.out.println("Generated " + count + " chunks. " + 200 / ((double) (n - l) / 1000000) * 1000 + "cps.");

                    l = System.nanoTime();

                }
            }
        }



        System.out.println("Saving...");

        for(Map.Entry<Long, MCAFile> entry : world.getFiles().entrySet()) {
            if(entry.getValue() == null) continue;
            entry.getValue().cleanupPalettesAndBlockStates();
            int x = (int) (entry.getKey() >> 32);
            int z = (int) (long) entry.getKey();
            File file = new File("region", MCAUtil.createNameFromRegionLocation(x, z));
            file.getParentFile().mkdirs();
            MCAUtil.write(entry.getValue(), file);
        }

        System.out.println("Done in " + (System.nanoTime() - l) / 1000000000 + "s");
    }
}
