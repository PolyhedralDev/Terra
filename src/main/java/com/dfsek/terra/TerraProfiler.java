package com.dfsek.terra;

import org.bukkit.World;
import org.polydev.gaea.profiler.DataType;
import org.polydev.gaea.profiler.Measurement;
import org.polydev.gaea.profiler.WorldProfiler;

import java.util.HashMap;
import java.util.Map;

public class TerraProfiler extends WorldProfiler {
    private static final Map<World, TerraProfiler> profilerMap = new HashMap<>();
    public TerraProfiler(World w) {
        super(w);
        this.addMeasurement(new Measurement(2500000, DataType.PERIOD_MILLISECONDS), "TotalChunkGenTime")
                .addMeasurement(new Measurement(2500000, DataType.PERIOD_MILLISECONDS), "ChunkBaseGenTime")
                .addMeasurement(new Measurement(2000000, DataType.PERIOD_MILLISECONDS), "BiomeSetTime")
                .addMeasurement(new Measurement(25000000, DataType.PERIOD_MILLISECONDS), "TreeGenTime")
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "FaunaTime");
        profilerMap.put(w, this);
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
