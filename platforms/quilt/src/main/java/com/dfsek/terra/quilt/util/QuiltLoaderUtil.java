package com.dfsek.terra.quilt.util;

import com.dfsek.terra.lifecycle.util.LoaderUtil;

import org.quiltmc.loader.api.QuiltLoader;


public class QuiltLoaderUtil extends LoaderUtil {
    @Override
    public String mapClassName(String namespace, String className) {
        return QuiltLoader.getMappingResolver().mapClassName(namespace, className);
    }
    
    @Override
    public String mapMethodName(String namespace, String owner, String name, String descriptor) {
        return QuiltLoader.getMappingResolver().mapMethodName(namespace, owner, name, descriptor);
    }
}
