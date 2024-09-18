package org.allaymc.terra.allay.delegate;

import org.allaymc.api.item.type.ItemType;

import com.dfsek.terra.api.inventory.Item;

/**
 * @author daoge_cmd
 */
public final class AllayItemType implements Item {
    private final ItemType<?> allayItemType;
    private final int beData;
    private final double maxDurability;

    public AllayItemType(ItemType<?> allayItemType, int beData) {
        this.allayItemType = allayItemType;
        this.beData = beData;
        // TODO: Better way to get max damage
        this.maxDurability = allayItemType.createItemStack().getItemData().maxDamage();
    }

    @Override
    public com.dfsek.terra.api.inventory.ItemStack newItemStack(int amount) {
        return new AllayItemStack(allayItemType.createItemStack(amount, beData));
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
