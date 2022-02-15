/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure.feature;

import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.function.IntToBooleanFunction;


/**
 * A column of binary data
 */
public class BinaryColumn {
    private final IntToBooleanFunction data;
    private final int minY;
    private final int maxY;
    
    private static final BinaryColumn NULL = new BinaryColumn(0, 1, y -> false);
    
    /**
     * Constructs a new {@link BinaryColumn} with all values initiated to {@code false}
     * @param minY Minimum Y value
     * @param maxY Maximum Y value
     */
    public BinaryColumn(int minY, int maxY, IntToBooleanFunction data) {
        this.minY = minY;
        this.maxY = maxY;
        if(maxY <= minY) throw new IllegalArgumentException("Max y must be greater than min y");
        this.data = data;
    }
    
    public static BinaryColumn getNull() {
        return NULL;
    }
    
    public BinaryColumn(Range y, IntToBooleanFunction data) {
        this(y.getMin(), y.getMax(), data);
    }
    
    /**
     * Get the value at a height.
     * @param y Height of entry to get.
     * @return Whether height has been set.
     */
    public boolean get(int y) {
        return data.apply(y);
    }
    
    
    public boolean contains(int y) {
        return y >= minY && y < maxY;
    }
    
    /**
     * Perform an action for all heights which have been set.
     * @param consumer Action to perform
     */
    public void forEach(IntConsumer consumer) {
        for(int y : matching()) {
            consumer.accept(y);
        }
    }
    
    public int[] matching() {
        return IntStream.range(minY, maxY).filter(this::get).toArray();
    }
    
    /**
     * Return a {@link BinaryColumn} of equal height with a boolean AND operation applied to each height.
     * @param that Other binary column, must match this column's height.
     * @return Merged column.
     *
     * @throws IllegalArgumentException if column heights do not match
     */
    public BinaryColumn and(BinaryColumn that) {
        return bool(that, (a, b) -> a.getAsBoolean() && b.getAsBoolean());
    }
    
    /**
     * Return a {@link BinaryColumn} of equal height with a boolean OR operation applied to each height.
     * @param that Other binary column, must match this column's height.
     * @return Merged column.
     *
     * @throws IllegalArgumentException if column heights do not match
     */
    public BinaryColumn or(BinaryColumn that) {
        return bool(that, (a, b) -> a.getAsBoolean() || b.getAsBoolean());
    }
    
    public BinaryColumn xor(BinaryColumn that) {
        return bool(that, (a, b) -> a.getAsBoolean() ^ b.getAsBoolean());
    }
    
    private BinaryColumn bool(BinaryColumn that, BooleanBinaryOperator operator) {
        int smallMinY = Math.min(this.minY, that.minY);
        int bigMaxY = Math.max(this.maxY, that.maxY);
    
        return new BinaryColumn(smallMinY, bigMaxY, y -> operator.apply(() -> this.get(y), () -> that.get(y)));
    }
    
    private interface BooleanBinaryOperator {
        boolean apply(BooleanSupplier a, BooleanSupplier b);
    }
}
