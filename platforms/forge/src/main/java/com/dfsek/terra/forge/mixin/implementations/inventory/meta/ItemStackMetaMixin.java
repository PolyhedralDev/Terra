package com.dfsek.terra.forge.mixin.implementations.inventory.meta;

import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.api.inventory.item.ItemMeta;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Mixin(ItemStack.class)
@Implements(@Interface(iface = ItemMeta.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ItemStackMetaMixin {
    @Shadow
    public abstract ListNBT getEnchantmentTags();

    @Shadow
    public abstract boolean isEnchanted();

    @Shadow
    public abstract void enchant(net.minecraft.enchantment.Enchantment p_77966_1_, int p_77966_2_);

    public Object terra$getHandle() {
        return this;
    }

    @Intrinsic(displace = true)
    public Map<Enchantment, Integer> terra$getEnchantments() {
        if(!isEnchanted()) return Collections.emptyMap();
        Map<Enchantment, Integer> map = new HashMap<>();

        getEnchantmentTags().forEach(enchantment -> {
            CompoundNBT eTag = (CompoundNBT) enchantment;
            map.put((Enchantment) Registry.ENCHANTMENT.byId(eTag.getInt("id")), eTag.getInt("lvl"));
        });
        return map;
    }

    public void terra$addEnchantment(Enchantment enchantment, int level) {
        enchant((net.minecraft.enchantment.Enchantment) enchantment, level);
    }
}
