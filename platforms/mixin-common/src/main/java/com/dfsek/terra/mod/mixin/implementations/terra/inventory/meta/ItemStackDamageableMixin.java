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

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.inventory.item.Damageable;


@Mixin(ItemStack.class)
@Implements(@Interface(iface = Damageable.class, prefix = "terra$"))
public abstract class ItemStackDamageableMixin {
    @Shadow
    public abstract boolean isDamaged();
    
    @Shadow
    public abstract int getDamage();
    
    @Shadow
    public abstract void setDamage(int damage);
    
    @Intrinsic
    public int terra$getDamage() {
        return getDamage();
    }
    
    @Intrinsic
    public void terra$setDamage(int damage) {
        setDamage(damage);
    }
    
    public boolean terra$hasDamage() {
        return isDamaged();
    }
}
