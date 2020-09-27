package com.dfsek.terra.structure.serialize;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.io.Serializable;

public class SerializableEnchantment implements Serializable {
    public static final long serialVersionUID = 5298928608478640006L;
    private final SerializableNamespacedKey enchant;
    public SerializableEnchantment(Enchantment e) {
        enchant = new SerializableNamespacedKey(e.getKey());
    }
    public Enchantment toEnchantment() {
        return Enchantment.getByKey(enchant.getNamespacedKey());
    }
}
