package org.allaymc.terra.allay.delegate;

import org.allaymc.api.item.type.ItemType;

import com.dfsek.terra.api.inventory.Item;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public final class AllayItemType implements Item {
    private final ItemType<?> allayItemType;
    private final double maxDurability;

    public AllayItemType(ItemType<?> allayItemType) {
        this.allayItemType = allayItemType;
        // TODO: 感觉不太优雅，应该有更好的办法
        this.maxDurability = allayItemType.createItemStack().getItemData().maxDamage();
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

    public ItemType<?> allayItemType() {
        return allayItemType;
    }
}
