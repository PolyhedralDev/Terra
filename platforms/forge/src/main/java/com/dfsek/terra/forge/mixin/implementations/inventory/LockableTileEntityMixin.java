package com.dfsek.terra.forge.mixin.implementations.inventory;

import com.dfsek.terra.api.inventory.Inventory;
import com.dfsek.terra.api.inventory.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.LockableTileEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LockableTileEntity.class)
@Implements(@Interface(iface = Inventory.class, prefix = "terra$", remap = Interface.Remap.NONE))
public class LockableTileEntityMixin {
    public Object terra$getHandle() {
        return this;
    }

    public int terra$getSize() {
        return ((LockableTileEntity) (Object) this).getContainerSize();
    }

    @SuppressWarnings("ConstantConditions")
    public ItemStack terra$getItem(int slot) {
        net.minecraft.item.ItemStack itemStack = ((LockableTileEntity) (Object) this).getItem(slot);
        return itemStack.getItem() == Items.AIR ? null : (ItemStack) (Object) itemStack;
    }

    @SuppressWarnings("ConstantConditions")
    public void terra$setItem(int slot, ItemStack newStack) {
        ((LockableTileEntity) (Object) this).setItem(slot, (net.minecraft.item.ItemStack) (Object) newStack);
    }
}
