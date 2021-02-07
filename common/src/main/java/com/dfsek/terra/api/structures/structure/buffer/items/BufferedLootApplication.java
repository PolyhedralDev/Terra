package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.api.platform.block.state.Container;
import com.dfsek.terra.api.structures.loot.LootTable;
import com.dfsek.terra.api.util.FastRandom;

public class BufferedLootApplication implements BufferedItem {
    private final LootTable table;
    private final TerraPlugin main;

    public BufferedLootApplication(LootTable table, TerraPlugin main) {
        this.table = table;
        this.main = main;
    }

    @Override
    public void paste(Location origin) {
        BlockState data = origin.getBlock().getState();
        if(!(data instanceof Container)) {
            main.getLogger().severe("Failed to place loot at " + origin + "; block " + data + " is not container.");
            return;
        }
        Container container = (Container) data;
        table.fillInventory(container.getInventory(), new FastRandom(origin.hashCode()));
    }
}
