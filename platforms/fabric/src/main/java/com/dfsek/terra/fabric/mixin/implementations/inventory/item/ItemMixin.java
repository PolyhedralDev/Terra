package com.dfsek.terra.fabric.mixin.implementations.inventory.item;

import com.dfsek.terra.api.platform.inventory.ItemStack;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
@Implements(@Interface(iface = com.dfsek.terra.api.platform.inventory.Item.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ItemMixin {
    @Shadow
    public abstract int getMaxDamage();

    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }

    @SuppressWarnings("ConstantConditions")
    public ItemStack terra$newItemStack(int amount) {
        return (ItemStack) (Object) new net.minecraft.item.ItemStack((Item) (Object) this, amount);
    }

    public double terra$getMaxDurability() {
        return getMaxDamage();
    }
}
