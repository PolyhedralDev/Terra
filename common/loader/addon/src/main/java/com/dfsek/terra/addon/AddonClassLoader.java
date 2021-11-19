package com.dfsek.terra.addon;

import java.net.URL;
import java.net.URLClassLoader;


public class AddonClassLoader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }
    
    public AddonClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
