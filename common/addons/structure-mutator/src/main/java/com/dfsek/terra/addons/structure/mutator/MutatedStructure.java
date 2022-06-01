package com.dfsek.terra.addons.structure.mutator;

import java.util.Random;

import com.dfsek.terra.api.registry.key.Keyed;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.WritableWorld;
import com.dfsek.terra.api.world.util.ReadInterceptor;
import com.dfsek.terra.api.world.util.WriteInterceptor;


public class MutatedStructure implements Structure, Keyed<MutatedStructure> {
    private final RegistryKey key;
    private final Structure base;
    private final ReadInterceptor readInterceptor;
    private final WriteInterceptor writeInterceptor;
    
    public MutatedStructure(RegistryKey key, Structure base,
                            ReadInterceptor readInterceptor, WriteInterceptor writeInterceptor) {
        this.key = key;
        this.base = base;
        this.readInterceptor = readInterceptor;
        this.writeInterceptor = writeInterceptor;
    }
    
    @Override
    public RegistryKey getRegistryKey() {
        return key;
    }
    
    @Override
    public boolean generate(Vector3Int location, WritableWorld world, Random random, Rotation rotation) {
        return base.generate(location,
                             world
                                     .buffer()
                                     .read(readInterceptor)
                                     .write(writeInterceptor)
                                     .build(),
                             random, rotation);
    }
}
