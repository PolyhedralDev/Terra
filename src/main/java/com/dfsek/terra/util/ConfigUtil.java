package com.dfsek.terra.util;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.biome.palette.PaletteLayer;
import com.dfsek.terra.carving.CarverPalette;
import com.dfsek.terra.config.loaders.BlockDataLoader;
import com.dfsek.terra.config.loaders.FloraLayerLoader;
import com.dfsek.terra.config.loaders.FloraSearchLoader;
import com.dfsek.terra.config.loaders.GridSpawnLoader;
import com.dfsek.terra.config.loaders.MaterialLoader;
import com.dfsek.terra.config.loaders.MaterialSetLoader;
import com.dfsek.terra.config.loaders.NoiseBuilderLoader;
import com.dfsek.terra.config.loaders.OreConfigLoader;
import com.dfsek.terra.config.loaders.OreTypeLoader;
import com.dfsek.terra.config.loaders.PaletteHolderLoader;
import com.dfsek.terra.config.loaders.PaletteLayerLoader;
import com.dfsek.terra.config.loaders.ProbabilityCollectionLoader;
import com.dfsek.terra.config.loaders.RangeLoader;
import com.dfsek.terra.config.loaders.TreeLayerLoader;
import com.dfsek.terra.config.loaders.VanillaBiomeLoader;
import com.dfsek.terra.config.loaders.base.CarverPaletteLoader;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.generation.items.flora.FloraLayer;
import com.dfsek.terra.generation.items.flora.TerraFlora;
import com.dfsek.terra.generation.items.ores.Ore;
import com.dfsek.terra.generation.items.ores.OreConfig;
import com.dfsek.terra.generation.items.tree.TreeLayer;
import com.dfsek.terra.procgen.GridSpawn;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;

public final class ConfigUtil {
    /**
     * Register all Terra loaders to a {@link TypeRegistry}.
     *
     * @param registry Registry.
     */
    public static void registerAllLoaders(TypeRegistry registry) {
        registry.registerLoader(ProbabilityCollection.class, new ProbabilityCollectionLoader())
                .registerLoader(Range.class, new RangeLoader())
                .registerLoader(CarverPalette.class, new CarverPaletteLoader())
                .registerLoader(GridSpawn.class, new GridSpawnLoader())
                .registerLoader(PaletteHolder.class, new PaletteHolderLoader())
                .registerLoader(PaletteLayer.class, new PaletteLayerLoader())
                .registerLoader(Biome.class, new VanillaBiomeLoader())
                .registerLoader(BlockData.class, new BlockDataLoader())
                .registerLoader(Material.class, new MaterialLoader())
                .registerLoader(FloraLayer.class, new FloraLayerLoader())
                .registerLoader(Ore.Type.class, new OreTypeLoader())
                .registerLoader(OreConfig.class, new OreConfigLoader())
                .registerLoader(NoiseBuilder.class, new NoiseBuilderLoader())
                .registerLoader(TreeLayer.class, new TreeLayerLoader())
                .registerLoader(MaterialSet.class, new MaterialSetLoader())
                .registerLoader(TerraFlora.Search.class, new FloraSearchLoader());
    }
}
