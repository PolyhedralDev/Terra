package com.dfsek.terra.addons.chunkgenerator.config.noise;

import com.dfsek.seismic.type.sampler.Sampler;


public class ThreadLocalNoiseHolder {
    private final ThreadLocal<Holder> holder = ThreadLocal.withInitial(Holder::new);

    public double getNoise(Sampler sampler, int x, int y, int z, long seed) {
        Holder holder = this.holder.get();

        if(holder.init && holder.y == y && holder.z == z && holder.x == x && holder.seed == seed) {
            return holder.noise;
        }

        double noise = sampler.getSample(seed, x, y, z);
        holder.noise = noise;
        holder.x = x;
        holder.y = y;
        holder.z = z;
        holder.seed = seed;
        holder.init = true;
        return noise;
    }

    private static final class Holder {
        int x, y, z;
        boolean init = false;
        long seed;
        double noise;
    }
}
