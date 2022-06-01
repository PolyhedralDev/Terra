/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure.feature;

import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;

import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.function.IntToBooleanFunction;
import com.dfsek.terra.api.util.generic.Lazy;


/**
 * A column of binary data
 */
public class BinaryColumn {
    private static final BinaryColumn NULL = new BinaryColumn(0, 1, y -> false);
    private final IntToBooleanFunction data;
    private final int minY;
    private final int maxY;
    private final Lazy<boolean[]> results;
    
    /**
     * Constructs a new {@link BinaryColumn} with all values initiated to {@code false}
     *
     * @param minY Minimum Y value
     * @param maxY Maximum Y value
     */
    public BinaryColumn(int minY, int maxY, IntToBooleanFunction data) {
        this.minY = minY;
        this.maxY = maxY;
        this.results = Lazy.lazy(() -> {
            boolean[] res = new boolean[maxY - minY];
            for(int y = minY; y < maxY; y++) {
                res[y - minY] = get(y);
            }
            return res;
        });
        if(maxY <= minY) throw new IllegalArgumentException("Max y must be greater than min y");
        this.data = data;
    }
    
    public BinaryColumn(int minY, int maxY, boolean[] data) {
        this.minY = minY;
        this.maxY = maxY;
        this.results = Lazy.lazy(() -> data);
        if(maxY <= minY) throw new IllegalArgumentException("Max y must be greater than min y");
        this.data = y -> data[y - minY];
    }
    
    public BinaryColumn(Range y, IntToBooleanFunction data) {
        this(y.getMin(), y.getMax(), data);
    }
    
    public static BinaryColumn getNull() {
        return NULL;
    }
    
    /**
     * Get the value at a height.
     *
     * @param y Height of entry to get.
     *
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
     *
     * @param consumer Action to perform
     */
    public void forEach(IntConsumer consumer) {
        boolean[] results = this.results.value();
        for(int y = minY; y < maxY; y++) {
            if(results[y - minY]) {
                consumer.accept(y);
            }
        }
    }
    
    /**
     * Return a {@link BinaryColumn} of equal height with a boolean AND operation applied to each height.
     *
     * @param that Other binary column, must match this column's height.
     *
     * @return Merged column.
     *
     * @throws IllegalArgumentException if column heights do not match
     */
    public BinaryColumn and(BinaryColumn that) {
        int bigMinY = Math.max(this.minY, that.minY); // narrow new column, as areas outside will always be false.
        int smallMaxY = Math.min(this.maxY, that.maxY);
        
        if(bigMinY >= smallMaxY) return getNull();
        
        return new BinaryColumn(bigMinY, smallMaxY, y -> this.get(y) && that.get(y));
    }
    
    /**
     * Return a {@link BinaryColumn} of equal height with a boolean OR operation applied to each height.
     *
     * @param that Other binary column, must match this column's height.
     *
     * @return Merged column.
     *
     * @throws IllegalArgumentException if column heights do not match
     */
    public BinaryColumn or(BinaryColumn that) {
        return or(that, (a, b) -> a.getAsBoolean() || b.getAsBoolean());
    }
    
    public BinaryColumn xor(BinaryColumn that) {
        return or(that, (a, b) -> a.getAsBoolean() ^ b.getAsBoolean());
    }
    
    private BinaryColumn or(BinaryColumn that, BooleanBinaryOperator operator) {
        int smallMinY = Math.min(this.minY, that.minY);
        int bigMaxY = Math.max(this.maxY, that.maxY);
        
        return new BinaryColumn(smallMinY, bigMaxY, y -> operator.apply(() -> this.get(y), () -> that.get(y)));
    }
    
    private interface BooleanBinaryOperator {
        boolean apply(BooleanSupplier a, BooleanSupplier b);
    }
}
