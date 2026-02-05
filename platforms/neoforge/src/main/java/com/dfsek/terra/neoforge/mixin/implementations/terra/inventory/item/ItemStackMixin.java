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

package com.dfsek.terra.neoforge.mixin.implementations.terra.inventory.item;

import net.minecraft.component.Component;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.ItemMeta;


@Mixin(ItemStack.class)
@Implements(@Interface(iface = com.dfsek.terra.api.inventory.ItemStack.class, prefix = "terra$"))
public abstract class ItemStackMixin {
    @Shadow
    public abstract int getCount();

    @Shadow
    public abstract void setCount(int count);

    @Shadow
    public abstract net.minecraft.item.Item getItem();

    @Shadow
    public abstract boolean isDamageable();

    @Shadow
    public abstract ComponentMap getComponents();

    @Shadow
    @Final
    private ComponentMapImpl components;

    public int terra$getAmount() {
        return getCount();
    }

    public void terra$setAmount(int i) {
        setCount(i);
    }

    public Item terra$getType() {
        return (Item) getItem();
    }

    public ItemMeta terra$getItemMeta() {
        return (ItemMeta) this;
    }

    @SuppressWarnings("ConstantConditions")
    public void terra$setItemMeta(ItemMeta meta) {
        ComponentChanges.Builder builder = ComponentChanges.builder();
        this.getComponents().getTypes().forEach(builder::remove);

        ComponentMap components = ((ItemStack) (Object) meta).getComponents();
        components.forEach(builder::add);

        this.components.applyChanges(builder.build());
    }

    @Intrinsic
    public boolean terra$isDamageable() {
        return isDamageable();
    }
}
