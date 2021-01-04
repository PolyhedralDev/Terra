package com.dfsek.terra.async;

import com.dfsek.terra.DirectUtils;
import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.platform.DirectChunkData;
import com.dfsek.terra.platform.DirectWorld;
import com.dfsek.terra.region.Generator;
import net.querz.mca.MCAUtil;

import java.io.File;
import java.util.concurrent.RecursiveAction;

public class GenerationWorker extends RecursiveAction {
    private final int x;
    private final int z;
    private final DirectWorld world;
    private final Generator generator;
    private final Counter counter;

    public GenerationWorker(int x, int z, DirectWorld world, Generator generator, Counter counter) {
        this.x = x;
        this.z = z;
        this.world = world;
        this.generator = generator;
        this.counter = counter;
    }


    @Override
    protected void compute() {
        for(int cx = x * 32; cx < x * 32 + 32; cx++) {
            for(int cz = z * 32; cz <= z * 32 + 32; cz++) {
                DirectChunkData chunkData = (DirectChunkData) world.getChunkAt(cx, cz);
                generator.getGenerator().generateChunkData(world, null, cx, cz, chunkData);

                generator.getCavePopulator().populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, world.getSeed())), chunkData);
                generator.getStructurePopulator().populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, world.getSeed())), chunkData);
                generator.getOrePopulator().populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, world.getSeed())), chunkData);
                generator.getFloraPopulator().populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, world.getSeed())), chunkData);
                generator.getTreePopulator().populate(world, new FastRandom(MathUtil.getCarverChunkSeed(cx, cz, world.getSeed())), chunkData);

                counter.add();
            }
        }
        long regionID = DirectUtils.regionID(x, z);
        try {
            File file = new File("region", MCAUtil.createNameFromRegionLocation(x, z));
            file.getParentFile().mkdirs();
            MCAUtil.write(world.getFiles().get(regionID), file);
        } catch(Exception e) {
            e.printStackTrace();
        }
        world.getFiles().remove(regionID);
        System.out.println("Saved region [" + x + "," + z + "]. " + world.getFiles().size() + " regions remain loaded.");
    }
}
