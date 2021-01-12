package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.noise.samplers.FastNoiseLite;
import com.dfsek.terra.api.math.noise.samplers.Normalizer;
import com.dfsek.terra.generation.config.NoiseBuilder;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NoiseBuilderLoader implements TypeLoader<NoiseBuilder> {
    private static final ConfigLoader LOADER = new ConfigLoader();

    static {
        LOADER.registerLoader(FastNoiseLite.NoiseType.class, (t, object, cf) -> FastNoiseLite.NoiseType.valueOf((String) object))
                .registerLoader(FastNoiseLite.FractalType.class, (t, object, cf) -> FastNoiseLite.FractalType.valueOf((String) object))
                .registerLoader(FastNoiseLite.DomainWarpType.class, (t, object, cf) -> FastNoiseLite.DomainWarpType.valueOf((String) object))
                .registerLoader(FastNoiseLite.RotationType3D.class, (t, object, cf) -> FastNoiseLite.RotationType3D.valueOf((String) object))
                .registerLoader(FastNoiseLite.CellularReturnType.class, (t, object, cf) -> FastNoiseLite.CellularReturnType.valueOf((String) object))
                .registerLoader(FastNoiseLite.CellularDistanceFunction.class, (t, object, cf) -> FastNoiseLite.CellularDistanceFunction.valueOf((String) object))
                .registerLoader(NoiseBuilder.class, new NoiseBuilderLoader())
                .registerLoader(Normalizer.NormalType.class, (t, o, l) -> Normalizer.NormalType.valueOf(o.toString().toUpperCase()));
    }

    @Override
    public NoiseBuilder load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        NoiseBuilder builder = new NoiseBuilder();
        try {
            LOADER.load(builder, new Configuration((Map<String, Object>) o));
        } catch(ConfigException e) {
            throw new LoadException("Could not load noise", e);
        }
        return builder;
    }
}
