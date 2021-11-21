/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure.feature;

import java.util.function.IntConsumer;


public class BinaryColumn {
    private final boolean[] data;
    private final int minY;
    
    public BinaryColumn(int minY, int maxY) {
        this.minY = minY;
        if(maxY <= minY) throw new IllegalArgumentException("Max y must be greater than min y");
        this.data = new boolean[maxY - minY];
    }
    
    public void set(int y) {
        data[y - minY] = true;
    }
    
    public boolean get(int y) {
        return data[y - minY];
    }
    
    public void forEach(IntConsumer consumer) {
        for(int y = 0; y < data.length; y++) {
            if(data[y]) {
                consumer.accept(y + minY);
            }
        }
    }
    
    public BinaryColumn and(BinaryColumn that) {
        if(that.minY != this.minY) throw new IllegalArgumentException("Must share same min Y");
        if(that.data.length != this.data.length) throw new IllegalArgumentException("Must share same max Y");
        BinaryColumn next = new BinaryColumn(minY, data.length - minY);
        
        for(int i = 0; i < this.data.length; i++) {
            next.data[i] = this.data[i] && that.data[i];
        }
        
        return next;
    }
    
    public BinaryColumn or(BinaryColumn that) {
        if(that.minY != this.minY) throw new IllegalArgumentException("Must share same min Y");
        if(that.data.length != this.data.length) throw new IllegalArgumentException("Must share same max Y");
        BinaryColumn next = new BinaryColumn(minY, data.length - minY);
        
        for(int i = 0; i < this.data.length; i++) {
            next.data[i] = this.data[i] || that.data[i];
        }
        
        return next;
    }
}
