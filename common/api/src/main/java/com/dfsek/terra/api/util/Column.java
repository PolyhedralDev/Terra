package com.dfsek.terra.api.util;

import com.dfsek.terra.api.util.function.IntIntObjConsumer;
import com.dfsek.terra.api.util.function.IntObjConsumer;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public interface Column<T> {
    int getMinY();
    
    int getMaxY();
    
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
    default void forRanges(IntIntObjConsumer<T> consumer) {
        int min = getMinY();
        
        int y = min + 1;
        
        T runningObj = get(min);
    
    
        int runningMin = min;
        int max = (getMaxY() - 1);
        
        while(y < max) {
            T current = get(y);
            
            if(!current.equals(runningObj)) {
                consumer.accept(runningMin, y, runningObj);
                runningMin = y;
                runningObj = current;
            }
            y++;
        }
        consumer.accept(runningMin, ++y, runningObj);
    }
    
    default List<? extends T> asList() {
        List<T> list = new ArrayList<>();
        forEach((Consumer<T>) list::add);
        return list;
    }
}
