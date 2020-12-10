package com.dfsek.terra.api.gaea.profiler;

import com.dfsek.terra.api.gaea.math.MathUtil;
import com.dfsek.terra.api.gaea.util.GlueList;
import net.jafama.FastMath;
import org.bukkit.Bukkit;

import java.math.BigInteger;
import java.util.List;

/**
 * Class to record and hold all data for a single type of measurement performed by the profiler.
 */
public class Measurement {
    private final List<Long> measurements;
    private final long desirable;
    private final DataType type;
    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;

    /**
     * Constructs a new Measurement with a desired value and DataType.
     *
     * @param desirable The desired value of the measurement.
     * @param type      The type of data the measurement is holding.
     */
    public Measurement(long desirable, DataType type) {
        this.desirable = desirable;
        this.type = type;
        measurements = new GlueList<>();
    }

    public void record(long value) {
        max = FastMath.max(value, max);
        min = FastMath.min(value, min);
        if(value / 1000000 > 5000) Bukkit.getLogger().warning("Measurement took " + type.getFormatted(value));
        measurements.add(value);
    }

    public int size() {
        return measurements.size();
    }

    public ProfileFuture beginMeasurement() {
        ProfileFuture future = new ProfileFuture();
        long current = System.nanoTime();
        future.thenRun(() -> record(System.nanoTime() - current));
        return future;
    }

    public void reset() {
        min = Long.MAX_VALUE;
        max = Long.MIN_VALUE;
        measurements.clear();
    }

    public DataHolder getDataHolder() {
        return new DataHolder(type, desirable, 0.25);
    }

    public long getMin() {
        if(min == Long.MAX_VALUE) return 0;
        return min;
    }

    public long getMax() {
        if(max == Long.MIN_VALUE) return 0;
        return max;
    }

    public long average() {
        BigInteger running = new BigInteger("0");
        List<Long> mTemp = new GlueList<>(measurements);
        for(Long l : mTemp) {
            running = running.add(BigInteger.valueOf(l));
        }
        if(measurements.size() == 0) return 0;
        return running.divide(BigInteger.valueOf(measurements.size())).longValue();
    }

    public double getStdDev() {
        List<Long> mTemp = new GlueList<>(measurements);
        double[] vals = new double[mTemp.size()];
        for(int i = 0; i < mTemp.size(); i++) {
            vals[i] = mTemp.get(i);
        }
        return MathUtil.standardDeviation(vals);
    }

    public int entries() {
        return measurements.size();
    }

}
