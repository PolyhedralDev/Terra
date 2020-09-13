package com.dfsek.terra.biome;

import com.dfsek.terra.config.BiomeConfig;
import com.dfsek.terra.config.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.parsii.eval.Parser;
import org.polydev.gaea.math.parsii.eval.Scope;
import org.polydev.gaea.math.parsii.tokenizer.ParseException;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.BlockPalette;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class UserDefinedBiome implements Biome {
    private final UserDefinedGenerator gen;
    private final BiomeConfig config;
    private final UserDefinedDecorator decorator;
    public UserDefinedBiome(BiomeConfig config) throws ParseException {
        this.config = config;
        TreeMap<Integer, BlockPalette> paletteMap = new TreeMap<>();
        for(Map<?, ?> e : config.getMapList("palette")) {
            for(Map.Entry<?, ?> entry : e.entrySet()) {
                try {
                    if(((String) entry.getKey()).startsWith("BLOCK:")) {
                        try {
                            paletteMap.put((Integer) entry.getValue(), new BlockPalette().addBlockData(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData((String) entry.getKey()), 1), 1));
                        } catch(IllegalArgumentException ex) {
                            Bukkit.getLogger().severe("SEVERE configuration error for BlockPalettes in biome" + config.getFriendlyName() + ", ID: " + config.getBiomeID() + ". BlockData " + entry.getKey() + " is invalid!");
                        }
                    }
                    else paletteMap.put((Integer) entry.getValue(), ConfigUtil.getPalette((String) entry.getKey()).getPalette());
                } catch(ClassCastException ex) {
                    Bukkit.getLogger().severe("SEVERE configuration error for BlockPalettes in biome" + config.getFriendlyName() + ", ID: " + config.getBiomeID());
                }
            }
        }

        this.decorator = new UserDefinedDecorator(config);

        gen = new UserDefinedGenerator(Objects.requireNonNull(config.getString("noise-equation")), Collections.emptyList(), paletteMap);
    }

    public BiomeConfig getConfig() {
        return config;
    }

    /**
     * Gets the Vanilla biome to represent the custom biome.
     *
     * @return Biome - The Vanilla biome.
     */
    @Override
    public org.bukkit.block.Biome getVanillaBiome() {
        return config.getVanillaBiome();
    }

    /**
     * Gets the BiomeTerrain instance used to generate the biome.
     *
     * @return BiomeTerrain - The terrain generation instance.
     */
    @Override
    public BiomeTerrain getGenerator() {
        return gen;
    }

    /**
     * Gets a list of Structure Features to be applied to all structures in the biome.
     *
     * @return List&lt;Feature&gt; - The list of Features.
     */
    @Override
    public List<Feature> getStructureFeatures() {
        return null;
    }

    /**
     * Returns the Decorator instance containing information about the population in the biome.
     *
     * @return Decorator - the Decorator instance.
     */
    @Override
    public Decorator getDecorator() {
        return decorator;
    }
}
