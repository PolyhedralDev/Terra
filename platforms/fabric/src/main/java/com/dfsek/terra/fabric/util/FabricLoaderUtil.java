package com.dfsek.terra.fabric.util;

import com.dfsek.terra.lifecycle.util.LoaderUtil;

import net.fabricmc.loader.api.FabricLoader;


public class FabricLoaderUtil extends LoaderUtil {
    @Override
    public String mapClassName(String namespace, String className) {
        return FabricLoader.getInstance().getMappingResolver().mapClassName(namespace, className);
    }
    
    @Override
    public String mapMethodName(String namespace, String owner, String name, String descriptor) {
        return FabricLoader.getInstance().getMappingResolver().mapMethodName(namespace, owner, name, descriptor);
    }
}
