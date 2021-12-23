/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.inventory;

import com.dfsek.terra.api.Handle;


public interface Inventory extends Handle {
    void setItem(int slot, ItemStack newStack);
    
    int getSize();
    
    ItemStack getItem(int slot);
}
