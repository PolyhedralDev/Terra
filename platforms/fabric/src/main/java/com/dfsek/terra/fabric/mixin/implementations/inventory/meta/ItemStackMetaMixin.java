package com.dfsek.terra.fabric.mixin.implementations.inventory.meta;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.api.inventory.item.ItemMeta;


@Mixin(ItemStack.class)
@Implements(@Interface(iface = ItemMeta.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ItemStackMetaMixin {
    @Shadow
    public abstract boolean hasEnchantments();
    
    @Shadow
    public abstract NbtList getEnchantments();
    
    @Shadow
    public abstract void addEnchantment(net.minecraft.enchantment.Enchantment enchantment, int level);
    
    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }
    
    public void terra$addEnchantment(Enchantment enchantment, int level) {
        addEnchantment((net.minecraft.enchantment.Enchantment) enchantment, level);
    }
    
    @Intrinsic(displace = true)
    public Map<Enchantment, Integer> terra$getEnchantments() {
        if(!hasEnchantments()) return Collections.emptyMap();
        Map<Enchantment, Integer> map = new HashMap<>();
        
        getEnchantments().forEach(enchantment -> {
            NbtCompound eTag = (NbtCompound) enchantment;
            map.put((Enchantment) Registry.ENCHANTMENT.get(eTag.getInt("id")), eTag.getInt("lvl"));
        });
        return map;
    }
}
