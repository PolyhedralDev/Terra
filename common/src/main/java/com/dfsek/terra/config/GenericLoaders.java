package com.dfsek.terra.config;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.LoaderRegistrar;
import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.math.GridSpawn;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.normalizer.Normalizer;
import com.dfsek.terra.api.math.noise.samplers.FastNoiseLite;
import com.dfsek.terra.api.math.noise.samplers.ImageSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.palette.holder.PaletteHolder;
import com.dfsek.terra.api.world.palette.holder.PaletteLayerHolder;
import com.dfsek.terra.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.config.loaders.MaterialSetLoader;
import com.dfsek.terra.config.loaders.ProbabilityCollectionLoader;
import com.dfsek.terra.config.loaders.RangeLoader;
import com.dfsek.terra.config.loaders.config.FloraLayerLoader;
import com.dfsek.terra.config.loaders.config.GridSpawnLoader;
import com.dfsek.terra.config.loaders.config.OreConfigLoader;
import com.dfsek.terra.config.loaders.config.OreHolderLoader;
import com.dfsek.terra.config.loaders.config.TreeLayerLoader;
import com.dfsek.terra.config.loaders.config.biome.templates.ExpanderStageTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.mutator.BorderListMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.mutator.BorderMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.mutator.ReplaceListMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.mutator.ReplaceMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.mutator.SmoothMutatorTemplate;
import com.dfsek.terra.config.loaders.config.sampler.NoiseSamplerBuilderLoader;
import com.dfsek.terra.config.loaders.config.sampler.templates.DomainWarpTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.FastNoiseTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.ImageSamplerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.LinearNormalizerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.NormalNormalizerTemplate;
import com.dfsek.terra.config.loaders.palette.PaletteHolderLoader;
import com.dfsek.terra.config.loaders.palette.PaletteLayerLoader;
import com.dfsek.terra.util.MaterialSet;
import com.dfsek.terra.world.population.items.flora.FloraLayer;
import com.dfsek.terra.world.population.items.flora.TerraFlora;
import com.dfsek.terra.world.population.items.ores.Ore;
import com.dfsek.terra.world.population.items.ores.OreConfig;
import com.dfsek.terra.world.population.items.ores.OreHolder;
import com.dfsek.terra.world.population.items.tree.TreeLayer;

public class GenericLoaders implements LoaderRegistrar {
    private final TerraPlugin main;

    public GenericLoaders(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public void register(TypeRegistry registry) {
        registry.registerLoader(ProbabilityCollection.class, new ProbabilityCollectionLoader())
                .registerLoader(Range.class, new RangeLoader())
                .registerLoader(GridSpawn.class, new GridSpawnLoader())
                .registerLoader(PaletteHolder.class, new PaletteHolderLoader())
                .registerLoader(PaletteLayerHolder.class, new PaletteLayerLoader())
                .registerLoader(FloraLayer.class, new FloraLayerLoader())
                .registerLoader(Ore.Type.class, (t, o, l) -> Ore.Type.valueOf(o.toString()))
                .registerLoader(OreConfig.class, new OreConfigLoader())
                .registerLoader(TreeLayer.class, new TreeLayerLoader())
                .registerLoader(MaterialSet.class, new MaterialSetLoader())
                .registerLoader(OreHolder.class, new OreHolderLoader())
                .registerLoader(FastNoiseTemplate.class, FastNoiseTemplate::new)
                .registerLoader(ImageSamplerTemplate.class, ImageSamplerTemplate::new)
                .registerLoader(DomainWarpTemplate.class, DomainWarpTemplate::new)
                .registerLoader(LinearNormalizerTemplate.class, LinearNormalizerTemplate::new)
                .registerLoader(NormalNormalizerTemplate.class, NormalNormalizerTemplate::new)
                .registerLoader(FastNoiseTemplate.class, FastNoiseTemplate::new)
                .registerLoader(NoiseSeeded.class, new NoiseSamplerBuilderLoader())
                .registerLoader(ReplaceMutatorTemplate.class, ReplaceMutatorTemplate::new)
                .registerLoader(ExpanderStageTemplate.class, ExpanderStageTemplate::new)
                .registerLoader(SmoothMutatorTemplate.class, SmoothMutatorTemplate::new)
                .registerLoader(ReplaceListMutatorTemplate.class, ReplaceListMutatorTemplate::new)
                .registerLoader(BorderMutatorTemplate.class, BorderMutatorTemplate::new)
                .registerLoader(BorderListMutatorTemplate.class, BorderListMutatorTemplate::new)
                .registerLoader(ImageSampler.Channel.class, (t, object, cf) -> ImageSampler.Channel.valueOf((String) object))
                .registerLoader(ExpanderStage.Type.class, (t, object, cf) -> ExpanderStage.Type.valueOf((String) object))
                .registerLoader(MutatorStage.Type.class, (t, object, cf) -> MutatorStage.Type.valueOf((String) object))
                .registerLoader(FastNoiseLite.NoiseType.class, (t, object, cf) -> FastNoiseLite.NoiseType.valueOf((String) object))
                .registerLoader(FastNoiseLite.FractalType.class, (t, object, cf) -> FastNoiseLite.FractalType.valueOf((String) object))
                .registerLoader(FastNoiseLite.DomainWarpType.class, (t, object, cf) -> FastNoiseLite.DomainWarpType.valueOf((String) object))
                .registerLoader(FastNoiseLite.RotationType3D.class, (t, object, cf) -> FastNoiseLite.RotationType3D.valueOf((String) object))
                .registerLoader(FastNoiseLite.CellularReturnType.class, (t, object, cf) -> FastNoiseLite.CellularReturnType.valueOf((String) object))
                .registerLoader(FastNoiseLite.CellularDistanceFunction.class, (t, object, cf) -> FastNoiseLite.CellularDistanceFunction.valueOf((String) object))
                .registerLoader(Normalizer.NormalType.class, (t, o, l) -> Normalizer.NormalType.valueOf(o.toString().toUpperCase()))
                .registerLoader(TerraFlora.Search.class, (t, o, l) -> TerraFlora.Search.valueOf(o.toString()))
                .registerLoader(Normalizer.NormalType.class, (t, o, l) -> Normalizer.NormalType.valueOf(o.toString().toUpperCase()));
    }
}
