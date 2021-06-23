package com.dfsek.terra.api.inventory;

import com.dfsek.terra.api.block.Block;

public interface BlockInventoryHolder extends InventoryHolder {
    Block getBlock();
}
