package com.dfsek.terra.api.inventory.item;

import com.dfsek.terra.api.Handle;

public interface Damageable extends Handle {
    int getDamage();

    void setDamage(int damage);

    boolean hasDamage();
}
