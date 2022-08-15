package com.dfsek.terra.api.util.function;

import java.util.function.Function;


public final class Functions {
    private Functions() {
    
    }
    
    public static <T, U> Function<T, U> constant(U value) {
        return ignore -> value;
    }
}
