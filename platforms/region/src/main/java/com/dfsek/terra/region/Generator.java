package com.dfsek.terra.region;

import com.dfsek.terra.StandalonePlugin;
import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.util.GlueList;
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
import java.util.List;
import java.util.Map;

public class Generator {
    private final long seed;

    public Generator(long seed) {
        this.seed = seed;
    }

    public void generate() throws IOException {

        int rad = 25;

        StandalonePlugin plugin = new StandalonePlugin();
        plugin.load();


        MasterChunkGenerator generator = new MasterChunkGenerator(plugin.getRegistry().get("DEFAULT"), plugin, new SamplerCache(plugin));
        GenWrapper wrapper = new GenWrapper(generator);

        FloraPopulator floraPopulator = new FloraPopulator(plugin);
        StructurePopulator structurePopulator = new StructurePopulator(plugin);
        TreePopulator treePopulator = new TreePopulator(plugin);
        OrePopulator orePopulator = new OrePopulator(plugin);
        CavePopulator cavePopulator = new CavePopulator(plugin);

        int count = 0;

        List<Double> times = new GlueList<>();

        DirectWorld world = new DirectWorld(seed, wrapper);

        for(int cx = -rad; cx <= rad; cx++) {
            for(int cz = -rad; cz <= rad; cz++) {
                long start = System.nanoTime();

                DirectChunkData chunkData = (DirectChunkData) world.getChunkAt(cx, cz);
                generator.generateChunkData(world, null, cx, cz, chunkData);

                cavePopulator.populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, seed)), chunkData);
                structurePopulator.populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, seed)), chunkData);
                orePopulator.populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, seed)), chunkData);
                floraPopulator.populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, seed)), chunkData);
                treePopulator.populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, seed)), chunkData);


                long end = System.nanoTime() - start;
                count++;
                times.add((double) end / 1000000);

                if(count % 200 == 0) {
                    double total = 0;
                    for(double d : times) total += d;
                    double avg = total / count;

                    double pct = ((double) count / (rad * rad * 2 * 2)) * 100D;

                    plugin.getLogger().info("Generated " + count + " chunks. " + (1 / avg) * 1000 + "cps; " + pct + "%");
                }
            }
        }

        for(Map.Entry<Long, MCAFile> entry : world.getFiles().entrySet()) {
            if(entry.getValue() == null) continue;
            entry.getValue().cleanupPalettesAndBlockStates();
            int x = (int) (entry.getKey() >> 32);
            int z = (int) (long) entry.getKey();
            File file = new File("region", MCAUtil.createNameFromRegionLocation(x, z));
            file.getParentFile().mkdirs();
            MCAUtil.write(entry.getValue(), file);
        }
    }
}
