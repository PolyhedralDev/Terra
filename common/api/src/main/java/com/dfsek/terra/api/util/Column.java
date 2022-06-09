package com.dfsek.terra.api.util;

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
}
