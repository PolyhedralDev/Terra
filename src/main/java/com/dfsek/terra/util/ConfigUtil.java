package com.dfsek.terra.util;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.biome.palette.PaletteLayer;
import com.dfsek.terra.carving.CarverPalette;
import com.dfsek.terra.config.loaders.MaterialSetLoader;
import com.dfsek.terra.config.loaders.ProbabilityCollectionLoader;
import com.dfsek.terra.config.loaders.RangeLoader;
import com.dfsek.terra.config.loaders.config.FloraLayerLoader;
import com.dfsek.terra.config.loaders.config.GridSpawnLoader;
import com.dfsek.terra.config.loaders.config.NoiseBuilderLoader;
import com.dfsek.terra.config.loaders.config.OreConfigLoader;
import com.dfsek.terra.config.loaders.config.TreeLayerLoader;
import com.dfsek.terra.config.loaders.palette.CarverPaletteLoader;
import com.dfsek.terra.config.loaders.palette.PaletteHolderLoader;
import com.dfsek.terra.config.loaders.palette.PaletteLayerLoader;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.generation.items.flora.FloraLayer;
import com.dfsek.terra.generation.items.flora.TerraFlora;
import com.dfsek.terra.generation.items.ores.Ore;
import com.dfsek.terra.generation.items.ores.OreConfig;
import com.dfsek.terra.generation.items.tree.TreeLayer;
import com.dfsek.terra.procgen.GridSpawn;
import org.bukkit.Bukkit;
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
                .registerLoader(Biome.class, (t, o, l) -> Biome.valueOf((String) o))
                .registerLoader(BlockData.class, (t, o, l) -> Bukkit.createBlockData((String) o))
                .registerLoader(Material.class, (t, o, l) -> Material.matchMaterial((String) o))
                .registerLoader(FloraLayer.class, new FloraLayerLoader())
                .registerLoader(Ore.Type.class, (t, o, l) -> Ore.Type.valueOf((String) o))
                .registerLoader(OreConfig.class, new OreConfigLoader())
                .registerLoader(NoiseBuilder.class, new NoiseBuilderLoader())
                .registerLoader(TreeLayer.class, new TreeLayerLoader())
                .registerLoader(MaterialSet.class, new MaterialSetLoader())
                .registerLoader(TerraFlora.Search.class, (t, o, l) -> TerraFlora.Search.valueOf((String) o));
    }
}
