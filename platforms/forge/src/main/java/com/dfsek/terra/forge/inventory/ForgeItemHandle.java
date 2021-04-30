package com.dfsek.terra.forge.inventory;

import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.inventory.Item;
import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.forge.world.ForgeAdapter;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.Set;
import java.util.stream.Collectors;

public class ForgeItemHandle implements ItemHandle {

    @Override
    public Item createItem(String data) {
        try {
            return ForgeAdapter.adapt(new ItemArgument().parse(new StringReader(data)).getItem());
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException("Invalid item data \"" + data + "\"", e);
        }
    }

    @Override
    public Enchantment getEnchantment(String id) {
        return ForgeAdapter.adapt(Registry.ENCHANTMENT.get(ResourceLocation.tryParse(id)));
    }

    @Override
    public Set<Enchantment> getEnchantments() {
        return Registry.ENCHANTMENT.stream().map(ForgeAdapter::adapt).collect(Collectors.toSet());
    }
}
