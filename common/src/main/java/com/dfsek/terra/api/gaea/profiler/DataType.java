package com.dfsek.terra.api.gaea.profiler;

import net.jafama.FastMath;

public enum DataType {
    PERIOD_MILLISECONDS(Desire.LOW, 1000000, "ms"), PERIOD_NANOSECONDS(Desire.LOW, 1, "ns");
    private final Desire desire;
    private final long divisor;
    private final String unit;

    DataType(Desire d, long divisor, String unit) {
        this.desire = d;
        this.divisor = divisor;
        this.unit = unit;
    }

    public String getFormatted(long value) {
        return (double) FastMath.round(((double) value / divisor) * 100D) / 100D + unit;
    }

    public Desire getDesire() {
        return desire;
    }
}
