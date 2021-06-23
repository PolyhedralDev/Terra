package com.dfsek.terra.api.platform.inventory.item;

import com.dfsek.terra.api.platform.Handle;

public interface Damageable extends Handle {
    int getDamage();

    void setDamage(int damage);

    boolean hasDamage();
}
