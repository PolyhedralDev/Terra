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
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.command.argument.BlockArgumentParser.BlockResult;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.mod.implmentation.FabricEntityTypeExtended;

import static net.minecraft.command.argument.BlockArgumentParser.INVALID_BLOCK_ID_EXCEPTION;


public class MinecraftWorldHandle implements WorldHandle {


    private static final BlockState AIR = (BlockState) Blocks.AIR.getDefaultState();

    private static final Logger logger = LoggerFactory.getLogger(MinecraftWorldHandle.class);

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull BlockState createBlockState(@NotNull String data) {
        try {
            BlockResult blockResult = BlockArgumentParser.block(Registries.BLOCK, data, true);
            BlockState blockState;
            if(blockResult.nbt() != null) {
                net.minecraft.block.BlockState state = blockResult.blockState();
                NbtCompound nbtCompound = blockResult.nbt();
                if(state.hasBlockEntity()) {
                    BlockEntity blockEntity = ((BlockEntityProvider) state.getBlock()).createBlockEntity(new BlockPos(0, 0, 0), state);

                    nbtCompound.putInt("x", 0);
                    nbtCompound.putInt("y", 0);
                    nbtCompound.putInt("z", 0);

                    nbtCompound.put("id", BlockEntity.TYPE_CODEC, blockEntity.getType());

                    blockState = (BlockStateExtended) new BlockStateArgument(state, blockResult.properties().keySet(), nbtCompound);
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
            NbtCompound nbtData = null;
            StringReader reader = new StringReader(data);

            int i = reader.getCursor();

            identifier = Identifier.fromCommandInput(reader);

            net.minecraft.entity.EntityType<?> entity =
                (net.minecraft.entity.EntityType<?>) ((Reference<?>) Registries.ENTITY_TYPE.getOptional(
                    RegistryKey.of(RegistryKeys.ENTITY_TYPE, identifier)).orElseThrow(() -> {
                    reader.setCursor(i);
                    return INVALID_BLOCK_ID_EXCEPTION.createWithContext(reader, identifier.toString());
                })).value();

            if(reader.canRead() && reader.peek() == '{') {
                nbtData = StringNbtReader.readCompoundAsArgument(reader);
                nbtData.putString("id", entity.getRegistryEntry().registryKey().getValue().toString());
            }

            EntityType entityType;
            if(nbtData != null) {
                entityType = new FabricEntityTypeExtended(entity, nbtData);
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
