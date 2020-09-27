package com.dfsek.terra.structure.serialize;

import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializableItemStack implements Serializable {
    public static final long serialVersionUID = 5298928608478640005L;
    private final HashMap<SerializableEnchantment, Integer> enchantments = new HashMap<>();
    private final SerializableBlockData blockData;
    private transient ItemStack stack;
    private final SerializableItemMeta meta;
    private final int amount;
    public SerializableItemStack(ItemStack item) {
        for(Map.Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()) {
            this.enchantments.put(new SerializableEnchantment(e.getKey()), e.getValue());
        }
        this.meta = new SerializableItemMeta(item.getItemMeta());
        this.blockData = new SerializableBlockData(item.getType().createBlockData());
        this.amount = item.getAmount();
    }
    public ItemStack getStack() {
        if(stack == null) {
            stack = new ItemStack(blockData.getData().getMaterial(), amount);
        }
        return stack;
    }
}
