package com.dfsek.terra.api.platform.inventory.item;

import com.dfsek.terra.api.platform.Handle;

public interface Damageable extends Handle, Cloneable {
    Damageable clone();
    int getDamage();
    void setDamage(int damage);
    boolean hasDamage();
}
