package org.allaymc.terra.allay.delegate;

import org.allaymc.api.item.ItemStack;

import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.ItemMeta;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public record AllayItemStack(ItemStack allayItemStack) implements com.dfsek.terra.api.inventory.ItemStack{
    @Override
    public int getAmount() {
        return allayItemStack.getCount();
    }

    @Override
    public void setAmount(int i) {
        allayItemStack.setCount(i);
    }

    @Override
    public Item getType() {
        return new AllayItemType(allayItemStack.getItemType(), allayItemStack.getMeta());
    }

    @Override
    public ItemMeta getItemMeta() {
        return new AllayItemMeta(allayItemStack);
    }

    @Override
    public void setItemMeta(ItemMeta meta) {
        var targetItem = ((AllayItemMeta) meta).allayItemStack();
        allayItemStack.removeAllEnchantments();
        for (var enchantment : targetItem.getEnchantments()) {
            allayItemStack.addEnchantment(enchantment.getType(), enchantment.getLevel());
        }
        allayItemStack.setLore(targetItem.getLore());
        allayItemStack.setDurability(targetItem.getDurability());
        allayItemStack.setCustomName(targetItem.getCustomName());
        allayItemStack.setMeta(targetItem.getMeta());
    }

    @Override
    public ItemStack getHandle() {
        return allayItemStack;
    }
}
