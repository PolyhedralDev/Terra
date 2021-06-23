package com.dfsek.terra.api;

public interface Logger {
    void info(String message);

    void warning(String message);

    void severe(String message);

    default void stack(Throwable t) {
        t.printStackTrace();
    }
}
