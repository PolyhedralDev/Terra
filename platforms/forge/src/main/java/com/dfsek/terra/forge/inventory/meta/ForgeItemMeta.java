package com.dfsek.terra.forge.inventory.meta;

import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;
import com.dfsek.terra.forge.world.ForgeAdapter;
import net.minecraft.command.arguments.NBTCompoundTagArgument;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.registry.Registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ForgeItemMeta implements ItemMeta {
    protected final ItemStack delegate;

    public ForgeItemMeta(ItemStack delegate) {
        this.delegate = delegate;
    }

    @Override
    public ItemStack getHandle() {
        return delegate;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        if(!delegate.isEnchanted()) return Collections.emptyMap();
        Map<Enchantment, Integer> map = new HashMap<>();

        delegate.getEnchantmentTags().forEach(enchantment -> {
            CompoundNBT eTag = (CompoundNBT) enchantment;
            map.put(ForgeAdapter.adapt(Registry.ENCHANTMENT.byId(eTag.getInt("id"))), eTag.getInt("lvl"));
        });
        return map;
    }

    @Override
    public void addEnchantment(Enchantment enchantment, int level) {
        delegate.enchant(ForgeAdapter.adapt(enchantment), level);
    }
}
