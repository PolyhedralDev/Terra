package com.dfsek.terra.forge.world.block.state;

import com.dfsek.terra.api.platform.block.state.Container;
import com.dfsek.terra.api.platform.inventory.Inventory;
import com.dfsek.terra.forge.inventory.ForgeInventory;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.world.IWorld;

public class ForgeContainer extends ForgeBlockState implements Container {
    public ForgeContainer(LockableLootTileEntity blockEntity, IWorld worldAccess) {
        super(blockEntity, worldAccess);
    }

    @Override
    public Inventory getInventory() {
        return new ForgeInventory(((LockableLootTileEntity) blockEntity));
    }
}
