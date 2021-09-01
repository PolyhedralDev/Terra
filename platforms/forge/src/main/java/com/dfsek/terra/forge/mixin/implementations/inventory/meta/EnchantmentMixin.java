package com.dfsek.terra.forge.mixin.implementations.inventory.meta;

import com.dfsek.terra.api.inventory.ItemStack;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;


@Mixin(Enchantment.class)
@Implements(@Interface(iface = com.dfsek.terra.api.inventory.item.Enchantment.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class EnchantmentMixin {
    
    @Shadow
    public abstract boolean canEnchant(net.minecraft.item.ItemStack p_92089_1_);
    
    public Object terra$getHandle() {
        return this;
    }
    
    @SuppressWarnings("ConstantConditions")
    public boolean terra$canEnchantItem(ItemStack itemStack) {
        return canEnchant((net.minecraft.item.ItemStack) (Object) itemStack);
    }
    
    public String terra$getID() {
        return Objects.requireNonNull(Registry.ENCHANTMENT.getId((Enchantment) (Object) this)).toString();
    }
    
    public boolean terra$conflictsWith(com.dfsek.terra.api.inventory.item.Enchantment other) {
        return !isCompatibleWith((Enchantment) other);
    }
    
    @Shadow
    public abstract boolean isCompatibleWith(Enchantment p_191560_1_);
}
