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

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;


public class SpongeBlockState implements BlockState {
    private final org.spongepowered.api.block.BlockState delegate;
    
    public SpongeBlockState(org.spongepowered.api.block.BlockState delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public org.spongepowered.api.block.BlockState getHandle() {
        return delegate;
    }
    
    @Override
    public boolean matches(BlockState other) {
        return delegate.type().equals(((SpongeBlockType) other.getBlockType()).getHandle());
    }
    
    @Override
    public <T extends Comparable<T>> boolean has(Property<T> property) {
        return false;
    }
    
    @Override
    public <T extends Comparable<T>> T get(Property<T> property) {
        return null;
    }
    
    @Override
    public <T extends Comparable<T>> BlockState set(Property<T> property, T value) {
        return this;
    }
    
    @Override
    public BlockType getBlockType() {
        return new SpongeBlockType(delegate.type());
    }
    
    @Override
    public String getAsString(boolean verbose) {
        return delegate.toString();
    }
    
    @Override
    public boolean isAir() {
        return delegate.type().equals(BlockTypes.AIR.get());
    }
    
    @Override
    public BlockState clone() {
        return this;
    }
}
