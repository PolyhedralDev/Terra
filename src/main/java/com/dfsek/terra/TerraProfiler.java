package com.dfsek.terra;

import com.dfsek.terra.generation.TerraChunkGenerator;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.World;
import org.polydev.gaea.profiler.DataType;
import org.polydev.gaea.profiler.Measurement;
import org.polydev.gaea.profiler.WorldProfiler;

import java.util.Map;

public class TerraProfiler extends WorldProfiler {
    private static final Map<World, TerraProfiler> profilerMap = new Object2ObjectOpenHashMap<>();

    public TerraProfiler(World w) {
        super(w);
        this
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "FloraTime")
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "TreeTime")
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "OreTime")
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "CaveTime")
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "StructureTime")
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "ElevationTime")
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "SnowTime");
    }

    public static TerraProfiler fromWorld(World w) {
        if(w.getGenerator() instanceof TerraChunkGenerator) {
            if(profilerMap.containsKey(w)) return profilerMap.get(w);
            TerraProfiler p = new TerraProfiler(w);
            profilerMap.put(w, p);
            return p;
        } else throw new IllegalArgumentException("Attempted to instantiate/fetch Profiler for non-Terra world!");
    }
}
