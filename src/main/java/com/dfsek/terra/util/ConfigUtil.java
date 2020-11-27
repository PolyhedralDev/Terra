package com.dfsek.terra.util;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.biome.palette.PaletteLayer;
import com.dfsek.terra.carving.CarverPalette;
import com.dfsek.terra.config.loaders.BlockDataLoader;
import com.dfsek.terra.config.loaders.GridSpawnLoader;
import com.dfsek.terra.config.loaders.MaterialLoader;
import com.dfsek.terra.config.loaders.PaletteHolderLoader;
import com.dfsek.terra.config.loaders.PaletteLayerLoader;
import com.dfsek.terra.config.loaders.ProbabilityCollectionLoader;
import com.dfsek.terra.config.loaders.RangeLoader;
import com.dfsek.terra.config.loaders.VanillaBiomeLoader;
import com.dfsek.terra.config.loaders.base.CarverPaletteLoader;
import com.dfsek.terra.procgen.GridSpawn;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class ConfigUtil {
    public static List<InputStream> loadFromPath(Path folder) {
        List<InputStream> streams = new ArrayList<>();
        folder.toFile().mkdirs();
        try(Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile).filter(file -> file.toString().toLowerCase().endsWith(".yml")).forEach(file -> {
                try {
                    streams.add(new FileInputStream(file.toFile()));
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        } catch(IOException e) {
            e.printStackTrace();
        }

        return streams;
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
                .registerLoader(Biome.class, new VanillaBiomeLoader())
                .registerLoader(BlockData.class, new BlockDataLoader())
                .registerLoader(Material.class, new MaterialLoader());
    }
}
