package com.dfsek.terra.api.util.generic;

import java.util.function.Supplier;


public final class Construct {
    public static <T> T construct(Supplier<T> in) {
        return in.get();
    }
}
