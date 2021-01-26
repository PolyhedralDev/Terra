package com.dfsek.terra.config;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.LoaderRegistrar;
import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.math.GridSpawn;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.samplers.Normalizer;
import com.dfsek.terra.api.world.palette.holder.PaletteHolder;
import com.dfsek.terra.api.world.palette.holder.PaletteLayerHolder;
import com.dfsek.terra.config.loaders.MaterialSetLoader;
import com.dfsek.terra.config.loaders.ProbabilityCollectionLoader;
import com.dfsek.terra.config.loaders.RangeLoader;
import com.dfsek.terra.config.loaders.config.FloraLayerLoader;
import com.dfsek.terra.config.loaders.config.GridSpawnLoader;
import com.dfsek.terra.config.loaders.config.NoiseBuilderLoader;
import com.dfsek.terra.config.loaders.config.OreConfigLoader;
import com.dfsek.terra.config.loaders.config.OreHolderLoader;
import com.dfsek.terra.config.loaders.config.TreeLayerLoader;
import com.dfsek.terra.config.loaders.palette.PaletteHolderLoader;
import com.dfsek.terra.config.loaders.palette.PaletteLayerLoader;
import com.dfsek.terra.util.MaterialSet;
import com.dfsek.terra.world.generation.config.NoiseBuilder;
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
                .registerLoader(NoiseBuilder.class, new NoiseBuilderLoader())
                .registerLoader(TreeLayer.class, new TreeLayerLoader())
                .registerLoader(MaterialSet.class, new MaterialSetLoader())
                .registerLoader(OreHolder.class, new OreHolderLoader())
                .registerLoader(TerraFlora.Search.class, (t, o, l) -> TerraFlora.Search.valueOf(o.toString()))
                .registerLoader(Normalizer.NormalType.class, (t, o, l) -> Normalizer.NormalType.valueOf(o.toString().toUpperCase()));
    }
}
