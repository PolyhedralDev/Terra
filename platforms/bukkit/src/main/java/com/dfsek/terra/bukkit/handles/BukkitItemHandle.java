package com.dfsek.terra.bukkit.handles;

import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.bukkit.util.MinecraftUtils;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BukkitItemHandle implements ItemHandle {

    @Override
    public Item createItem(String data) {
        return BukkitAdapter.adapt(Material.matchMaterial(data));
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
