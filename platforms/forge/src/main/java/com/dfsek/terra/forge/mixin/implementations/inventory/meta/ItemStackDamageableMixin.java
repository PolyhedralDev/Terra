package com.dfsek.terra.forge.mixin.implementations.inventory.meta;

import com.dfsek.terra.api.inventory.item.Damageable;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
@Implements(@Interface(iface = Damageable.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ItemStackDamageableMixin {
    @Shadow
    public abstract boolean isDamaged();

    @Shadow
    public abstract int getDamageValue();

    @Shadow
    public abstract void setDamageValue(int p_196085_1_);

    public boolean terra$hasDamage() {
        return isDamaged();
    }

    @Intrinsic
    public void terra$setDamage(int damage) {
        setDamageValue(damage);
    }

    @Intrinsic
    public int terra$getDamage() {
        return getDamageValue();
    }
}
