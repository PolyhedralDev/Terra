/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.inventory.item;

import com.dfsek.terra.api.Handle;


public interface Damageable extends Handle {
    int getDamage();
    
    void setDamage(int damage);
    
    boolean hasDamage();
}
