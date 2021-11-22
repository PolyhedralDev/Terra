/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.util;


import org.apache.commons.rng.core.source64.XoRoShiRo128PlusPlus;

import java.io.Serial;
import java.util.Random;
import java.util.SplittableRandom;


public class FastRandom extends Random {
    
    @Serial
    private static final long serialVersionUID = 4571946470190183260L;
    private XoRoShiRo128PlusPlus random;
    
    public FastRandom() {
        super();
        SplittableRandom randomseed = new SplittableRandom();
        this.random = new XoRoShiRo128PlusPlus(randomseed.nextLong(), randomseed.nextLong());
    }
    
    public FastRandom(long seed) {
        super(seed);
        SplittableRandom randomseed = new SplittableRandom(seed);
        this.random = new XoRoShiRo128PlusPlus(randomseed.nextLong(), randomseed.nextLong());
    }
    
    @Override
    public synchronized void setSeed(long seed) {
        SplittableRandom randomseed = new SplittableRandom(seed);
        this.random = new XoRoShiRo128PlusPlus(randomseed.nextLong(), randomseed.nextLong());
    }
    
    @Override
    public void nextBytes(byte[] bytes) {
        random.nextBytes(bytes);
    }
    
    @Override
    public int nextInt() {
        return random.nextInt();
    }
    
    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
    
    @Override
    public long nextLong() {
        return random.nextLong();
    }
    
    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }
    
    @Override
    public float nextFloat() {
        return (float) random.nextDouble();
    }
    
    @Override
    public double nextDouble() {
        return random.nextDouble();
    }
}