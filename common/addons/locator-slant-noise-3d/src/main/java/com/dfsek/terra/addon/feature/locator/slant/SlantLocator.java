package com.dfsek.terra.addon.feature.locator.slant;

import java.util.function.DoublePredicate;

import com.dfsek.terra.addons.chunkgenerator.generation.NoiseChunkGenerator3D;
import com.dfsek.terra.api.structure.feature.BinaryColumn;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.util.Column;


public class SlantLocator implements Locator {
    
    private final DoublePredicate predicate;
    
    public SlantLocator(DoublePredicate predicate) {
        this.predicate = predicate;
    }
    
    @Override
    public BinaryColumn getSuitableCoordinates(Column<?> column) {
        return column.newBinaryColumn(y -> {
            int x = column.getX();
            int z = column.getZ();
            World world = column.getWorld();
            NoiseChunkGenerator3D generator = (NoiseChunkGenerator3D) world.getGenerator();
            BiomeProvider biomeProvider = world.getBiomeProvider();
            return predicate.test(generator.getSlant(x, y, z, world, biomeProvider));
        });
    }
}
