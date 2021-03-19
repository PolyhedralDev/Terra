package com.dfsek.terra.region;

import com.dfsek.terra.StandalonePlugin;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.platform.DirectWorld;
import com.dfsek.terra.platform.GenWrapper;
import com.dfsek.terra.world.generation.generators.DefaultChunkGenerator3D;
import com.dfsek.terra.world.population.FloraPopulator;
import com.dfsek.terra.world.population.OrePopulator;
import com.dfsek.terra.world.population.StructurePopulator;
import com.dfsek.terra.world.population.TreePopulator;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Generator {
    private final Logger logger = LoggerFactory.getLogger(Generator.class);
    private final long seed;
    private final List<ChunkCoordinate> chunkList;
    FloraPopulator floraPopulator;
    StructurePopulator structurePopulator;
    TreePopulator treePopulator;
    OrePopulator orePopulator;
    DefaultChunkGenerator3D chunkGenerator;

    public Generator(long seed, StandalonePlugin plugin) {
        plugin.load();
        floraPopulator = new FloraPopulator(plugin);
        structurePopulator = new StructurePopulator(plugin);
        treePopulator = new TreePopulator(plugin);
        orePopulator = new OrePopulator(plugin);
        chunkGenerator = new DefaultChunkGenerator3D(plugin.getConfigRegistry().get("DEFAULT"), plugin);
        this.seed = seed;

        chunkList = new GlueList<>();
    }

    public void addChunk(int x, int z) {
        this.chunkList.add(new ChunkCoordinate(x, z));
    }

    public void generate() throws IOException {
        logger.info("Total mem: {}GB", ((double) Runtime.getRuntime().maxMemory() / 1024 / 1024) / 1024);


//        GenWrapper wrapper = new GenWrapper(generator);
//        DirectWorld world = new DirectWorld(seed, wrapper);
        GenWrapper wrapper = new GenWrapper(chunkGenerator);
        DirectWorld world = new DirectWorld(seed, wrapper);

        GenerationManager manager = new GenerationManager(world);

        long l = System.nanoTime();

        chunkList.parallelStream()
                .sorted(Comparator.comparingDouble(c -> Math.sqrt(((double) c.getX() * c.getX()) + ((double) c.getZ() * c.getZ()))))
                .forEach(c -> manager.registerGenerationTask(chunk -> {
                    chunkGenerator.generateChunkData(world, null, chunk.getX(), chunk.getZ(), chunk);

                    structurePopulator.populate(world, chunk);
                    orePopulator.populate(world, chunk);
                    floraPopulator.populate(world, chunk);
                    treePopulator.populate(world, chunk);

                    return chunk;
                }, c.getX(), c.getZ()));

        logger.info("Successfully registered all generation tasks!");

        try {
            manager.awaitTermination();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }


        logger.info("Saving...");

        for(Map.Entry<Long, MCAFile> entry : world.getFiles().entrySet()) {
            if(entry.getValue() == null) continue;
            entry.getValue().cleanupPalettesAndBlockStates();
            int x = (int) (entry.getKey() >> 32);
            int z = (int) (long) entry.getKey();
            File file = new File("region", MCAUtil.createNameFromRegionLocation(x, z));
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            MCAUtil.write(entry.getValue(), file);
        }

        logger.info("Done in {}s", (System.nanoTime() - l) / 1000000000);
    }
}
