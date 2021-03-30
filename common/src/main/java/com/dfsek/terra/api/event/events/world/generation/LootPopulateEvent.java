package com.dfsek.terra.api.event.events.world.generation;

import com.dfsek.terra.api.event.events.AbstractCancellable;
import com.dfsek.terra.api.event.events.Cancellable;
import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.state.Container;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedLootApplication;
import com.dfsek.terra.config.pack.ConfigPack;

/**
 * Called when loot is populated via {@link BufferedLootApplication}.
 */
public class LootPopulateEvent extends AbstractCancellable implements PackEvent, Cancellable {
    private final Block block;
    private final Container container;
    private final ConfigPack pack;

    public LootPopulateEvent(Block block, Container container, ConfigPack pack) {
        this.block = block;
        this.container = container;
        this.pack = pack;
    }

    @Override
    public ConfigPack getPack() {
        return pack;
    }

    /**
     * Get the block containing the tile entity loot is applied to.
     *
     * @return Block at which loot is applied.
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Get the {@link Container} representing the inventory.
     *
     * @return Inventory recieving loot.
     */
    public Container getContainer() {
        return container;
    }
}
