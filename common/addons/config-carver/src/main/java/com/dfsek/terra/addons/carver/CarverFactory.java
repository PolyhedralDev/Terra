/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.carver;

import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.tectonic.exception.LoadException;

import java.util.Arrays;
import java.util.List;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.MathUtil;


public class CarverFactory implements ConfigFactory<CarverTemplate, UserDefinedCarver> {
    private final ConfigPack pack;
    
    public CarverFactory(ConfigPack pack) {
        this.pack = pack;
    }
    
    @Override
    public UserDefinedCarver build(CarverTemplate config, Platform platform) throws LoadException {
        double[] start = { config.getStartX(), config.getStartY(), config.getStartZ() };
        double[] mutate = { config.getMutateX(), config.getMutateY(), config.getMutateZ() };
        List<String> radius = Arrays.asList(config.getRadMX(), config.getRadMY(), config.getRadMZ());
        long hash = MathUtil.hashToLong(config.getID());
        UserDefinedCarver carver;
        try {
            carver = new UserDefinedCarver(config.getHeight(), config.getLength(), start, mutate, radius, new Scope(), hash,
                                           config.getCutTop(), config.getCutBottom(), config, platform);
        } catch(ParseException e) {
            throw new LoadException("Unable to parse radius equations", e);
        }
        carver.setRecalc(config.getRecalc());
        carver.setRecalcMagnitude(config.getRecaclulateMagnitude());
        return carver;
    }
}
