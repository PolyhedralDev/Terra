package com.dfsek.terra.fabric.mixin.inventory;

import com.dfsek.terra.api.platform.inventory.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(Enchantment.class)
@Implements(@Interface(iface = com.dfsek.terra.api.platform.inventory.item.Enchantment.class, prefix = "terra$"))
public abstract class EnchantmentMixin {
    @Shadow
    public abstract boolean isAcceptableItem(net.minecraft.item.ItemStack stack);

    @Shadow
    public abstract boolean canCombine(Enchantment other);

    public Object terra$getHandle() {
        return this;
    }

    @SuppressWarnings("ConstantConditions")
    public boolean terra$canEnchantItem(ItemStack itemStack) {
        return isAcceptableItem((net.minecraft.item.ItemStack) (Object) itemStack);
    }

    public String terra$getID() {
        return Objects.requireNonNull(Registry.ENCHANTMENT.getId((Enchantment) (Object) this)).toString();
    }

    public boolean terra$conflictsWith(com.dfsek.terra.api.platform.inventory.item.Enchantment other) {
        return !canCombine((Enchantment) other);
    }
}
