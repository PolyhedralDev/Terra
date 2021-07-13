package com.dfsek.terra.fabric.block;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;
import com.dfsek.terra.api.block.state.properties.base.Properties;
import com.dfsek.terra.api.util.generic.Construct;
import com.dfsek.terra.api.util.generic.pair.ImmutablePair;
import com.dfsek.terra.fabric.mixin.access.StateAccessor;
import com.dfsek.terra.fabric.util.FabricAdapter;
import net.minecraft.block.Blocks;
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

public class FabricBlockState implements BlockState {
    private static final Map<Property<?>, ImmutablePair<net.minecraft.state.property.Property<?>, Function<Object, Object>>> PROPERTY_DELEGATES_T2M = Construct.construct(() -> {
        Map<Property<?>, ImmutablePair<net.minecraft.state.property.Property<?>, Function<Object, Object>>> map = new HashMap<>();
        map.put(Properties.AXIS, ImmutablePair.of(net.minecraft.state.property.Properties.AXIS, a -> FabricAdapter.adapt((net.minecraft.util.math.Direction.Axis) a)));

        map.put(Properties.NORTH, ImmutablePair.of(net.minecraft.state.property.Properties.NORTH, Function.identity()));
        map.put(Properties.SOUTH, ImmutablePair.of(net.minecraft.state.property.Properties.SOUTH, Function.identity()));
        map.put(Properties.EAST, ImmutablePair.of(net.minecraft.state.property.Properties.EAST, Function.identity()));
        map.put(Properties.WEST, ImmutablePair.of(net.minecraft.state.property.Properties.WEST, Function.identity()));

        map.put(Properties.NORTH_CONNECTION, ImmutablePair.of(net.minecraft.state.property.Properties.NORTH_WIRE_CONNECTION, c -> FabricAdapter.adapt((WireConnection) c)));
        map.put(Properties.SOUTH_CONNECTION, ImmutablePair.of(net.minecraft.state.property.Properties.SOUTH_WIRE_CONNECTION, c -> FabricAdapter.adapt((WireConnection) c)));
        map.put(Properties.EAST_CONNECTION, ImmutablePair.of(net.minecraft.state.property.Properties.EAST_WIRE_CONNECTION, c -> FabricAdapter.adapt((WireConnection) c)));
        map.put(Properties.WEST_CONNECTION, ImmutablePair.of(net.minecraft.state.property.Properties.WEST_WIRE_CONNECTION, c -> FabricAdapter.adapt((WireConnection) c)));


        map.put(Properties.NORTH_HEIGHT, ImmutablePair.of(net.minecraft.state.property.Properties.NORTH_WALL_SHAPE, h -> FabricAdapter.adapt((WallShape) h)));
        map.put(Properties.SOUTH_HEIGHT, ImmutablePair.of(net.minecraft.state.property.Properties.SOUTH_WALL_SHAPE, h -> FabricAdapter.adapt((WallShape) h)));
        map.put(Properties.EAST_HEIGHT, ImmutablePair.of(net.minecraft.state.property.Properties.EAST_WALL_SHAPE, h -> FabricAdapter.adapt((WallShape) h)));
        map.put(Properties.WEST_HEIGHT, ImmutablePair.of(net.minecraft.state.property.Properties.WEST_WALL_SHAPE, h -> FabricAdapter.adapt((WallShape) h)));

        map.put(Properties.DIRECTION, ImmutablePair.of(net.minecraft.state.property.Properties.FACING, d -> FabricAdapter.adapt((Direction) d)));

        map.put(Properties.WATERLOGGED, ImmutablePair.of(net.minecraft.state.property.Properties.WATERLOGGED, Function.identity()));

        map.put(Properties.RAIL_SHAPE, ImmutablePair.of(net.minecraft.state.property.Properties.RAIL_SHAPE, s -> FabricAdapter.adapt((RailShape) s)));

        map.put(Properties.HALF, ImmutablePair.of(net.minecraft.state.property.Properties.BLOCK_HALF, h -> FabricAdapter.adapt((BlockHalf) h)));

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
    public BlockType getBlockType() {
        return (BlockType) delegate.getBlock();
    }

    @Override
    public boolean matches(BlockState other) {
        return delegate.getBlock() == ((FabricBlockState) other).delegate.getBlock();
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
    public String getAsString() {
        StringBuilder data = new StringBuilder(Registry.BLOCK.getId(delegate.getBlock()).toString());
        if(!delegate.getEntries().isEmpty()) {
            data.append('[');
            data.append(delegate.getEntries().entrySet().stream().map(StateAccessor.getPropertyMapPrinter()).collect(Collectors.joining(",")));
            data.append(']');
        }
        return data.toString();
    }

    @Override
    public boolean isAir() {
        return delegate.isAir();
    }

    @Override
    public boolean isStructureVoid() {
        return delegate.getBlock() == Blocks.STRUCTURE_VOID;
    }

    @Override
    public <T> boolean has(Property<T> property) {
        return delegate.getProperties().contains(PROPERTY_DELEGATES_T2M.get(property).getLeft());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Property<T> property) {
        ImmutablePair<net.minecraft.state.property.Property<?>, Function<Object, Object>> pair = PROPERTY_DELEGATES_T2M.get(property);
        return (T) pair.getRight().apply(delegate.get(pair.getLeft()));
    }

    @Override
    public <T> BlockState set(Property<T> property, T value) {
        //return delegate = delegate.with(PROPERTY_DELEGATES_T2M.get(property), v);
        return this;
    }

    @Override
    public net.minecraft.block.BlockState getHandle() {
        return delegate;
    }
}
