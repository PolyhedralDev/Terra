package com.dfsek.terra.config.factories;

import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.templates.CarverTemplate;

public class CarverFactory implements TerraFactory<CarverTemplate, UserDefinedCarver> {
    @Override
    public UserDefinedCarver build(CarverTemplate config) {
        double[] start = new double[] {config.getStartX(), config.getStartY(), config.getStartZ()};
        double[] mutate = new double[] {config.getMutateX(), config.getMutateY(), config.getMutateZ(), config.getMutateRadius()};
        double[] radius = new double[] {config.getRadMX(), config.getRadMY(), config.getRadMZ()};
        int hash = config.getId().hashCode();
        return new UserDefinedCarver(config.getHeight(), config.getRadius(), config.getLength(), start, mutate, radius, hash, config.getCutTop(), config.getCutBottom(), config);
    }
}
