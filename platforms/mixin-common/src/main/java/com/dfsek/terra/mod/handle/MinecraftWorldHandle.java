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

package com.dfsek.terra.mod.handle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.commands.arguments.blocks.BlockStateParser.BlockResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.mod.implmentation.MinecraftEntityTypeExtended;

import static net.minecraft.commands.arguments.blocks.BlockStateParser.ERROR_UNKNOWN_BLOCK;


public class MinecraftWorldHandle implements WorldHandle {


    private static final BlockState AIR = (BlockState) Blocks.AIR.defaultBlockState();

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull BlockState createBlockState(@NotNull String data) {
        try {
            BlockResult blockResult = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK, data, true);
            BlockState blockState;
            if(blockResult.nbt() != null) {
                net.minecraft.world.level.block.state.BlockState state = blockResult.blockState();
                CompoundTag nbtCompound = blockResult.nbt();
                if(state.hasBlockEntity()) {
                    BlockEntity blockEntity = ((EntityBlock) state.getBlock()).newBlockEntity(new BlockPos(0, 0, 0), state);

                    nbtCompound.putInt("x", 0);
                    nbtCompound.putInt("y", 0);
                    nbtCompound.putInt("z", 0);

                    nbtCompound.store("id", BlockEntity.TYPE_CODEC, blockEntity.getType());

                    blockState = (BlockStateExtended) new BlockInput(state, blockResult.properties().keySet(), nbtCompound);
                } else {
                    blockState = (BlockState) state;
                }

            } else {
                blockState = (BlockState) blockResult.blockState();
            }

            if(blockState == null) throw new IllegalArgumentException("Invalid data: " + data);
            return blockState;
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public @NotNull BlockState air() {
        return AIR;
    }

    @Override
    public @NotNull EntityType getEntity(@NotNull String data) {
        try {
            Identifier identifier;
            CompoundTag nbtData = null;
            StringReader reader = new StringReader(data);

            int i = reader.getCursor();

            identifier = Identifier.read(reader);

            net.minecraft.world.entity.EntityType<?> entity =
                (net.minecraft.world.entity.EntityType<?>) ((Reference<?>) BuiltInRegistries.ENTITY_TYPE.get(
                    ResourceKey.create(Registries.ENTITY_TYPE, identifier)).orElseThrow(() -> {
                    reader.setCursor(i);
                    return ERROR_UNKNOWN_BLOCK.createWithContext(reader, identifier.toString());
                })).value();

            if(reader.canRead() && reader.peek() == '{') {
                nbtData = TagParser.parseCompoundAsArgument(reader);
                nbtData.putString("id", entity.builtInRegistryHolder().key().identifier().toString());
            }

            EntityType entityType;
            if(nbtData != null) {
                entityType = new MinecraftEntityTypeExtended(entity, nbtData);
            } else {
                entityType = (EntityType) entity;
            }

            if(identifier == null) throw new IllegalArgumentException("Invalid data: " + data);
            return entityType;
        } catch(CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
