package com.dfsek.terra.api.generic.inventory.item;

import com.dfsek.terra.api.generic.Handle;

public interface Damageable extends Handle, Cloneable {
    Damageable clone();
    int getDamage();
    void setDamage(int damage);
    boolean hasDamage();
}
