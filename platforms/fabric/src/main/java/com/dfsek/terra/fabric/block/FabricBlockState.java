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

package com.dfsek.terra.fabric.block;

import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.RailShape;
import net.minecraft.block.enums.WallShape;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;
import com.dfsek.terra.api.block.state.properties.base.Properties;
import com.dfsek.terra.api.util.generic.Construct;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.fabric.mixin.access.StateAccessor;
import com.dfsek.terra.fabric.util.FabricAdapter;


public class FabricBlockState implements BlockState {
    private static final Map<Property<?>, Pair<net.minecraft.state.property.Property<?>, Function<Object, Object>>>
            PROPERTY_DELEGATES_T2M = Construct.construct(() -> {
        Map<Property<?>, Pair<net.minecraft.state.property.Property<?>, Function<Object, Object>>> map = new HashMap<>();
        map.put(Properties.AXIS, Pair.of(net.minecraft.state.property.Properties.AXIS,
                                         a -> FabricAdapter.adapt((net.minecraft.util.math.Direction.Axis) a)));
        
        map.put(Properties.NORTH, Pair.of(net.minecraft.state.property.Properties.NORTH, Function.identity()));
        map.put(Properties.SOUTH, Pair.of(net.minecraft.state.property.Properties.SOUTH, Function.identity()));
        map.put(Properties.EAST, Pair.of(net.minecraft.state.property.Properties.EAST, Function.identity()));
        map.put(Properties.WEST, Pair.of(net.minecraft.state.property.Properties.WEST, Function.identity()));
        
        map.put(Properties.NORTH_CONNECTION, Pair.of(net.minecraft.state.property.Properties.NORTH_WIRE_CONNECTION,
                                                     c -> FabricAdapter.adapt((WireConnection) c)));
        map.put(Properties.SOUTH_CONNECTION, Pair.of(net.minecraft.state.property.Properties.SOUTH_WIRE_CONNECTION,
                                                     c -> FabricAdapter.adapt((WireConnection) c)));
        map.put(Properties.EAST_CONNECTION, Pair.of(net.minecraft.state.property.Properties.EAST_WIRE_CONNECTION,
                                                    c -> FabricAdapter.adapt((WireConnection) c)));
        map.put(Properties.WEST_CONNECTION, Pair.of(net.minecraft.state.property.Properties.WEST_WIRE_CONNECTION,
                                                    c -> FabricAdapter.adapt((WireConnection) c)));
        
        
        map.put(Properties.NORTH_HEIGHT,
                Pair.of(net.minecraft.state.property.Properties.NORTH_WALL_SHAPE, h -> FabricAdapter.adapt((WallShape) h)));
        map.put(Properties.SOUTH_HEIGHT,
                Pair.of(net.minecraft.state.property.Properties.SOUTH_WALL_SHAPE, h -> FabricAdapter.adapt((WallShape) h)));
        map.put(Properties.EAST_HEIGHT,
                Pair.of(net.minecraft.state.property.Properties.EAST_WALL_SHAPE, h -> FabricAdapter.adapt((WallShape) h)));
        map.put(Properties.WEST_HEIGHT,
                Pair.of(net.minecraft.state.property.Properties.WEST_WALL_SHAPE, h -> FabricAdapter.adapt((WallShape) h)));
        
        map.put(Properties.DIRECTION,
                Pair.of(net.minecraft.state.property.Properties.FACING, d -> FabricAdapter.adapt((Direction) d)));
        
        map.put(Properties.WATERLOGGED, Pair.of(net.minecraft.state.property.Properties.WATERLOGGED, Function.identity()));
        
        map.put(Properties.RAIL_SHAPE,
                Pair.of(net.minecraft.state.property.Properties.RAIL_SHAPE, s -> FabricAdapter.adapt((RailShape) s)));
        
        map.put(Properties.HALF,
                Pair.of(net.minecraft.state.property.Properties.BLOCK_HALF, h -> FabricAdapter.adapt((BlockHalf) h)));
        
        return map;
    });
    
    private static final Map<net.minecraft.state.property.Property<?>, Property<?>> PROPERTY_DELEGATES_M2T = Construct.construct(() -> {
        Map<net.minecraft.state.property.Property<?>, Property<?>> map = new HashMap<>();
        PROPERTY_DELEGATES_T2M.forEach((p, p2) -> map.put(p2.getLeft(), p));
        return map;
    });
    
    protected net.minecraft.block.BlockState delegate;
    
    public FabricBlockState(net.minecraft.block.BlockState delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public boolean matches(BlockState other) {
        return delegate.getBlock() == ((FabricBlockState) other).delegate.getBlock();
    }
    
    @Override
    public <T> boolean has(Property<T> property) {
        return delegate.getProperties().contains(PROPERTY_DELEGATES_T2M.get(property).getLeft());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Property<T> property) {
        Pair<net.minecraft.state.property.Property<?>, Function<Object, Object>> pair = PROPERTY_DELEGATES_T2M.get(property);
        return (T) pair.getRight().apply(delegate.get(pair.getLeft()));
    }
    
    @Override
    public <T> BlockState set(Property<T> property, T value) {
        //return delegate = delegate.with(PROPERTY_DELEGATES_T2M.get(property), v);
        return this;
    }
    
    @Override
    public BlockType getBlockType() {
        return (BlockType) delegate.getBlock();
    }
    
    @Override
    public String getAsString() {
        StringBuilder data = new StringBuilder(Registry.BLOCK.getId(delegate.getBlock()).toString());
        if(!delegate.getEntries().isEmpty()) {
            data.append('[');
            data.append(
                    delegate.getEntries().entrySet().stream().map(StateAccessor.getPropertyMapPrinter()).collect(Collectors.joining(",")));
            data.append(']');
        }
        return data.toString();
    }
    
    @Override
    public boolean isAir() {
        return delegate.isAir();
    }

    @Override
    public BlockState clone() {
        try {
            return (FabricBlockState) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
    
    @Override
    public net.minecraft.block.BlockState getHandle() {
        return delegate;
    }
}
