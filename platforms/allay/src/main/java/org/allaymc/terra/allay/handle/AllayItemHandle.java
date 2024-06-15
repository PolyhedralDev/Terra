package org.allaymc.terra.allay.handle;

import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.Enchantment;

import org.allaymc.api.item.enchantment.EnchantmentRegistry;
import org.allaymc.api.item.enchantment.type.EnchantmentLuckOfTheSeaType;
import org.allaymc.api.item.registry.ItemTypeRegistry;
import org.allaymc.api.utils.Identifier;
import org.allaymc.terra.allay.Mapping;
import org.allaymc.terra.allay.delegate.AllayEnchantment;
import org.allaymc.terra.allay.delegate.AllayItemType;

import java.util.Set;
import java.util.stream.Collectors;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public class AllayItemHandle implements ItemHandle {
    @Override
    public Item createItem(String data) {
        return new AllayItemType(ItemTypeRegistry.getRegistry().get(new Identifier(Mapping.itemIdJeToBe(data))));
    }

    @Override
    public Enchantment getEnchantment(String id) {
        return new AllayEnchantment(EnchantmentRegistry.getRegistry().getByK2(new Identifier(Mapping.enchantmentIdJeToBe(id))));
    }

    @Override
    public Set<Enchantment> getEnchantments() {
        return EnchantmentRegistry.getRegistry().getContent().m1().values().stream()
            .map(AllayEnchantment::new)
            .collect(Collectors.toSet());
    }
}
