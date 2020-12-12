package com.dfsek.terra.api;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.gaea.math.ProbabilityCollection;
import com.dfsek.terra.api.gaea.math.Range;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.biome.palette.PaletteLayer;
import com.dfsek.terra.carving.CarverPalette;
import com.dfsek.terra.config.loaders.ImageLoaderLoader;
import com.dfsek.terra.config.loaders.MaterialSetLoader;
import com.dfsek.terra.config.loaders.ProbabilityCollectionLoader;
import com.dfsek.terra.config.loaders.RangeLoader;
import com.dfsek.terra.config.loaders.config.FloraLayerLoader;
import com.dfsek.terra.config.loaders.config.GridSpawnLoader;
import com.dfsek.terra.config.loaders.config.NoiseBuilderLoader;
import com.dfsek.terra.config.loaders.config.OreConfigLoader;
import com.dfsek.terra.config.loaders.config.OreHolderLoader;
import com.dfsek.terra.config.loaders.config.TreeLayerLoader;
import com.dfsek.terra.config.loaders.palette.CarverPaletteLoader;
import com.dfsek.terra.config.loaders.palette.PaletteHolderLoader;
import com.dfsek.terra.config.loaders.palette.PaletteLayerLoader;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.generation.items.flora.FloraLayer;
import com.dfsek.terra.generation.items.flora.TerraFlora;
import com.dfsek.terra.generation.items.ores.Ore;
import com.dfsek.terra.generation.items.ores.OreConfig;
import com.dfsek.terra.generation.items.ores.OreHolder;
import com.dfsek.terra.generation.items.tree.TreeLayer;
import com.dfsek.terra.image.ImageLoader;
import com.dfsek.terra.procgen.GridSpawn;
import com.dfsek.terra.util.MaterialSet;

public class GenericLoaders implements LoaderRegistrar {
    private final TerraPlugin main;

    public GenericLoaders(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public void register(TypeRegistry registry) {
        registry.registerLoader(ProbabilityCollection.class, new ProbabilityCollectionLoader())
                .registerLoader(Range.class, new RangeLoader())
                .registerLoader(CarverPalette.class, new CarverPaletteLoader())
                .registerLoader(GridSpawn.class, new GridSpawnLoader())
                .registerLoader(PaletteHolder.class, new PaletteHolderLoader())
                .registerLoader(PaletteLayer.class, new PaletteLayerLoader())
                .registerLoader(FloraLayer.class, new FloraLayerLoader())
                .registerLoader(Ore.Type.class, (t, o, l) -> Ore.Type.valueOf((String) o))
                .registerLoader(OreConfig.class, new OreConfigLoader())
                .registerLoader(NoiseBuilder.class, new NoiseBuilderLoader())
                .registerLoader(TreeLayer.class, new TreeLayerLoader(main))
                .registerLoader(MaterialSet.class, new MaterialSetLoader())
                .registerLoader(OreHolder.class, new OreHolderLoader())
                .registerLoader(ImageLoader.class, new ImageLoaderLoader())
                .registerLoader(TerraBiomeGrid.Type.class, (t, o, l) -> TerraBiomeGrid.Type.valueOf((String) o))
                .registerLoader(ImageLoader.Channel.class, (t, o, l) -> ImageLoader.Channel.valueOf((String) o))
                .registerLoader(ImageLoader.Align.class, (t, o, l) -> ImageLoader.Align.valueOf((String) o))
                .registerLoader(TerraFlora.Search.class, (t, o, l) -> TerraFlora.Search.valueOf((String) o));
    }
}
