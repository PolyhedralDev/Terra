package com.dfsek.terra;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.profiler.DataType;
import com.dfsek.terra.api.profiler.Measurement;
import com.dfsek.terra.api.profiler.WorldProfiler;

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
