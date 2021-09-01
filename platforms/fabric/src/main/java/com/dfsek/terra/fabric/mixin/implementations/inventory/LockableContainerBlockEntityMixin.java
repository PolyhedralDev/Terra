package com.dfsek.terra.fabric.mixin.implementations.inventory;

import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

import com.dfsek.terra.api.inventory.Inventory;
import com.dfsek.terra.api.inventory.ItemStack;


@Mixin(LockableContainerBlockEntity.class)
@Implements(@Interface(iface = Inventory.class, prefix = "terra$", remap = Interface.Remap.NONE))
public class LockableContainerBlockEntityMixin {
    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }
    
    @SuppressWarnings("ConstantConditions")
    public void terra$setItem(int slot, ItemStack newStack) {
        ((LockableContainerBlockEntity) (Object) this).setStack(slot, (net.minecraft.item.ItemStack) (Object) newStack);
    }
    
    public int terra$getSize() {
        return ((LockableContainerBlockEntity) (Object) this).size();
    }
    
    @SuppressWarnings("ConstantConditions")
    public ItemStack terra$getItem(int slot) {
        net.minecraft.item.ItemStack itemStack = ((LockableContainerBlockEntity) (Object) this).getStack(slot);
        return itemStack.getItem() == Items.AIR ? null : (ItemStack) (Object) itemStack;
    }
}
