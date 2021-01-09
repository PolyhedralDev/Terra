package com.dfsek.terra.api.platform.inventory;

import com.dfsek.terra.api.platform.Handle;

public interface InventoryHolder extends Handle {
    Inventory getInventory();
}
