package com.dfsek.terra.region;

import com.dfsek.terra.StandalonePlugin;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.async.Counter;
import com.dfsek.terra.async.GenerationWorker;
import com.dfsek.terra.generation.MasterChunkGenerator;
import com.dfsek.terra.generation.math.SamplerCache;
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
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicLong;

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

        AtomicLong l = new AtomicLong(System.nanoTime());

        Counter counter = new Counter((id) -> {
            if(id % 200 == 0) {
                long c = System.nanoTime();

                long diff = c - l.get();

                double ms = (double) diff / 1000000;

                System.out.println("[" + Thread.currentThread().getName() + "] Generated " + id + " chunks. " + (200d / ms) * 1000 + " cps. " + world.getFiles().size() + " Regions loaded.");
                l.set(System.nanoTime());
            }
        });


        List<GenerationWorker> workers = new GlueList<>();


        for(int x = -rad / 32; x <= rad / 32; x++) {
            for(int z = -rad / 32; z <= rad / 32; z++) {
                workers.add(new GenerationWorker(x, z, world, this, counter));
            }
        }


        ForkJoinTask.invokeAll(workers);


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

        System.out.println("Done in " + (System.nanoTime() - l.get()) / 1000000000 + "s");
    }

    public StructurePopulator getStructurePopulator() {
        return structurePopulator;
    }

    public OrePopulator getOrePopulator() {
        return orePopulator;
    }

    public CavePopulator getCavePopulator() {
        return cavePopulator;
    }

    public TreePopulator getTreePopulator() {
        return treePopulator;
    }

    public FloraPopulator getFloraPopulator() {
        return floraPopulator;
    }

    public long getSeed() {
        return seed;
    }

    public MasterChunkGenerator getGenerator() {
        return generator;
    }
}
