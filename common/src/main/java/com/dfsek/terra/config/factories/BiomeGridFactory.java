package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.gaea.biome.Biome;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.builder.biomegrid.BiomeGridBuilder;
import com.dfsek.terra.config.builder.biomegrid.UserDefinedGridBuilder;
import com.dfsek.terra.config.templates.BiomeGridTemplate;

import java.util.List;

public class BiomeGridFactory implements TerraFactory<BiomeGridTemplate, BiomeGridBuilder> {

    @Override
    public UserDefinedGridBuilder build(BiomeGridTemplate config, TerraPlugin main) {

        UserDefinedGridBuilder holder = new UserDefinedGridBuilder();
        holder.setXFreq(config.getXFreq());
        holder.setZFreq(config.getZFreq());

        int xSize = config.getGrid().size();
        int zSize = config.getGrid().get(0).size();

        Biome[][] biomes = new UserDefinedBiome[xSize][zSize];

        for(int x = 0; x < xSize; x++) {
            List<Biome> layer = config.getGrid().get(x);
            if(!(layer.size() == zSize)) throw new IllegalArgumentException();
            for(int z = 0; z < zSize; z++) {
                biomes[x][z] = layer.get(z);
            }
        }
        holder.setBiomes(biomes);

        return holder;
    }
}
