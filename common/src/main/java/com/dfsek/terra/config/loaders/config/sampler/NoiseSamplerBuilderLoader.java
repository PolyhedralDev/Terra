package com.dfsek.terra.config.loaders.config.sampler;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.normalizer.LinearNormalizer;
import com.dfsek.terra.api.math.noise.normalizer.NormalNormalizer;
import com.dfsek.terra.api.math.noise.normalizer.Normalizer;
import com.dfsek.terra.api.math.noise.samplers.DomainWarpedSampler;
import com.dfsek.terra.api.math.noise.samplers.ImageSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.fileloaders.Loader;
import com.dfsek.terra.world.generation.config.NoiseBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NoiseSamplerBuilderLoader implements TypeLoader<NoiseSeeded> {
    private final Loader fileAccess;

    public NoiseSamplerBuilderLoader(Loader fileAccess) {
        this.fileAccess = fileAccess;
    }

    @Override
    public NoiseSeeded load(Type t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) c;

        String samplerType = "NOISE";

        int dimensions = map.containsKey("dimensions")
                ? Integer.parseInt(map.get("dimensions").toString())
                : 2;

        if(map.containsKey("sampler-type")) {
            samplerType = map.get("sampler-type").toString();
        }

        if(samplerType.equals("NOISE")) {
            NoiseBuilder builder = new NoiseBuilder();
            try {
                loader.load(builder, new Configuration(map));
            } catch(ConfigException e) {
                throw new LoadException("Failed to load noise function", e);
            }
            return new NoiseSeeded() {
                @Override
                public NoiseSampler apply(Long seed) {
                    return builder.build(seed);
                }

                @Override
                public int getDimensions() {
                    return dimensions;
                }
            };
        } else if(samplerType.equals("NORMALIZER")) {
            Normalizer.NormalType normalType = (Normalizer.NormalType) loader.loadType(Normalizer.NormalType.class, map.get("type"));

            NoiseSeeded noise = (NoiseSeeded) loader.loadType(NoiseSeeded.class, map.get("function"));

            switch(normalType) {
                case LINEAR: {
                    if(!map.containsKey("max")) throw new LoadException("Max unspecified.");
                    if(!map.containsKey("min")) throw new LoadException("Min unspecified.");


                    int min = Integer.parseInt(map.get("min").toString());
                    int max = Integer.parseInt(map.get("max").toString());
                    return new NoiseSeeded() {
                        @Override
                        public NoiseSampler apply(Long seed) {
                            return new LinearNormalizer(noise.apply(seed), min, max);
                        }

                        @Override
                        public int getDimensions() {
                            return dimensions;
                        }
                    };
                }
                case NORMAL: {
                    if(!map.containsKey("mean")) throw new LoadException("Mean unspecified.");
                    if(!map.containsKey("standard-deviation")) throw new LoadException("Standard Deviation unspecified.");
                    if(!map.containsKey("groups")) throw new LoadException("Groups unspecified.");

                    double mean = Double.parseDouble(map.get("mean").toString());
                    double stdDev = Double.parseDouble(map.get("standard-deviation").toString());
                    int groups = Integer.parseInt(map.get("groups").toString());
                    return new NoiseSeeded() {
                        @Override
                        public NoiseSampler apply(Long seed) {
                            return new NormalNormalizer(noise.apply(seed), groups, mean, stdDev);
                        }

                        @Override
                        public int getDimensions() {
                            return dimensions;
                        }
                    };
                }
            }
        } else if(samplerType.equals("IMAGE")) {
            try {
                BufferedImage image = ImageIO.read(fileAccess.get(map.get("image").toString()));
                ImageSampler.Channel channel = ImageSampler.Channel.valueOf(map.get("channel").toString());
                double frequency = Double.parseDouble(map.get("frequency").toString());
                return new NoiseSeeded() {
                    @Override
                    public NoiseSampler apply(Long seed) {
                        return new ImageSampler(image, channel, frequency);
                    }

                    @Override
                    public int getDimensions() {
                        return dimensions;
                    }
                };
            } catch(IOException | NullPointerException e) {
                throw new LoadException("Failed to load image", e);
            }
        } else if(samplerType.equals("DOMAIN_WARP")) {
            NoiseSeeded warp = (NoiseSeeded) loader.loadType(NoiseSeeded.class, map.get("warp"));
            NoiseSeeded target = (NoiseSeeded) loader.loadType(NoiseSeeded.class, map.get("function"));
            double amplitude = ((Number) map.getOrDefault("amplitude", 1)).doubleValue();
            int salt = (Integer) map.getOrDefault("salt", 0);
            return new NoiseSeeded() {
                @Override
                public NoiseSampler apply(Long seed) {
                    return new DomainWarpedSampler(target.apply(seed), warp.apply(seed), (int) (seed + salt), amplitude);
                }

                @Override
                public int getDimensions() {
                    return dimensions;
                }
            };
        }

        throw new LoadException("No such noise sampler type \"" + samplerType + "\"");
    }
}
