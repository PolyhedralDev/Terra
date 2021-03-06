package com.dfsek.terra;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Unused.
 * <p>
 * This was the original class that did shit. It no longer does shit.
 */
public class RegionGenerator {
    public static void main(String[] args) throws IOException {
        long seed;
        if(args.length == 1) seed = Long.parseLong(args[0]);
        else seed = ThreadLocalRandom.current().nextLong();

//        StandalonePlugin plugin = new StandalonePlugin(configBuilder.build());
//        Generator generator = new Generator(seed, plugin);

//        generator.generate();
    }
}
