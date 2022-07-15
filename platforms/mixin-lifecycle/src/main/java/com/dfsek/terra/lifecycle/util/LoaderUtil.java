package com.dfsek.terra.lifecycle.util;

public abstract class LoaderUtil {
    public static LoaderUtil INSTANCE;
    
    public abstract String mapClassName(String namespace, String className);
    
    public abstract String mapMethodName(String namespace, String owner, String name, String descriptor);
}
