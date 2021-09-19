package com.dfsek.terra.api.inventory;

import com.dfsek.terra.api.Handle;


public interface InventoryHolder extends Handle {
    Inventory getInventory();
}
