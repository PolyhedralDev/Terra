package com.dfsek.terra.fabric.mixin.implementations.inventory.meta;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.inventory.item.Damageable;


@Mixin(ItemStack.class)
@Implements(@Interface(iface = Damageable.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ItemStackDamageableMixin {
    @Shadow
    public abstract boolean isDamaged();
    
    @Shadow
    public abstract int getDamage();
    
    @Shadow
    public abstract void setDamage(int damage);
    
    @Intrinsic
    public int terra$getDamage() {
        return getDamage();
    }
    
    @Intrinsic
    public void terra$setDamage(int damage) {
        setDamage(damage);
    }
    
    public boolean terra$hasDamage() {
        return isDamaged();
    }
}
