package com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 2, time = 5)
public class Interpolator3Benchmark {
    private final Interpolator3 interpolator = new Interpolator3(0, 1, 0, 1, 0, 1, 0, 1);

    @Benchmark
    public void benchmarkInterpolator3(Blackhole blackhole) {
        blackhole.consume(interpolator.trilerp(0.5, 0.75, 0.5));
    }
}
