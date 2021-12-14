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

package com.dfsek.terra.fabric.handle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.fabric.block.FabricBlockState;
import com.dfsek.terra.fabric.util.FabricAdapter;


public class FabricWorldHandle implements WorldHandle {
    
    private static final com.dfsek.terra.api.block.state.BlockState AIR = FabricAdapter.adapt(Blocks.AIR.getDefaultState());
    
    @Override
    public @NotNull FabricBlockState createBlockData(@NotNull String data) {
        BlockArgumentParser parser = new BlockArgumentParser(new StringReader(data), true);
        try {
            BlockState state = parser.parse(true).getBlockState();
            if(state == null) throw new IllegalArgumentException("Invalid data: " + data);
            return FabricAdapter.adapt(state);
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    @Override
    public com.dfsek.terra.api.block.state.@NotNull BlockState air() {
        return AIR;
    }
    
    @Override
    public @NotNull EntityType getEntity(@NotNull String id) {
        Identifier identifier = Identifier.tryParse(id);
        if(identifier == null) identifier = Identifier.tryParse(id);
        return (EntityType) Registry.ENTITY_TYPE.get(identifier);
    }
    
}
