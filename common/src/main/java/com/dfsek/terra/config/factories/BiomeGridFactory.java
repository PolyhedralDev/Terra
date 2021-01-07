package com.dfsek.terra.config.factories;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.builder.biomegrid.BiomeGridBuilder;
import com.dfsek.terra.config.builder.biomegrid.UserDefinedGridBuilder;
import com.dfsek.terra.config.templates.BiomeGridTemplate;

import java.util.List;

public class BiomeGridFactory implements TerraFactory<BiomeGridTemplate, BiomeGridBuilder> {

    @Override
    public UserDefinedGridBuilder build(BiomeGridTemplate config, TerraPlugin main) throws LoadException {

        UserDefinedGridBuilder holder = new UserDefinedGridBuilder();

        int xSize = config.getGrid().size();
        int zSize = config.getGrid().get(0).size();

        holder.setXFreq(config.getXFreq() / xSize);
        holder.setZFreq(config.getZFreq() / zSize);

        Biome[][] biomes = new UserDefinedBiome[xSize][zSize];

        for(int x = 0; x < xSize; x++) {
            List<Biome> layer = config.getGrid().get(x);
            if(!(layer.size() == zSize)) throw new LoadException("Expected " + zSize + " biomes in row " + x + ", found " + layer.size());
            for(int z = 0; z < zSize; z++) {
                biomes[x][z] = layer.get(z);
            }
        }
        holder.setBiomes(biomes);

        return holder;
    }
}
