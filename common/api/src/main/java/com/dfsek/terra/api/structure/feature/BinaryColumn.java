/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure.feature;

import java.util.function.IntConsumer;

import com.dfsek.terra.api.util.Range;


/**
 * A column of binary data
 */
public class BinaryColumn {
    private final boolean[] data;
    private final int minY;
    private final int maxY;
    
    /**
     * Constructs a new {@link BinaryColumn} with all values initiated to {@code false}
     * @param minY Minimum Y value
     * @param maxY Maximum Y value
     */
    public BinaryColumn(int minY, int maxY) {
        this.minY = minY;
        this.maxY = maxY;
        if(maxY <= minY) throw new IllegalArgumentException("Max y must be greater than min y");
        this.data = new boolean[maxY - minY];
    }
    
    public BinaryColumn(Range y) {
        this(y.getMin(), y.getMax());
    }
    
    /**
     * Set the value of a height to {@code true}.
     * @param y Height of entry to set.
     */
    public void set(int y) {
        data[y - minY] = true;
    }
    
    /**
     * Get the value at a height.
     * @param y Height of entry to get.
     * @return Whether height has been set.
     */
    public boolean get(int y) {
        return data[y - minY];
    }
    
    
    public boolean contains(int y) {
        return y >= minY && y < maxY;
    }
    
    /**
     * Perform an action for all heights which have been set.
     * @param consumer Action to perform
     */
    public void forEach(IntConsumer consumer) {
        for(int y = 0; y < data.length; y++) {
            if(data[y]) {
                consumer.accept(y + minY);
            }
        }
    }
    
    /**
     * Return a {@link BinaryColumn} of equal height with a boolean AND operation applied to each height.
     * @param that Other binary column, must match this column's height.
     * @return Merged column.
     *
     * @throws IllegalArgumentException if column heights do not match
     */
    public BinaryColumn and(BinaryColumn that) {
        return bool(that, Boolean::logicalAnd);
    }
    
    /**
     * Return a {@link BinaryColumn} of equal height with a boolean OR operation applied to each height.
     * @param that Other binary column, must match this column's height.
     * @return Merged column.
     *
     * @throws IllegalArgumentException if column heights do not match
     */
    public BinaryColumn or(BinaryColumn that) {
        return bool(that, Boolean::logicalOr);
    }
    
    public BinaryColumn xor(BinaryColumn that) {
        return bool(that, Boolean::logicalXor);
    }
    
    private BinaryColumn bool(BinaryColumn that, BooleanBinaryOperator operator) {
        int smallMinY = Math.min(this.minY, that.minY);
        int bigMaxY = Math.max(this.maxY, that.maxY);
    
        BinaryColumn next = new BinaryColumn(smallMinY, bigMaxY);
    
        for(int i = smallMinY; i < bigMaxY; i++) {
            boolean left = false;
            boolean right = false;
            
            if(this.contains(i)) {
                left = this.get(i);
            }
        
            if(that.contains(i)) {
                right = that.get(i);
            }
            
            next.data[i - smallMinY] = operator.apply(left, right);
        }
    
        return next;
    }
    
    private interface BooleanBinaryOperator {
        boolean apply(boolean a, boolean b);
    }
}
