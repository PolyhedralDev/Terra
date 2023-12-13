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

package com.dfsek.terra.mod.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.HeightLimitView;

import java.util.random.RandomGenerator;

import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.info.WorldProperties;


public final class MinecraftAdapter {

    public static Vector3 adapt(BlockPos pos) {
        return Vector3.of(pos.getX(), pos.getY(), pos.getZ());
    }

    public static WorldProperties adapt(HeightLimitView height, long seed) {
        return new WorldProperties() {
            @Override
            public long getSeed() {
                return seed;
            }

            @Override
            public int getMaxHeight() {
                return height.getTopY();
            }

            @Override
            public int getMinHeight() {
                return height.getBottomY();
            }

            @Override
            public Object getHandle() {
                return height;
            }
        };
    }

    public static RandomGenerator adapt(Random random) {
        return new RandomGenerator() {
            @Override
            public boolean nextBoolean() {
                return random.nextBoolean();
            }

            @Override
            public float nextFloat() {
                return random.nextFloat();
            }

            @Override
            public double nextDouble() {
                return random.nextDouble();
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
            public double nextGaussian() {
                return random.nextGaussian();
            }

            @Override
            public int nextInt(int origin, int bound) {
                return random.nextBetween(origin, bound);
            }
        };
    }
}
