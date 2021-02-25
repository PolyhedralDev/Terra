package com.dfsek.terra.fabric.world.block.state;

import com.dfsek.terra.api.platform.block.state.Container;
import com.dfsek.terra.api.platform.inventory.Inventory;
import com.dfsek.terra.fabric.inventory.FabricInventory;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.world.WorldAccess;

public class FabricContainer extends FabricBlockState implements Container {
    public FabricContainer(LootableContainerBlockEntity blockEntity, WorldAccess worldAccess) {
        super(blockEntity, worldAccess);
    }

    @Override
    public Inventory getInventory() {
        return new FabricInventory(((LootableContainerBlockEntity) blockEntity));
    }
}
