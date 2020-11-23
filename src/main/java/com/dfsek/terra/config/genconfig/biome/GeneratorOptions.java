package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.config.genconfig.noise.NoiseConfig;
import com.dfsek.terra.generation.config.WorldGenerator;
import com.dfsek.terra.math.BlankFunction;
import com.dfsek.terra.util.DataUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;
import org.polydev.gaea.world.palette.Palette;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.tokenizer.ParseException;

import java.util.Map;

public class GeneratorOptions {

    private final Map<Long, WorldGenerator> generators = new Object2ObjectOpenHashMap<>();

    private final boolean preventSmooth;
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final Palette<BlockData>[] palettes = new Palette[256];
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final Palette<BlockData>[] slantPalettes = new Palette[256];


    private final String equation;
    private final String elevationEquation;
    private final Scope userVariables;
    private final Map<String, NoiseConfig> noiseBuilders;

    private final boolean elevationInterpolation;

    public GeneratorOptions(String equation, @Nullable String elevateEquation, Scope userVariables, Map<Integer, Palette<BlockData>> paletteMap, Map<Integer, Palette<BlockData>> slantPaletteMap, Map<String, NoiseConfig> noiseBuilders, boolean preventSmooth, boolean elevationInterpolation)
            throws ParseException {
        this.equation = equation;
        this.elevationEquation = elevateEquation;
        this.userVariables = userVariables;
        this.noiseBuilders = noiseBuilders;
        this.preventSmooth = preventSmooth;

        Scope s = new Scope().withParent(userVariables);
        Parser p = new Parser();
        for(Map.Entry<String, NoiseConfig> e : noiseBuilders.entrySet()) {
            int dimensions = e.getValue().getDimensions();
            if(dimensions == 2 || dimensions == 3) p.registerFunction(e.getKey(), new BlankFunction(dimensions));
        }
        p.parse(equation, s); // Validate equation at config load time to prevent error during world load.
        if(elevateEquation != null) p.parse(elevateEquation, s);


        for(int y = 0; y < 256; y++) {
            Palette<BlockData> d = DataUtil.BLANK_PALETTE;
            for(Map.Entry<Integer, Palette<BlockData>> e : paletteMap.entrySet()) {
                if(e.getKey() >= y) {
                    d = e.getValue();
                    break;
                }
            }
            palettes[y] = d;
            Palette<BlockData> slantPalette = null;
            for(Map.Entry<Integer, Palette<BlockData>> e : slantPaletteMap.entrySet()) {
                if(e.getKey() >= y) {
                    slantPalette = e.getValue();
                    break;
                }
            }
            slantPalettes[y] = slantPalette;
        }
        this.elevationInterpolation = elevationInterpolation;
    }

    public WorldGenerator getGenerator(long seed) {
        return generators.computeIfAbsent(seed, s -> new WorldGenerator(seed, equation, elevationEquation, userVariables, noiseBuilders, palettes, slantPalettes, preventSmooth)
                .setElevationInterpolation(elevationInterpolation));
    }
}
