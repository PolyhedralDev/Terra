package com.dfsek.terra.bukkit.handles;

import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.bukkit.util.MinecraftUtils;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.block.BukkitMaterialData;
import com.dfsek.terra.bukkit.world.inventory.BukkitItemStack;
import org.bukkit.NamespacedKey;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BukkitItemHandle implements ItemHandle {
    @Override
    public ItemStack newItemStack(MaterialData material, int amount) {
        return new BukkitItemStack(new org.bukkit.inventory.ItemStack(((BukkitMaterialData) material).getHandle(), amount));
    }

    @Override
    public Enchantment getEnchantment(String id) {
        return BukkitAdapter.adapt(org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft(MinecraftUtils.stripMinecraftNamespace(id))));
    }

    @Override
    public Set<Enchantment> getEnchantments() {
        return Arrays.stream(org.bukkit.enchantments.Enchantment.values()).map(BukkitAdapter::adapt).collect(Collectors.toSet());
    }
}
