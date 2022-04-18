package com.dfsek.terra.addons.structure.mutator.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.world.util.ReadInterceptor;
import com.dfsek.terra.api.world.util.WriteInterceptor;


public class MutatedStructureTemplate implements AbstractableTemplate {
    @Value("id")
    private String id;
    
    @Value("mutate.read")
    private ReadInterceptor readInterceptor;
    
    @Value("mutate.write")
    private WriteInterceptor writeInterceptor;
    
    @Value("structure")
    private Structure delegate;
    
    @Override
    public String getID() {
        return id;
    }
    
    public ReadInterceptor getReadInterceptor() {
        return readInterceptor;
    }
    
    public WriteInterceptor getWriteInterceptor() {
        return writeInterceptor;
    }
    
    public Structure getDelegate() {
        return delegate;
    }
}
