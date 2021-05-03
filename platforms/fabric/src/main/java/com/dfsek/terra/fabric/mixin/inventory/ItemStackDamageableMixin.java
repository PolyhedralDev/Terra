package com.dfsek.terra.fabric.mixin.inventory;

import com.dfsek.terra.api.platform.inventory.item.Damageable;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
@Implements(@Interface(iface = Damageable.class, prefix = "terra$"))
public abstract class ItemStackDamageableMixin {
    @Shadow
    public abstract boolean isDamaged();

    public boolean terra$hasDamage() {
        return isDamaged();
    }
}
