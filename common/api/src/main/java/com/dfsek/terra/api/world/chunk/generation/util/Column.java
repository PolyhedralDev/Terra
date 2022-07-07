/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.chunk.generation.util;

import java.util.function.IntConsumer;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.structure.feature.BinaryColumn;
import com.dfsek.terra.api.util.function.IntToBooleanFunction;
import com.dfsek.terra.api.world.WritableWorld;


/**
 * A single vertical column of a world.
 *
 * <b>Due to the {@link #clamp(int, int)} method, the height of the column may not always be the same as the world! Be careful!</b>
 */
public class Column<T extends WritableWorld> {
    private final int x;
    private final int z;
    private final int min;
    private final int max;
    private final T world;
    
    public Column(int x, int z, T world) {
        this(x, z, world, world.getMinHeight(), world.getMaxHeight());
    }
    
    public Column(int x, int z, T world, int min, int max) {
        this.x = x;
        this.z = z;
        this.world = world;
        this.max = max;
        this.min = min;
    }
    
    
    public int getX() {
        return x;
    }
    
    public int getZ() {
        return z;
    }
    
    public BlockState getBlock(int y) {
        return world.getBlockState(x, y, z);
    }
    
    public T getWorld() {
        return world;
    }
    
    public int getMinY() {
        return min;
    }
    
    public int getMaxY() {
        return max;
    }
    
    public void forEach(IntConsumer function) {
        for(int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
            function.accept(y);
        }
    }
    
    public Column<T> clamp(int min, int max) {
        if(min >= max) throw new IllegalArgumentException("Min greater than or equal to max: " + min + ", " + max);
        return new Column<>(x, z, world, min, max);
    }
    
    public BinaryColumn newBinaryColumn(IntToBooleanFunction function) {
        return new BinaryColumn(getMinY(), getMaxY(), function);
    }
    
    public BinaryColumnBuilder newBinaryColumn() {
        return new BinaryColumnBuilder(this);
    }
    
    @SuppressWarnings("unchecked")
    public Column<T> adjacent(int offsetX, int offsetZ) {
        return (Column<T>) world.column(x + offsetX, z + offsetZ);
    }
    
    
    public static class BinaryColumnBuilder {
        private final boolean[] arr;
        private final Column<?> column;
        
        public BinaryColumnBuilder(Column<?> column) {
            this.column = column;
            arr = new boolean[column.getMaxY() - column.getMinY()];
        }
        
        public BinaryColumn build() {
            return new BinaryColumn(column.getMinY(), column.getMaxY(), arr);
        }
        
        public BinaryColumnBuilder set(int y) {
            arr[y - column.getMinY()] = true;
            return this;
        }
    }
}