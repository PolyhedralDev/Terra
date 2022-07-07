package com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation;

import net.jafama.FastMath;

import com.dfsek.terra.addons.chunkgenerator.config.noise.BiomeNoiseProperties;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

import static com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation.Interpolator.lerp;


public class LazilyEvaluatedInterpolator {
    private final Double[] samples; //
    
    private final int chunkX;
    private final int chunkZ;
    
    private final int horizontalRes;
    private final int verticalRes;
    
    private final BiomeProvider biomeProvider;
    private final PropertyKey<BiomeNoiseProperties> noisePropertiesKey;
    
    private final long seed;
    private final int min, max;
    
    private final int zMul, yMul;
    
    public LazilyEvaluatedInterpolator(BiomeProvider biomeProvider, int cx, int cz, int max,
                                       PropertyKey<BiomeNoiseProperties> noisePropertiesKey, int min, int horizontalRes, int verticalRes,
                                       long seed) {
        this.noisePropertiesKey = noisePropertiesKey;
        int hSamples = FastMath.ceilToInt(16.0 / horizontalRes);
        int vSamples = FastMath.ceilToInt((double) (max - min) / verticalRes);
        this.zMul = (hSamples + 1);
        this.yMul = zMul * zMul;
        samples = new Double[yMul * (vSamples + 1)];
        this.chunkX = cx << 4;
        this.chunkZ = cz << 4;
        this.horizontalRes = horizontalRes;
        this.verticalRes = verticalRes;
        this.biomeProvider = biomeProvider;
        this.seed = seed;
        this.min = min;
        this.max = max - 1;
    }
    
    private double sample(int xIndex, int yIndex, int zIndex, int ox, int oy, int oz) {
        int index = xIndex + (zIndex * zMul) + (yIndex * yMul);
        Double sample = samples[index];
        if(sample == null) {
            int xi = ox + chunkX;
            int zi = oz + chunkZ;
            
            int y = FastMath.min(max, oy);
            
            sample = biomeProvider
                    .getBiome(xi, y, zi, seed)
                    .getContext()
                    .get(noisePropertiesKey)
                    .carving()
                    .noise(seed, xi, y, zi);
            samples[index] = sample;
        }
        return sample;
    }
    
    public double sample(int x, int y, int z) {
        int xIndex = x / horizontalRes;
        int yIndex = (y - min) / verticalRes;
        int zIndex = z / horizontalRes;
        
        double sample_0_0_0 = sample(xIndex, yIndex, zIndex, x, y, z);
        
        boolean yRange = y % verticalRes == 0;
        if(x % horizontalRes == 0 && yRange && z % horizontalRes == 0) { // we're at the sampling point
            return sample_0_0_0;
        }
        
        double sample_0_0_1 = sample(xIndex, yIndex, zIndex + 1, x, y, z + horizontalRes);
        
        double sample_1_0_0 = sample(xIndex + 1, yIndex, zIndex, x + horizontalRes, y, z);
        double sample_1_0_1 = sample(xIndex + 1, yIndex, zIndex + 1, x + horizontalRes, y, z + horizontalRes);
        
        double xFrac = (double) (x % horizontalRes) / horizontalRes;
        double zFrac = (double) (z % horizontalRes) / horizontalRes;
        double lerp_bottom_0 = lerp(zFrac, sample_0_0_0, sample_0_0_1);
        double lerp_bottom_1 = lerp(zFrac, sample_1_0_0, sample_1_0_1);
        
        double lerp_bottom = lerp(xFrac, lerp_bottom_0, lerp_bottom_1);
        
        if(yRange) { // we can do bilerp
            return lerp_bottom;
        }
        
        double yFrac = (double) FastMath.floorMod(y, verticalRes) / verticalRes;
        
        
        double sample_0_1_0 = sample(xIndex, yIndex + 1, zIndex, x, y + verticalRes, z);
        double sample_0_1_1 = sample(xIndex, yIndex + 1, zIndex + 1, x, y + verticalRes, z + horizontalRes);
        
        
        double sample_1_1_0 = sample(xIndex + 1, yIndex + 1, zIndex, x + horizontalRes, y + verticalRes, z);
        double sample_1_1_1 = sample(xIndex + 1, yIndex + 1, zIndex + 1, x + horizontalRes, y + verticalRes, z + horizontalRes);
        
        double lerp_top_0 = lerp(zFrac, sample_0_1_0, sample_0_1_1);
        double lerp_top_1 = lerp(zFrac, sample_1_1_0, sample_1_1_1);
        
        double lerp_top = lerp(xFrac, lerp_top_0, lerp_top_1);
        
        return lerp(yFrac, lerp_bottom, lerp_top);
    }
}
