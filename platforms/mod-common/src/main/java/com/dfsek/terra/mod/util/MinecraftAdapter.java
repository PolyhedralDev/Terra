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
import net.minecraft.world.HeightLimitView;

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
}
