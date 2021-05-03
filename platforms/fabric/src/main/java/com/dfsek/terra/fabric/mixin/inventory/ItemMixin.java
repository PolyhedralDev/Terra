package com.dfsek.terra.fabric.mixin.inventory;

import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.fabric.inventory.FabricItemStack;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
@Implements(@Interface(iface = com.dfsek.terra.api.platform.inventory.Item.class, prefix = "vw$"))
public abstract class ItemMixin {
    @Shadow
    public abstract int getMaxDamage();

    public Object vw$getHandle() {
        return this;
    }

    public ItemStack vw$newItemStack(int amount) {
        return new FabricItemStack(new net.minecraft.item.ItemStack((Item) (Object) this, amount));
    }

    public double vw$getMaxDurability() {
        return getMaxDamage();
    }
}
