package com.dfsek.terra.mod;

import java.util.concurrent.atomic.AtomicReference;


public final class CommonPlatform {
    private static final AtomicReference<ModPlatform> platform = new AtomicReference<>();
    
    public static ModPlatform get() {
        ModPlatform modPlatform = platform.get();
        
        if(modPlatform == null) {
            throw new IllegalStateException("Platform is not yet initialised!");
        }
        
        return modPlatform;
    }
    
    public static void initialize(ModPlatform modPlatform) {
        if(!platform.compareAndSet(null, modPlatform)) {
            throw new IllegalStateException("Platform has already been initialized to " + platform.get());
        }
    }
}
