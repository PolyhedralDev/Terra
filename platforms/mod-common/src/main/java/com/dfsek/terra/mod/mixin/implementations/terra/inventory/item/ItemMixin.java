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

package com.dfsek.terra.mod.mixin.implementations.terra.inventory.item;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.inventory.ItemStack;


@Mixin(Item.class)
@Implements(@Interface(iface = com.dfsek.terra.api.inventory.Item.class, prefix = "terra$"))
public abstract class ItemMixin {
    @Shadow
    public abstract int getMaxDamage();
    
    @SuppressWarnings("ConstantConditions")
    public ItemStack terra$newItemStack(int amount) {
        return (ItemStack) (Object) new net.minecraft.item.ItemStack((Item) (Object) this, amount);
    }
    
    public double terra$getMaxDurability() {
        return getMaxDamage();
    }
}
