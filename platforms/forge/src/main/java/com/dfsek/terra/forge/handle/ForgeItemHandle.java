package com.dfsek.terra.forge.handle;

import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.Enchantment;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.stream.Collectors;


public class ForgeItemHandle implements ItemHandle {
    
    @Override
    public Item createItem(String data) {
        try {
            return (Item) new ItemArgument().parse(new StringReader(data)).getItem();
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException("Invalid item data \"" + data + "\"", e);
        }
    }
    
    @Override
    public Enchantment getEnchantment(String id) {
        return (Enchantment) ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.tryParse(id));
    }
    
    @Override
    public Set<Enchantment> getEnchantments() {
        return ForgeRegistries.ENCHANTMENTS.getEntries().stream().map(entry -> (Enchantment) entry.getValue()).collect(Collectors.toSet());
    }
}
