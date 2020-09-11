package com.dfsek.terra.biome;

import com.dfsek.terra.config.BiomeConfig;
import org.bukkit.block.Biome;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Fauna;

import java.util.Map;

public class UserDefinedDecorator extends Decorator {

    private final ProbabilityCollection<Fauna> fauna = new ProbabilityCollection<>();
    private int faunaChance;

    public UserDefinedDecorator(BiomeConfig config) {
        if(config.contains("fauna")) {
            for(Map.Entry<String, Object> e : config.getConfigurationSection("fauna").getValues(false).entrySet()) {
                fauna.add(Fauna.valueOf(e.getKey()), (Integer) e.getValue());
            }
        }
        faunaChance = config.getInt("fauna-chance", 0);
    }

    @Override
    public ProbabilityCollection<Tree> getTrees() {
        return null;
    }

    @Override
    public int getTreeDensity() {
        return 0;
    }

    @Override
    public boolean overrideStructureChance() {
        return false;
    }

    @Override
    public boolean shouldGenerateSnow() {
        return false;
    }

    @Override
    public Biome getVanillaBiome() {
        return null;
    }

    @Override
    public ProbabilityCollection<Fauna> getFauna() {
        return fauna;
    }

    @Override
    public int getFaunaChance() {
        return faunaChance;
    }
}
