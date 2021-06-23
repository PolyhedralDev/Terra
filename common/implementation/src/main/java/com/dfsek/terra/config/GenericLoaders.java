package com.dfsek.terra.config;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.LoaderRegistrar;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.math.GridSpawn;
import com.dfsek.terra.api.math.range.ConstantRange;
import com.dfsek.terra.noise.samplers.ImageSampler;
import com.dfsek.terra.noise.samplers.noise.CellularSampler;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.util.collections.ProbabilityCollectionImpl;
import com.dfsek.terra.api.util.seeded.SourceSeeded;
import com.dfsek.terra.api.util.seeded.StageSeeded;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeSource;
import com.dfsek.terra.api.world.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.api.world.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.biome.provider.ImageBiomeProvider;
import com.dfsek.terra.api.world.palette.holder.PaletteHolder;
import com.dfsek.terra.api.world.palette.holder.PaletteLayerHolder;
import com.dfsek.terra.api.world.palette.slant.SlantHolder;
import com.dfsek.terra.carving.CarverPalette;
import com.dfsek.terra.config.loaders.LinkedHashMapLoader;
import com.dfsek.terra.config.loaders.MaterialSetLoader;
import com.dfsek.terra.config.loaders.ProbabilityCollectionLoader;
import com.dfsek.terra.config.loaders.RangeLoader;
import com.dfsek.terra.config.loaders.config.FloraLayerLoader;
import com.dfsek.terra.config.loaders.config.GridSpawnLoader;
import com.dfsek.terra.config.loaders.config.OreConfigLoader;
import com.dfsek.terra.config.loaders.config.OreHolderLoader;
import com.dfsek.terra.config.loaders.config.TreeLayerLoader;
import com.dfsek.terra.config.loaders.config.biome.BiomeProviderBuilderLoader;
import com.dfsek.terra.config.loaders.config.biome.SourceBuilderLoader;
import com.dfsek.terra.config.loaders.config.biome.StageBuilderLoader;
import com.dfsek.terra.config.loaders.config.biome.templates.source.NoiseSourceTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.expander.ExpanderStageTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator.BorderListMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator.BorderMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator.ReplaceListMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator.ReplaceMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator.SmoothMutatorTemplate;
import com.dfsek.terra.config.loaders.config.function.FunctionTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.DomainWarpTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.ImageSamplerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.ClampNormalizerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.LinearNormalizerTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.normalizer.NormalNormalizerTemplate;
import com.dfsek.terra.config.loaders.palette.CarverPaletteLoader;
import com.dfsek.terra.config.loaders.palette.PaletteHolderLoader;
import com.dfsek.terra.config.loaders.palette.PaletteLayerLoader;
import com.dfsek.terra.config.loaders.palette.slant.SlantHolderLoader;
import com.dfsek.terra.world.population.items.flora.FloraLayer;
import com.dfsek.terra.world.population.items.flora.TerraFlora;
import com.dfsek.terra.world.population.items.ores.Ore;
import com.dfsek.terra.world.population.items.ores.OreConfig;
import com.dfsek.terra.world.population.items.ores.OreHolder;
import com.dfsek.terra.world.population.items.tree.TreeLayer;

import java.util.LinkedHashMap;

public class GenericLoaders implements LoaderRegistrar {
    private final TerraPlugin main;

    public GenericLoaders(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public void register(TypeRegistry registry) {
        registry.registerLoader(ProbabilityCollectionImpl.class, new ProbabilityCollectionLoader())
                .registerLoader(ConstantRange.class, new RangeLoader())
                .registerLoader(GridSpawn.class, new GridSpawnLoader())
                .registerLoader(PaletteHolder.class, new PaletteHolderLoader())
                .registerLoader(PaletteLayerHolder.class, new PaletteLayerLoader())
                .registerLoader(SlantHolder.class, new SlantHolderLoader())
                .registerLoader(FloraLayer.class, new FloraLayerLoader())
                .registerLoader(Ore.Type.class, (t, o, l) -> Ore.Type.valueOf(o.toString()))
                .registerLoader(OreConfig.class, new OreConfigLoader())
                .registerLoader(TreeLayer.class, new TreeLayerLoader())
                .registerLoader(MaterialSet.class, new MaterialSetLoader())
                .registerLoader(OreHolder.class, new OreHolderLoader())
                .registerLoader(ImageSamplerTemplate.class, ImageSamplerTemplate::new)
                .registerLoader(DomainWarpTemplate.class, DomainWarpTemplate::new)
                .registerLoader(LinearNormalizerTemplate.class, LinearNormalizerTemplate::new)
                .registerLoader(NormalNormalizerTemplate.class, NormalNormalizerTemplate::new)
                .registerLoader(ClampNormalizerTemplate.class, ClampNormalizerTemplate::new)
                .registerLoader(ReplaceMutatorTemplate.class, ReplaceMutatorTemplate::new)
                .registerLoader(ExpanderStageTemplate.class, ExpanderStageTemplate::new)
                .registerLoader(SmoothMutatorTemplate.class, SmoothMutatorTemplate::new)
                .registerLoader(ReplaceListMutatorTemplate.class, ReplaceListMutatorTemplate::new)
                .registerLoader(BorderMutatorTemplate.class, BorderMutatorTemplate::new)
                .registerLoader(BorderListMutatorTemplate.class, BorderListMutatorTemplate::new)
                .registerLoader(NoiseSourceTemplate.class, NoiseSourceTemplate::new)
                .registerLoader(FunctionTemplate.class, FunctionTemplate::new)
                .registerLoader(LinkedHashMap.class, new LinkedHashMapLoader())
                .registerLoader(CarverPalette.class, new CarverPaletteLoader())
                .registerLoader(SourceSeeded.class, new SourceBuilderLoader())
                .registerLoader(StageSeeded.class, new StageBuilderLoader())
                .registerLoader(BiomeProvider.BiomeProviderBuilder.class, new BiomeProviderBuilderLoader())
                .registerLoader(ImageSampler.Channel.class, (t, object, cf) -> ImageSampler.Channel.valueOf((String) object))
                .registerLoader(BiomeProvider.Type.class, (t, object, cf) -> BiomeProvider.Type.valueOf((String) object))
                .registerLoader(BiomeSource.Type.class, (t, object, cf) -> BiomeSource.Type.valueOf((String) object))
                .registerLoader(ImageBiomeProvider.Align.class, (t, object, cf) -> ImageBiomeProvider.Align.valueOf((String) object))
                .registerLoader(ExpanderStage.Type.class, (t, object, cf) -> ExpanderStage.Type.valueOf((String) object))
                .registerLoader(MutatorStage.Type.class, (t, object, cf) -> MutatorStage.Type.valueOf((String) object))
                .registerLoader(CellularSampler.ReturnType.class, (t, object, cf) -> CellularSampler.ReturnType.valueOf((String) object))
                .registerLoader(CellularSampler.DistanceFunction.class, (t, object, cf) -> CellularSampler.DistanceFunction.valueOf((String) object))
                .registerLoader(TerraFlora.Search.class, (t, o, l) -> TerraFlora.Search.valueOf(o.toString()));

        if(main != null) {
            registry.registerLoader(TerraAddon.class, main.getAddons())
                    .registerLoader(BlockType.class, (t, object, cf) -> main.getWorldHandle().createBlockData((String) object).getBlockType());
        }
    }
}
