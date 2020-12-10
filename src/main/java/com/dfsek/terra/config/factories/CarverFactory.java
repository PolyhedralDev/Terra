package com.dfsek.terra.config.factories;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.Terra;
import com.dfsek.terra.api.gaea.math.MathUtil;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.templates.CarverTemplate;
import parsii.tokenizer.ParseException;

import java.util.Arrays;
import java.util.List;

public class CarverFactory implements TerraFactory<CarverTemplate, UserDefinedCarver> {
    private final ConfigPack pack;

    public CarverFactory(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public UserDefinedCarver build(CarverTemplate config, Terra main) throws LoadException {
        double[] start = new double[] {config.getStartX(), config.getStartY(), config.getStartZ()};
        double[] mutate = new double[] {config.getMutateX(), config.getMutateY(), config.getMutateZ()};
        List<String> radius = Arrays.asList(config.getRadMX(), config.getRadMY(), config.getRadMZ());
        long hash = MathUtil.hashToLong(config.getID());
        UserDefinedCarver carver;
        try {
            carver = new UserDefinedCarver(config.getHeight(), config.getLength(), start, mutate, radius, pack.getVarScope(), hash, config.getCutTop(), config.getCutBottom(), config, main);
        } catch(ParseException e) {
            throw new LoadException("Unable to parse radius equations", e);
        }
        carver.setRecalc(config.getRecalc());
        carver.setRecalcMagnitude(config.getRecaclulateMagnitude());
        return carver;
    }
}
