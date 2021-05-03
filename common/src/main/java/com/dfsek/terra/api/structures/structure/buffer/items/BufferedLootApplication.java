package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.event.events.world.generation.LootPopulateEvent;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.api.platform.block.state.Container;
import com.dfsek.terra.api.structures.loot.LootTable;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.util.FastRandom;

public class BufferedLootApplication implements BufferedItem {
    private final LootTable table;
    private final TerraPlugin main;
    private final StructureScript structure;

    public BufferedLootApplication(LootTable table, TerraPlugin main, StructureScript structure) {
        this.table = table;
        this.main = main;
        this.structure = structure;
    }

    @Override
    public void paste(Location origin) {
        try {
            Block block = origin.getBlock();
            BlockState data = block.getState();
            if(!(data instanceof Container)) {
                main.logger().severe("Failed to place loot at " + origin + "; block " + data + " is not container.");
                return;
            }
            Container container = (Container) data;

            LootPopulateEvent event = new LootPopulateEvent(block, container, table, block.getLocation().getWorld().getTerraGenerator().getConfigPack(), structure);
            main.getEventManager().callEvent(event);
            if(event.isCancelled()) return;

            event.getTable().fillInventory(container.getInventory(), new FastRandom(origin.hashCode()));
            data.update(false);
        } catch(Exception e) {
            main.logger().warning("Could not apply loot at " + origin + ": " + e.getMessage());
            main.getDebugLogger().stack(e);
        }
    }
}
