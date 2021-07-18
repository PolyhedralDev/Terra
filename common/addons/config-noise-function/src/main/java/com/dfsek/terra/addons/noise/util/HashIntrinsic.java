/*
Copyright 2009 Sandia Corporation. Under the terms of Contract
DE-AC04-94AL85000 with Sandia Corporation, the U.S. Government
retains certain rights in this software.

BSD Open Source License.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

   * Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
   * Neither the name of Sandia National Laboratories nor the names of its
     contributors may be used to endorse or promote products derived from
     this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
 */

package com.dfsek.terra.addons.noise.util;

import java.io.Serializable;

public abstract class HashIntrinsic implements Serializable {
    public static final int FLOAT_EXP_BIT_MASK = 2139095040;
    public static final int FLOAT_SIGNIF_BIT_MASK = 8388607;
    public static final long DOUBLE_EXP_BIT_MASK = 9218868437227405312L;
    public static final long DOUBLE_SIGNIF_BIT_MASK = 4503599627370495L;
    protected static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected static final int MAXIMUM_CAPACITY = 1073741824;
    protected static final float DEFAULT_LOAD_FACTOR = 0.75F;
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

    protected static int tableIndex(int hc, int lm1) {
        hc ^= hc >>> 20 ^ hc >>> 12;
        hc ^= hc >>> 7 ^ hc >>> 4;
        return hc & lm1;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public abstract void clear();
}
