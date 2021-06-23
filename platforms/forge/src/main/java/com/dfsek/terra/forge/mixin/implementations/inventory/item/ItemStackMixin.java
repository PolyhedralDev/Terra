package com.dfsek.terra.forge.mixin.implementations.inventory.item;

import com.dfsek.terra.api.platform.inventory.Item;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
@Implements(@Interface(iface = com.dfsek.terra.api.platform.inventory.ItemStack.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ItemStackMixin {
    @Shadow
    public abstract int getCount();

    @Shadow
    public abstract void setCount(int count);

    @Shadow
    public abstract net.minecraft.item.Item getItem();


    @Shadow
    public abstract boolean isDamageableItem();

    @Shadow
    public abstract void setTag(@Nullable CompoundNBT p_77982_1_);

    public int terra$getAmount() {
        return getCount();
    }

    public void terra$setAmount(int i) {
        setCount(i);
    }

    public Item terra$getType() {
        return (Item) getItem();
    }

    public ItemMeta terra$getItemMeta() {
        return (ItemMeta) this;
    }

    @SuppressWarnings("ConstantConditions")
    public void terra$setItemMeta(ItemMeta meta) {
        setTag(((ItemStack) (Object) meta).getTag());
    }

    public Object terra$getHandle() {
        return this;
    }

    @Intrinsic
    public boolean terra$isDamageable() {
        return isDamageableItem();
    }
}
