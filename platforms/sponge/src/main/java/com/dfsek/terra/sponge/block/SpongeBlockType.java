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

package com.dfsek.terra.sponge.block;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Keys;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;


public class SpongeBlockType implements BlockType {
    private final org.spongepowered.api.block.BlockType delegate;
    
    public SpongeBlockType(org.spongepowered.api.block.BlockType delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public org.spongepowered.api.block.BlockType getHandle() {
        return delegate;
    }
    
    @Override
    public BlockState getDefaultState() {
        return new SpongeBlockState(delegate.defaultState());
    }
    
    @Override
    public boolean isSolid() {
        return !delegate.getOrElse(Keys.IS_SOLID, false);
    }
    
    @Override
    public boolean isWater() {
        return delegate.equals(BlockTypes.WATER.get());
    }
}
