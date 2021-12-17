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

package com.dfsek.terra.fabric.mixin.implementations.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.fabric.util.FabricAdapter;


@Mixin(Block.class)
@Implements(@Interface(iface = BlockType.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class BlockMixin {
    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }
    
    public com.dfsek.terra.api.block.state.BlockState terra$getDefaultState() {
        return FabricAdapter.adapt(((Block) (Object) this).getDefaultState());
    }
    
    public boolean terra$isSolid() {
        return ((Block) (Object) this).getDefaultState().isOpaque();
    }
    
    @SuppressWarnings("ConstantConditions")
    public boolean terra$isWater() {
        return ((Object) this) == Blocks.WATER;
    }
}
