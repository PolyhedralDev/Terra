package com.dfsek.terra.forge.mixin.implementations.block.state;

import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.inventory.Inventory;
import com.dfsek.terra.forge.mixin.implementations.block.TileEntityMixin;
import net.minecraft.tileentity.LockableLootTileEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LockableLootTileEntity.class)
@Implements(@Interface(iface = Container.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class LockableLootTileEntityMixin extends TileEntityMixin {
    public Inventory terra$getInventory() {
        return (Inventory) this;
    }

    public Object terra$getHandle() {
        return this;
    }
}
