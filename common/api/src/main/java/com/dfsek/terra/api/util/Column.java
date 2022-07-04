package com.dfsek.terra.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.dfsek.terra.api.util.function.IntIntObjConsumer;
import com.dfsek.terra.api.util.function.IntObjConsumer;


public interface Column<T> {
    int getMinY();
    
    int getMaxY();
    
    int getX();
    
    int getZ();
    
    T get(int y);
    
    default void forEach(Consumer<T> consumer) {
        for(int y = getMinY(); y < getMaxY(); y++) {
            consumer.accept(get(y));
        }
    }
    
    default void forEach(IntObjConsumer<T> consumer) {
        for(int y = getMinY(); y < getMaxY(); y++) {
            consumer.accept(y, get(y));
        }
    }
    
    default void forRanges(int resolution, IntIntObjConsumer<T> consumer) {
        int min = getMinY();
        
        int y = min;
        
        T runningObj = get(y);
        
        int runningMin = min;
        
        int max = getMaxY() - 1;
        
        while(true) {
            y += resolution;
            if(y > max) {
                break;
            }
            T current = get(y);
            
            if(!current.equals(runningObj)) {
                consumer.accept(runningMin, y, runningObj);
                runningMin = y;
                runningObj = current;
            }
        }
        consumer.accept(runningMin, getMaxY(), runningObj);
    }
    
    default List<? extends T> asList() {
        List<T> list = new ArrayList<>();
        forEach((Consumer<T>) list::add);
        return list;
    }
}
