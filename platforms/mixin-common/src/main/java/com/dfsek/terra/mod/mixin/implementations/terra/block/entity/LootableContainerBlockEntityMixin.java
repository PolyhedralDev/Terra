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

package com.dfsek.terra.mod.mixin.implementations.terra.block.entity;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.inventory.Inventory;


@Mixin(LootableContainerBlockEntity.class)
@Implements(@Interface(iface = Container.class, prefix = "terra$"))
public abstract class LootableContainerBlockEntityMixin extends BlockEntityMixin {
    public Inventory terra$getInventory() {
        return (Inventory) this;
    }
}
