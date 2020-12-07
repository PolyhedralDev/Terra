package com.dfsek.terra.util;

import com.dfsek.tectonic.loading.TypeRegistry;
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
import com.dfsek.terra.config.loaders.config.StructureFeatureLoader;
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
import com.dfsek.terra.structure.features.Feature;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.polydev.gaea.GaeaPlugin;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;

public final class ConfigUtil {
    private static GaeaPlugin main;

    public static void setMain(GaeaPlugin main) {
        ConfigUtil.main = main;
    }

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
                .registerLoader(Biome.class, (t, o, l) -> Biome.valueOf((String) o))
                .registerLoader(BlockData.class, (t, o, l) -> Bukkit.createBlockData((String) o))
                .registerLoader(Material.class, (t, o, l) -> Material.matchMaterial((String) o))
                .registerLoader(FloraLayer.class, new FloraLayerLoader())
                .registerLoader(Ore.Type.class, (t, o, l) -> Ore.Type.valueOf((String) o))
                .registerLoader(OreConfig.class, new OreConfigLoader())
                .registerLoader(NoiseBuilder.class, new NoiseBuilderLoader())
                .registerLoader(TreeLayer.class, new TreeLayerLoader(main))
                .registerLoader(MaterialSet.class, new MaterialSetLoader())
                .registerLoader(OreHolder.class, new OreHolderLoader())
                .registerLoader(Feature.class, new StructureFeatureLoader())
                .registerLoader(ImageLoader.class, new ImageLoaderLoader())
                .registerLoader(EntityType.class, (t, o, l) -> EntityType.valueOf((String) o))
                .registerLoader(TerraBiomeGrid.Type.class, (t, o, l) -> TerraBiomeGrid.Type.valueOf((String) o))
                .registerLoader(StructureTypeEnum.class, (t, o, l) -> StructureTypeEnum.valueOf((String) o))
                .registerLoader(ImageLoader.Channel.class, (t, o, l) -> ImageLoader.Channel.valueOf((String) o))
                .registerLoader(ImageLoader.Align.class, (t, o, l) -> ImageLoader.Align.valueOf((String) o))
                .registerLoader(TerraFlora.Search.class, (t, o, l) -> TerraFlora.Search.valueOf((String) o));
    }
}
