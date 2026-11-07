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

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.api.inventory.item.ItemMeta;


@Mixin(ItemStack.class)
@Implements(@Interface(iface = ItemMeta.class, prefix = "terra$"))
public abstract class ItemStackMetaMixin {
    @Shadow
    public abstract boolean isEnchanted();

    @Shadow
    public abstract ItemEnchantments getEnchantments();

    @Shadow
    public abstract void enchant(Holder<net.minecraft.world.item.enchantment.Enchantment> enchantment, int level);

    public void terra$addEnchantment(Enchantment enchantment, int level) {
        ;
        enchant(Holder.direct((net.minecraft.world.item.enchantment.Enchantment) (Object) enchantment), level);
    }

    @Intrinsic(displace = true)
    public Map<Enchantment, Integer> terra$getEnchantments() {
        if(!isEnchanted()) return Collections.emptyMap();
        Map<Enchantment, Integer> map = new HashMap<>();

        ItemEnchantments enchantments = getEnchantments();
        enchantments.keySet().forEach(enchantment -> {
            net.minecraft.world.item.enchantment.Enchantment enchantmentValue = enchantment.value();
            map.put((Enchantment) (Object) enchantmentValue, enchantments.getLevel(Holder.direct(enchantmentValue)));
        });
        return map;
    }
}
