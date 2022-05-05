package com.dfsek.terra.api.addon.bootstrap;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;


public class BootstrapAddonClassLoader extends URLClassLoader {
    public BootstrapAddonClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
    
    public BootstrapAddonClassLoader(URL[] urls) {
        super(urls);
    }
    
    public BootstrapAddonClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }
    
    public BootstrapAddonClassLoader(String name, URL[] urls, ClassLoader parent) {
        super(name, urls, parent);
    }
    
    public BootstrapAddonClassLoader(String name, URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(name, urls, parent, factory);
    }
    
    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}
