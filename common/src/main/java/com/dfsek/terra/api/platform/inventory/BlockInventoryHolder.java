package com.dfsek.terra.api.platform.inventory;

import com.dfsek.terra.api.platform.block.Block;

public interface BlockInventoryHolder extends InventoryHolder {
    Block getBlock();
}
