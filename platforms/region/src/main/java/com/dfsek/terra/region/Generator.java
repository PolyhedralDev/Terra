package com.dfsek.terra.region;

import com.dfsek.terra.StandalonePlugin;
import com.dfsek.terra.api.platform.generator.ChunkGenerator;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.generation.MasterChunkGenerator;
import com.dfsek.terra.generation.math.SamplerCache;
import com.dfsek.terra.platform.DirectChunkData;
import com.dfsek.terra.platform.DirectWorld;
import com.dfsek.terra.platform.GenWrapper;
import net.querz.mca.Chunk;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generator {
    private final Map<Long, MCAFile> files = new HashMap<>();

    private final long seed;

    public Generator(long seed) {
        this.seed = seed;
    }

    public void generate() throws IOException {

        int rad = 50;

        StandalonePlugin plugin = new StandalonePlugin();
        plugin.load();


        MasterChunkGenerator generator = new MasterChunkGenerator(plugin.getRegistry().get("DEFAULT"), plugin, new SamplerCache(plugin));
        GenWrapper wrapper = new GenWrapper(generator);

        int count = 0;

        List<Double> times = new GlueList<>();

        for(int cx = -rad; cx <= rad; cx++) {
            for(int cz = -rad; cz <= rad; cz++) {
                long start = System.nanoTime();

                long key = (((long) MCAUtil.chunkToRegion(cx)) << 32) | (MCAUtil.chunkToRegion(cz) & 0xffffffffL);

                int finalCx = cx;
                int finalCz = cz;
                MCAFile file = files.computeIfAbsent(key, k -> new MCAFile(MCAUtil.chunkToRegion(finalCx), MCAUtil.chunkToRegion(finalCz)));

                Chunk chunk = Chunk.newChunk();

                ChunkGenerator.ChunkData chunkData = new DirectChunkData(chunk, cx, cz);
                generator.generateChunkData(new DirectWorld(seed, wrapper), null, cx, cz, chunkData);

                file.setChunk(cx, cz, chunk);

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

        for(Map.Entry<Long, MCAFile> entry : files.entrySet()) {
            entry.getValue().cleanupPalettesAndBlockStates();
            int x = (int) (entry.getKey() >> 32);
            int z = (int) (long) entry.getKey();
            File file = new File("region", MCAUtil.createNameFromRegionLocation(x, z));
            file.getParentFile().mkdirs();
            MCAUtil.write(entry.getValue(), file);
        }
    }
}
