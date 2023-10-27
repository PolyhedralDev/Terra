package com.dfsek.terra.addons.terrascript.v2.codegen.asm;

public class DynamicClassLoader extends ClassLoader {
    public DynamicClassLoader(Class<?> clazz) {
        super(clazz.getClassLoader());
    }
    
    public Class<?> defineClass(String name, byte[] data) {
        return defineClass(name, data, 0, data.length);
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }
}
