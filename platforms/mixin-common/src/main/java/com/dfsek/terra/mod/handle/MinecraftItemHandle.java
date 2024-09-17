/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.mod.handle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.Impl;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.mod.CommonPlatform;


public class MinecraftItemHandle implements ItemHandle {

    @Override
    public Item createItem(String data) {
        try {
            return (Item) new ItemStackArgumentType(new CommandRegistryAccess() {
                @Override
                public Stream<RegistryKey<? extends Registry<?>>> streamAllRegistryKeys() {
                    return CommonPlatform.get().getServer().getRegistryManager().streamAllRegistryKeys();
                }

                @Override
                public <T> Optional<Impl<T>> getOptionalWrapper(RegistryKey<? extends Registry<? extends T>> registryRef) {
                    return Optional.of(CommonPlatform.get().getServer().getRegistryManager().getWrapperOrThrow(registryRef));
                }
            }).parse(new StringReader(data)).getItem();
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException("Invalid item data \"" + data + "\"", e);
        }
    }

    @Override
    public Enchantment getEnchantment(String id) {
        return (Enchantment) (Registries.ENCHANTMENT.get(Identifier.tryParse(id)));
    }

    @Override
    public Set<Enchantment> getEnchantments() {
        return Registries.ENCHANTMENT.stream().map(enchantment -> (Enchantment) enchantment).collect(Collectors.toSet());
    }
}
