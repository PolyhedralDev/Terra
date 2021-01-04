package com.dfsek.terra;

import com.dfsek.terra.region.Generator;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class RegionGenerator {
    public static void main(String[] args) throws IOException, InterruptedException {
        long seed;
        if(args.length == 1) seed = Long.parseLong(args[0]);
        else seed = ThreadLocalRandom.current().nextLong();

        StandalonePlugin plugin = new StandalonePlugin();
        Generator generator = new Generator(seed, plugin);

        generator.generate();
    }
}
