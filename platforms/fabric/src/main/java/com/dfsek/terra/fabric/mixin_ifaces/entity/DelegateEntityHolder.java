package com.dfsek.terra.fabric.mixin_ifaces.entity;

import java.util.List;


public interface DelegateEntityHolder {
    List<DelegateEntity> getAndClearTerraEntities();
    
    void addTerraEntity(DelegateEntity entity);
}
