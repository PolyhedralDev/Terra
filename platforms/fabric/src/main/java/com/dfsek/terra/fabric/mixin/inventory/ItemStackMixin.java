package com.dfsek.terra.fabric.mixin.inventory;

import com.dfsek.terra.api.platform.inventory.Item;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
@Implements(@Interface(iface = com.dfsek.terra.api.platform.inventory.ItemStack.class, prefix = "vw$"))
public abstract class ItemStackMixin {
    @Shadow
    public abstract int getCount();

    @Shadow
    public abstract void setCount(int count);

    @Shadow
    public abstract net.minecraft.item.Item getItem();

    @Shadow
    public abstract boolean isDamageable();

    @Shadow
    public abstract ItemStack copy();

    public int vw$getAmount() {
        return getCount();
    }

    public void vw$setAmount(int i) {
        setCount(i);
    }

    public Item getType() {
        return (Item) getItem();
    }

    public ItemMeta vw$getItemMeta() {
        return (ItemMeta) this;
    }

    public void vw$setItemMeta(ItemMeta meta) {

    }

    public Object vw$getHandle() {
        return this;
    }
}
