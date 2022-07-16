package com.dfsek.terra.addons.chunkgenerator.generation;

import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.api.LayerResolver;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;
import com.dfsek.terra.api.world.info.WorldProperties;


public class LayeredChunkGenerator implements ChunkGenerator {
    
    private final Platform platform;
    private final LayerResolver resolver;
    
    public LayeredChunkGenerator(Platform platform, LayerResolver resolver) {
        this.platform = platform;
        this.resolver = resolver;
    }
    
    @Override
    public void generateChunkData(@NotNull ProtoChunk chunk, @NotNull WorldProperties world, @NotNull BiomeProvider biomeProvider,
                                  int chunkX, int chunkZ) {
    
        platform.getProfiler().push("chunk_base_layered");
        
        int xOrig = (chunkX << 4);
        int zOrig = (chunkZ << 4);
        long seed = world.getSeed();
        
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
    
                int cx = xOrig + x;
                int cz = zOrig + z;
                int paletteLevel = 0;
                LayerPalette previousLayerPalette = null;
                Column<Biome> biomeColumn = biomeProvider.getColumn(cx, cz, world);
                
                for(int y = world.getMaxHeight() - 1; y >= world.getMinHeight(); y--) {
                    
                    Biome biome = biomeColumn.get(y);
                    
                    LayerPalette layerPalette = resolver.resolve(seed, biome, cx, y, cz);
                    
                    if (previousLayerPalette == layerPalette) {
                        paletteLevel++;
                    } else if (layerPalette.resetsGroup()) {
                        paletteLevel = 0;
                    } else if (previousLayerPalette != null && layerPalette.getGroup() == previousLayerPalette.getGroup()) {
                        paletteLevel++;
                    } else {
                        paletteLevel = 0;
                    }
                    previousLayerPalette = layerPalette;
                    
                    Palette palette = layerPalette.get(seed, biome, cx, y, cz);
    
                    chunk.setBlock(cx, y, cz, palette.get(paletteLevel, cx, y, cz, seed));
                    
                }
            }
        }
    
        platform.getProfiler().pop("chunk_base_layered");
    }
    
    @Override
    public BlockState getBlock(WorldProperties world, int x, int y, int z, BiomeProvider biomeProvider) {
        long seed = world.getSeed();
        Biome biome = biomeProvider.getBiome(x, y, z, seed);
        int layer = 0; // Default to layer 0 for now
        return resolver.resolve(seed, biome, x, y, z)
                       .get(seed, biome, x, y, z)
                       .get(layer, x, y, z, seed);
    }
    
    @Override
    public Palette getPalette(int x, int y, int z, WorldProperties world, BiomeProvider biomeProvider) {
        long seed = world.getSeed();
        Biome biome = biomeProvider.getBiome(x, y, z, seed);
        return resolver.resolve(seed, biome, x, y, z)
                       .get(seed, biome, x, y, z);
    }
}
