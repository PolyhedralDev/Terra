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

package com.dfsek.terra.mod.mixin.implementations.terra.inventory.meta;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

import com.dfsek.terra.api.inventory.ItemStack;


@Mixin(Enchantment.class)
@Implements(@Interface(iface = com.dfsek.terra.api.inventory.item.Enchantment.class, prefix = "terra$"))
public abstract class EnchantmentMixin {
    @Shadow
    public abstract boolean isAcceptableItem(net.minecraft.item.ItemStack stack);
    
    @Shadow
    public abstract boolean canCombine(Enchantment other);
    
    @SuppressWarnings("ConstantConditions")
    public boolean terra$canEnchantItem(ItemStack itemStack) {
        return isAcceptableItem((net.minecraft.item.ItemStack) (Object) itemStack);
    }
    
    public boolean terra$conflictsWith(com.dfsek.terra.api.inventory.item.Enchantment other) {
        return !canCombine((Enchantment) other);
    }
    
    public String terra$getID() {
        return Objects.requireNonNull(Registry.ENCHANTMENT.getId((Enchantment) (Object) this)).toString();
    }
}
