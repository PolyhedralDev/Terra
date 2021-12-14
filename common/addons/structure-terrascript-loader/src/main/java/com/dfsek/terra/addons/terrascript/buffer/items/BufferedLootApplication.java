/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.buffer.items;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import com.dfsek.terra.addons.terrascript.script.StructureScript;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.event.events.world.generation.LootPopulateEvent;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.WritableWorld;


public class BufferedLootApplication implements BufferedItem {
    private static final Logger LOGGER = LoggerFactory.getLogger(BufferedLootApplication.class);
    private final LootTable table;
    private final Platform platform;
    private final StructureScript structure;
    
    public BufferedLootApplication(LootTable table, Platform platform, StructureScript structure) {
        this.table = table;
        this.platform = platform;
        this.structure = structure;
    }
    
    @Override
    public void paste(Vector3 origin, WritableWorld world) {
        try {
            BlockEntity data = world.getBlockState(origin);
            if(!(data instanceof Container container)) {
                LOGGER.error("Failed to place loot at {}; block {} is not a container", origin, data);
                return;
            }

            LootPopulateEvent event = new LootPopulateEvent(container, table, world.getPack(), structure);
            platform.getEventManager().callEvent(event);
            if(event.isCancelled()) return;
            
            event.getTable().fillInventory(container.getInventory(), new Random(origin.hashCode()));
            data.update(false);
        } catch(Exception e) {
            LOGGER.error("Could not apply loot at {}", origin, e);
            e.printStackTrace();
        }
    }
}
