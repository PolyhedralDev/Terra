package com.dfsek.terra.minestom.item;

import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.Enchantment;

import net.minestom.server.MinecraftServer;

import java.util.Set;
import java.util.stream.Collectors;


public class MinestomItemHandle implements ItemHandle {
    @Override
    public Item createItem(String data) {
        return new MinestomMaterial(data);
    }

    @Override
    public Enchantment getEnchantment(String id) {
        return new MinestomEnchantment(id);
    }

    @Override
    public Set<Enchantment> getEnchantments() {
        return MinecraftServer
            .getEnchantmentRegistry()
            .values()
            .stream()
            .map(MinestomEnchantment::new)
            .collect(Collectors.toSet());
    }
}
