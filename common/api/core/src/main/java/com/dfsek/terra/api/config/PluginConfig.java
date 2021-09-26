package com.dfsek.terra.api.config;

import com.dfsek.terra.api.Platform;


public interface PluginConfig {
    void load(Platform main);
    
    boolean dumpDefaultConfig();
    
    String getLanguage();
    
    boolean isDebugCommands();
    
    boolean isDebugLogging();
    
    boolean isDebugProfiler();
    
    boolean isDebugScript();
    
    long getDataSaveInterval();
    
    int getBiomeSearchResolution();
    
    int getCarverCacheSize();
    
    int getStructureCache();
    
    int getSamplerCache();
    
    int getMaxRecursion();
    
    int getBiomeCache();
    
    int getProviderCache();
}
