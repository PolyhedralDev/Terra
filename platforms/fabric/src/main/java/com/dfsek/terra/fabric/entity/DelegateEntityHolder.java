package com.dfsek.terra.fabric.entity;

import java.util.List;


public interface DelegateEntityHolder {
    List<DelegateEntity> getAndClearTerraEntities();
    
    void addTerraEntity(DelegateEntity entity);
}
