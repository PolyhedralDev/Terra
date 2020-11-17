package com.dfsek.terra.generation;

import com.dfsek.terra.config.genconfig.noise.NoiseConfig;
import com.dfsek.terra.generation.config.WorldGenerator;
import com.dfsek.terra.math.BlankFunction;
import com.dfsek.terra.util.DataUtil;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;
import org.polydev.gaea.biome.Generator;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.math.Interpolator;
import org.polydev.gaea.world.palette.Palette;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.tokenizer.ParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class UserDefinedGenerator extends Generator {

    private final boolean preventSmooth;
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final Palette<BlockData>[] palettes = new Palette[256];
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final Palette<BlockData>[] slantPalettes = new Palette[256];


    private final String equation;
    private final String elevationEquation;
    private final Map<String, Double> userVariables;
    private final Map<String, NoiseConfig> noiseBuilders;

    private final Map<UUID, WorldGenerator> gens = new HashMap<>();
    private boolean elevationInterpolation;


    public UserDefinedGenerator(String equation, @Nullable String elevateEquation, Map<String, Double> userVariables, Map<Integer, Palette<BlockData>> paletteMap, Map<Integer, Palette<BlockData>> slantPaletteMap, Map<String, NoiseConfig> noiseBuilders, boolean preventSmooth)
            throws ParseException {
        this.equation = equation;
        this.elevationEquation = elevateEquation;
        this.userVariables = userVariables;
        this.noiseBuilders = noiseBuilders;
        this.preventSmooth = preventSmooth;

        Scope s = new Scope();
        Parser p = new Parser();
        for(Map.Entry<String, Double> entry : userVariables.entrySet()) {
            s.getVariable(entry.getKey()).setValue(entry.getValue()); // Define all user variables.
        }
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

    }

    /**
     * Gets the 2D noise at a pair of coordinates using the provided FastNoiseLite instance.
     *
     * @param gen - The FastNoiseLite instance to use.
     * @param x   - The x coordinate.
     * @param z   - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    @Override
    public double getNoise(FastNoiseLite gen, World w, int x, int z) {
        return compute(w).getNoise(x, 0, z);
    }

    /**
     * Gets the 3D noise at a pair of coordinates using the provided FastNoiseLite instance.
     *
     * @param gen - The FastNoiseLite instance to use.
     * @param x   - The x coordinate.
     * @param y   - The y coordinate.
     * @param z   - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    @Override
    public double getNoise(FastNoiseLite gen, World w, int x, int y, int z) {
        return compute(w).getNoise(x, y, z);
    }

    private WorldGenerator compute(World world) {
        return gens.computeIfAbsent(world.getUID(), w -> new WorldGenerator(world, equation, elevationEquation, userVariables, noiseBuilders));
    }

    /**
     * Gets the BlocPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    @Override
    public Palette<BlockData> getPalette(int y) {
        return palettes[y];
    }

    public Palette<BlockData> getSlantPalette(int y) {
        return slantPalettes[y];
    }


    @Override
    public boolean useMinimalInterpolation() {
        return preventSmooth;
    }

    @Override
    public Interpolator.Type getInterpolationType() {
        return Interpolator.Type.LINEAR;
    }

    public ElevationEquation getElevationEquation(World w) {
        return compute(w).getElevationEquation();
    }

    public boolean interpolateElevation() {
        return elevationInterpolation;
    }

    public void setElevationInterpolation(boolean elevationInterpolation) {
        this.elevationInterpolation = elevationInterpolation;
    }
}
