/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.util;

import java.io.Serial;
import java.io.Serializable;


public abstract class HashIntrinsic implements Serializable {
    public static final int FLOAT_EXP_BIT_MASK = 2139095040;
    public static final int FLOAT_SIGNIF_BIT_MASK = 8388607;
    public static final long DOUBLE_EXP_BIT_MASK = 9218868437227405312L;
    public static final long DOUBLE_SIGNIF_BIT_MASK = 4503599627370495L;
    protected static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected static final int MAXIMUM_CAPACITY = 1073741824;
    protected static final float DEFAULT_LOAD_FACTOR = 0.75F;
    @Serial
    private static final long serialVersionUID = 8058099372006904458L;
    protected int size;
    protected int threshold;
    protected float loadFactor;
    protected int capMinus1;
    
    protected HashIntrinsic(int initialCapacity, float loadFactor) {
        if(initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        } else if(!(loadFactor <= 0.0F) && !Float.isNaN(loadFactor)) {
            if(initialCapacity > 1073741824) {
                initialCapacity = 1073741824;
            }
            
            int capacity;
            for(capacity = 1; capacity < initialCapacity; capacity <<= 1) {
            }
            
            this.capMinus1 = capacity - 1;
            this.loadFactor = loadFactor;
            this.threshold = (int) ((float) capacity * loadFactor);
        } else {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
    }
    
    public static int floatToIntBits(float value) {
        int result = Float.floatToRawIntBits(value);
        if((result & 2139095040) == 2139095040 && (result & 8388607) != 0) {
            result = 2143289344;
        }
        
        return result;
    }
    
    public static long doubleToLongBits(double value) {
        long result = Double.doubleToRawLongBits(value);
        if((result & 9218868437227405312L) == 9218868437227405312L && (result & 4503599627370495L) != 0L) {
            result = 9221120237041090560L;
        }
        
        return result;
    }
    
    protected static int hashCodeLong(long value) {
        return (int) (value ^ value >>> 32);
    }
    
    protected static int hashCodeFloat(float value) {
        return floatToIntBits(value);
    }
    
    protected static int hashCodeDouble(double value) {
        long bits = doubleToLongBits(value);
        return (int) (bits ^ bits >>> 32);
    }
    
    protected static int tableIndex(int hc, int lm1) {
        hc ^= hc >>> 20 ^ hc >>> 12;
        hc ^= hc >>> 7 ^ hc >>> 4;
        return hc & lm1;
    }
    
    public int size() {
        return this.size;
    }
    
    public abstract void clear();
    
    public boolean isEmpty() {
        return this.size == 0;
    }
}
