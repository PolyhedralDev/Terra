package com.dfsek.terra.addons.feature.locator.locators;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.BinaryColumn;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.world.Column;
import net.jafama.FastMath;

import java.util.List;

public class NoiseLocator implements Locator {
    private final List<NoiseSampler> samplers;

    public NoiseLocator(List<NoiseSampler> samplers) {
        this.samplers = samplers;
    }

    @Override
    public BinaryColumn getSuitableCoordinates(Column column) {
        BinaryColumn results = new BinaryColumn(column.getMinY(), column.getMaxY());

        long seed = column.getWorld().getSeed();
        samplers.forEach(sampler -> {
            int y = FastMath.floorToInt(sampler.getNoiseSeeded(seed, column.getX(), column.getX()));
            if(y >= column.getMaxY() || y < column.getMinY()) return;
            results.set(y);
        });

        return results;
    }
}
