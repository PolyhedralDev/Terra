package com.dfsek.terra.api.math.noise.normalizer;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import net.jafama.FastMath;

/**
 * Normalizer to redistribute normally distributed data to a continuous distribution via an automatically generated lookup table.
 */
public class NormalNormalizer extends Normalizer {

    private final double[] lookup;

    public NormalNormalizer(NoiseSampler sampler, int buckets, double mean, double standardDeviation) {
        super(sampler);
        this.lookup = new double[buckets];

        for(int i = 0; i < buckets; i++) {
            lookup[i] = MathUtil.normalInverse((double) i / buckets, mean, standardDeviation);
        }
    }

    @Override
    public double normalize(double in) {
        int start = 0;
        int end = lookup.length - 1;
        while(start + 1 < end) {
            int mid = start + (end - start) / 2;
            if(lookup[mid] <= in) {
                start = mid;
            } else {
                end = mid;
            }
        }
        double left = FastMath.abs(lookup[start] - in);
        double right = FastMath.abs(lookup[end] - in);

        double fin;
        if(left <= right) {
            fin = (double) start / (lookup.length);
        } else fin = (double) end / (lookup.length);

        return (fin - 0.5) * 2;
    }
}
