package com.dfsek.terra.allay.delegate;

import org.allaymc.api.item.data.ItemId;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.registry.Registries;

import com.dfsek.terra.api.inventory.Item;


/**
 * @author daoge_cmd
 */
public final class AllayItemType implements Item {
    private final ItemType<?> allayItemType;
    private final double maxDurability;

    public AllayItemType(ItemType<?> allayItemType) {
        this.allayItemType = allayItemType;
        this.maxDurability = Registries.ITEM_DATA.get(ItemId.fromIdentifier(allayItemType.getIdentifier())).maxDamage();
    }

    @Override
    public com.dfsek.terra.api.inventory.ItemStack newItemStack(int amount) {
        return new AllayItemStack(allayItemType.createItemStack(amount));
    }

    @Override
    public double getMaxDurability() {
        return maxDurability;
    }

    @Override
    public ItemType<?> getHandle() {
        return allayItemType;
    }
}
