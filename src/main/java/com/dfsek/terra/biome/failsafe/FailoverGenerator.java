package com.dfsek.terra.biome.failsafe;

import com.dfsek.terra.generation.UserDefinedGenerator;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.parsii.eval.Variable;
import org.polydev.gaea.math.parsii.tokenizer.ParseException;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public final class FailoverGenerator extends UserDefinedGenerator {
    private static final TreeMap<Integer, Palette<BlockData>> palette = new TreeMap<>();
    static {
        palette.put(255, new RandomPalette<BlockData>(new Random(2403)).add(Material.STONE.createBlockData(), 1));
    }
    public FailoverGenerator() throws ParseException {
        super("0", Collections.emptyList(), palette);
    }
}
