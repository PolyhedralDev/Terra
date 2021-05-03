package com.dfsek.terra.fabric.inventory;

import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.fabric.world.FabricAdapter;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class FabricEnchantment implements Enchantment {
    private final net.minecraft.enchantment.Enchantment enchantment;

    public FabricEnchantment(net.minecraft.enchantment.Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public net.minecraft.enchantment.Enchantment getHandle() {
        return enchantment;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return enchantment.isAcceptableItem((net.minecraft.item.ItemStack) (Object) itemStack);
    }

    @Override
    public String getID() {
        return Objects.requireNonNull(Registry.ENCHANTMENT.getId(enchantment)).toString();
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return !enchantment.canCombine(FabricAdapter.adapt(other));
    }

    @Override
    public int getMaxLevel() {
        return enchantment.getMaxLevel();
    }
}
