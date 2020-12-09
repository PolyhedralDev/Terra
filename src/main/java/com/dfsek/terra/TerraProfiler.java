package com.dfsek.terra;

import org.bukkit.World;
import org.polydev.gaea.profiler.DataType;
import org.polydev.gaea.profiler.Measurement;
import org.polydev.gaea.profiler.WorldProfiler;

public class TerraProfiler extends WorldProfiler {
    public TerraProfiler(World w) {
        super(w);
        this
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "FloraTime")
                .addMeasurement(new Measurement(10000000, DataType.PERIOD_MILLISECONDS), "TreeTime")
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "OreTime")
                .addMeasurement(new Measurement(5000000, DataType.PERIOD_MILLISECONDS), "CaveTime")
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "StructureTime")
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "ElevationTime");
    }
}
