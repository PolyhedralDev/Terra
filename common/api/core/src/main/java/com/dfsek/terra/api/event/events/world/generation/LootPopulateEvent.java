/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event.events.world.generation;

import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.AbstractCancellable;
import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.vector.Vector3;


/**
 * Called when loot is populated.
 */
public class LootPopulateEvent extends AbstractCancellable implements PackEvent {
    private final Container container;
    private final ConfigPack pack;
    private final Structure structure;
    private LootTable table;
    
    public LootPopulateEvent(Container container, LootTable table, ConfigPack pack, Structure structure) {
        this.container = container;
        this.table = table;
        this.pack = pack;
        this.structure = structure;
    }
    
    @Override
    public ConfigPack getPack() {
        return pack;
    }
    
    public Vector3 getPosition() {
        return container.getPosition();
    }
    
    /**
     * Get the {@link Container} representing the inventory.
     *
     * @return Inventory recieving loot.
     */
    public Container getContainer() {
        return container;
    }
    
    /**
     * Get the loot table to be populated.
     *
     * @return Loot table.
     */
    public LootTable getTable() {
        return table;
    }
    
    /**
     * Set the loot table to be populated.
     *
     * @param table New loot table.
     */
    public void setTable(@NotNull LootTable table) {
        this.table = table;
    }
    
    /**
     * Get the script used to generate the structure.
     *
     * @return Structure script.
     */
    public Structure getStructure() {
        return structure;
    }
}
