package com.dfsek.terra.addons.terrascript.v2.util;

import com.dfsek.terra.addons.terrascript.v2.Type;


public class ASMUtil {
    
    /**
     * Dynamically get name to account for possibility of shading
     * @param clazz Class instance
     * @return Internal class name
     */
    public static String dynamicName(Class<?> clazz) {
        return clazz.getCanonicalName().replace('.', '/');
    }
    
    public static org.objectweb.asm.Type tsTypeToAsmType(Type type) {
        return org.objectweb.asm.Type.getType((Class<?>) type.javaType());
    }
}
