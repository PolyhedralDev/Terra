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

package com.dfsek.terra.mod.mixin.implementations.terra.inventory;

import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

import com.dfsek.terra.api.inventory.Inventory;
import com.dfsek.terra.api.inventory.ItemStack;


@Mixin(LockableContainerBlockEntity.class)
@Implements(@Interface(iface = Inventory.class, prefix = "terra$"))
public class LockableContainerBlockEntityMixin {
    @SuppressWarnings("ConstantConditions")
    public void terra$setItem(int slot, ItemStack newStack) {
        ((LockableContainerBlockEntity) (Object) this).setStack(slot, (net.minecraft.item.ItemStack) (Object) newStack);
    }
    
    public int terra$getSize() {
        return ((LockableContainerBlockEntity) (Object) this).size();
    }
    
    @SuppressWarnings("ConstantConditions")
    public ItemStack terra$getItem(int slot) {
        net.minecraft.item.ItemStack itemStack = ((LockableContainerBlockEntity) (Object) this).getStack(slot);
        return itemStack.getItem() == Items.AIR ? null : (ItemStack) (Object) itemStack;
    }
}
