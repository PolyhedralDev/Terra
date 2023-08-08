package com.dfsek.terra.addons.terrascript.util;

public class ASMUtil {
    
    /**
     * Dynamically get name to account for possibility of shading
     * @param clazz Class instance
     * @return Internal class name
     */
    public static String dynamicName(Class<?> clazz) {
        return clazz.getCanonicalName().replace('.', '/');
    }
}
