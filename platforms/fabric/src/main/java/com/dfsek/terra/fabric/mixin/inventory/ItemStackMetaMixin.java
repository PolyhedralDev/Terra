package com.dfsek.terra.fabric.mixin.inventory;

import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Mixin(ItemStack.class)
@Implements(@Interface(iface = ItemMeta.class, prefix = "terra$"))
public abstract class ItemStackMetaMixin {
    @Shadow
    public abstract boolean hasEnchantments();

    @Shadow
    public abstract ListTag getEnchantments();

    @Shadow
    public abstract void addEnchantment(net.minecraft.enchantment.Enchantment enchantment, int level);

    public Object terra$getHandle() {
        return this;
    }

    public Map<Enchantment, Integer> terra$getEnchantments() {
        if(!hasEnchantments()) return Collections.emptyMap();
        Map<Enchantment, Integer> map = new HashMap<>();

        getEnchantments().forEach(enchantment -> {
            CompoundTag eTag = (CompoundTag) enchantment;
            map.put((Enchantment) Registry.ENCHANTMENT.get(eTag.getInt("id")), eTag.getInt("lvl"));
        });
        return map;
    }

    public void terra$addEnchantment(Enchantment enchantment, int level) {
        addEnchantment((net.minecraft.enchantment.Enchantment) enchantment, level);
    }
}
