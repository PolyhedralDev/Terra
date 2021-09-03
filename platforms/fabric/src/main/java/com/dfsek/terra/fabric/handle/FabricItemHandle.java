package com.dfsek.terra.fabric.handle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Set;
import java.util.stream.Collectors;

import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.Enchantment;


public class FabricItemHandle implements ItemHandle {
    
    @Override
    public Item createItem(String data) {
        try {
            return (Item) new ItemStackArgumentType().parse(new StringReader(data)).getItem();
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException("Invalid item data \"" + data + "\"", e);
        }
    }
    
    @Override
    public Enchantment getEnchantment(String id) {
        return (Enchantment) (Registry.ENCHANTMENT.get(Identifier.tryParse(id)));
    }
    
    @Override
    public Set<Enchantment> getEnchantments() {
        return Registry.ENCHANTMENT.stream().map(enchantment -> (Enchantment) enchantment).collect(Collectors.toSet());
    }
}
