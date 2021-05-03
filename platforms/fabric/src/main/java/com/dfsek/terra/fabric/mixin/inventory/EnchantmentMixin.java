package com.dfsek.terra.fabric.mixin.inventory;

import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.fabric.world.FabricAdapter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(Enchantment.class)
@Implements(@Interface(iface = com.dfsek.terra.api.platform.inventory.item.Enchantment.class, prefix = "vw$"))
public abstract class EnchantmentMixin {
    @Shadow
    public abstract boolean isAcceptableItem(net.minecraft.item.ItemStack stack);

    @Shadow
    public abstract boolean canCombine(Enchantment other);

    public Object vw$getHandle() {
        return this;
    }

    @SuppressWarnings("ConstantConditions")
    public boolean vw$canEnchantItem(ItemStack itemStack) {
        return isAcceptableItem((net.minecraft.item.ItemStack) (Object) itemStack);
    }

    public String vw$getID() {
        return Objects.requireNonNull(Registry.ENCHANTMENT.getId((Enchantment) (Object) this)).toString();
    }

    public boolean vw$conflictsWith(com.dfsek.terra.api.platform.inventory.item.Enchantment other) {
        return !canCombine((Enchantment) other);
    }
}
