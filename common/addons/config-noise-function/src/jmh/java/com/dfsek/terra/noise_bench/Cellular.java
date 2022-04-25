package com.dfsek.terra.noise_bench;

import com.dfsek.terra.addons.noise.samplers.noise.cellular.CellularSampler;
import com.dfsek.terra.api.noise.NoiseSampler;

import com.dfsek.terra.noise_bench.old.OldCellularSampler;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


public class Cellular {
    private static final long SEED = ThreadLocalRandom.current().nextLong();
    @State(Scope.Benchmark)
    public static class OldCellularParameters {
        public int seed;
    
        public int x;
    
        public int y;
    
        public int z;
        
        public NoiseSampler sampler = new OldCellularSampler();
    
        @Setup(Level.Iteration)
        public void setUp() {
            Random random = new Random(SEED);
            
            seed = random.nextInt();
            x = random.nextInt();
            y = random.nextInt();
            z = random.nextInt();
        }
    }
    
    @State(Scope.Benchmark)
    public static class CellularParameters {
        public int seed;
    
        public int x;
    
        public int y;
    
        public int z;
        
        public NoiseSampler sampler = new CellularSampler();
    
        @Setup(Level.Iteration)
        public void setUp() {
            Random random = new Random(SEED);
        
            seed = random.nextInt();
            x = random.nextInt();
            y = random.nextInt();
            z = random.nextInt();
        }
    }
    
    @Benchmark()
    @Warmup(iterations = 25, time = 200, timeUnit = MILLISECONDS)
    @Measurement(iterations = 15, time = 200, timeUnit = MILLISECONDS)
    @Fork(warmups = 2, value = 3)
    @BenchmarkMode(Mode.Throughput)
    public void old2D(OldCellularParameters parameters, Blackhole blackhole) {
        blackhole.consume(parameters.sampler.noise(parameters.seed, parameters.x, parameters.y));
    }
    
    @Benchmark
    @Warmup(iterations = 25, time = 200, timeUnit = MILLISECONDS)
    @Measurement(iterations = 15, time = 200, timeUnit = MILLISECONDS)
    @Fork(warmups = 2, value = 3)
    @BenchmarkMode(Mode.Throughput)
    public void old3D(OldCellularParameters parameters, Blackhole blackhole) {
        blackhole.consume(parameters.sampler.noise(parameters.seed, parameters.x, parameters.y, parameters.z));
    }
    
    @Benchmark
    @Warmup(iterations = 25, time = 200, timeUnit = MILLISECONDS)
    @Measurement(iterations = 15, time = 200, timeUnit = MILLISECONDS)
    @Fork(warmups = 2, value = 3)
    @BenchmarkMode(Mode.Throughput)
    public void new2D(CellularParameters parameters, Blackhole blackhole) {
        blackhole.consume(parameters.sampler.noise(parameters.seed, parameters.x, parameters.y));
    }
    
    @Benchmark
    @Warmup(iterations = 25, time = 200, timeUnit = MILLISECONDS)
    @Measurement(iterations = 15, time = 200, timeUnit = MILLISECONDS)
    @Fork(warmups = 2, value = 3)
    @BenchmarkMode(Mode.Throughput)
    public void new3D(CellularParameters parameters, Blackhole blackhole) {
        blackhole.consume(parameters.sampler.noise(parameters.seed, parameters.x, parameters.y, parameters.z));
    }
}
