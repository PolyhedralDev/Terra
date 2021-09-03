package com.dfsek.terra.region;

import com.dfsek.terra.StandalonePlugin;
import com.dfsek.terra.api.world.generator.TerraChunkGenerator;
import com.dfsek.terra.platform.DirectChunkData;
import com.dfsek.terra.platform.DirectWorld;

import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class Generator {
    private final long seed;
    TerraChunkGenerator generator;
    
    public Generator(long seed, StandalonePlugin plugin) {
        plugin.load();
        //generator = new DefaultChunkGenerator3D(plugin.getConfigRegistry().get("DEFAULT"), plugin);
        this.seed = seed;
    }
    
    public void generate() throws IOException {
        
        int rad = 64;
        System.out.println("Total mem: " + Runtime.getRuntime().maxMemory() / 1024 / 1024 / 1024 + "GB");
        
        
        DirectWorld world = new DirectWorld(seed, null);
        
        long l = System.nanoTime();
        int count = 0;
        
        for(int cx = -rad; cx <= rad; cx++) {
            for(int cz = -rad; cz <= rad; cz++) {
                DirectChunkData chunkData = (DirectChunkData) world.getChunkAt(cx, cz);
                generator.generateChunkData(world, null, cx, cz, chunkData);
                
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
