package com.dfsek.terra.api.world.util;

public final class Interceptors {
    private static final ReadInterceptor READ_THROUGH = ((x, y, z, world) -> world.getBlockState(x, y, z));
    private static final WriteInterceptor WRITE_THROUGH = ((x, y, z, block, world, physics) -> world.setBlockState(x, y, z, block,
                                                                                                                   physics));
    
    private Interceptors() {
    
    }
    
    public static ReadInterceptor readThrough() {
        return READ_THROUGH;
    }
    
    public static WriteInterceptor writeThrough() {
        return WRITE_THROUGH;
    }
}
