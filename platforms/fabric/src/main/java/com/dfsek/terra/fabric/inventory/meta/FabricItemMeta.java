package com.dfsek.terra.fabric.inventory.meta;

import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;
import com.dfsek.terra.fabric.world.FabricAdapter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FabricItemMeta implements ItemMeta {
    protected final ItemStack delegate;

    public FabricItemMeta(ItemStack delegate) {
        this.delegate = delegate;
    }

    @Override
    public ItemStack getHandle() {
        return delegate;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        if(!delegate.hasEnchantments()) return Collections.emptyMap();
        Map<Enchantment, Integer> map = new HashMap<>();

        delegate.getEnchantments().forEach(enchantment -> {
            CompoundTag eTag = (CompoundTag) enchantment;
            map.put(FabricAdapter.adapt(net.minecraft.enchantment.Enchantment.byRawId(eTag.getInt("id"))), eTag.getInt("lvl"));
        });
        return map;
    }

    @Override
    public void addEnchantment(Enchantment enchantment, int level) {
        delegate.addEnchantment(FabricAdapter.adapt(enchantment), level);
    }
}
