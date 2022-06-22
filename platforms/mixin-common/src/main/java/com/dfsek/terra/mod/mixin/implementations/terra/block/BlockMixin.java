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

package com.dfsek.terra.mod.mixin.implementations.terra.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

import com.dfsek.terra.api.block.BlockType;


@Mixin(Block.class)
@Implements(@Interface(iface = BlockType.class, prefix = "terra$"))
public abstract class BlockMixin {
    public com.dfsek.terra.api.block.state.BlockState terra$getDefaultState() {
        return (com.dfsek.terra.api.block.state.BlockState) ((Block) (Object) this).getDefaultState();
    }
    
    public boolean terra$isSolid() {
        return ((Block) (Object) this).getDefaultState().isOpaque();
    }
    
    @SuppressWarnings("ConstantConditions")
    public boolean terra$isWater() {
        return ((Object) this) == Blocks.WATER;
    }
}
