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

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;


public class MinecraftWorldHandle implements WorldHandle {


    private static final BlockState AIR = (BlockState) Blocks.AIR.getDefaultState();

    private static final Logger logger = LoggerFactory.getLogger(MinecraftWorldHandle.class);

    @Override
    public @NotNull BlockState createBlockState(@NotNull String data) {
        try {
            if(data.equals("minecraft:grass")) { //TODO: remove in 7.0
                data = "minecraft:short_grass";
                logger.warn(
                    "Translating minecraft:grass to minecraft:short_grass. In 1.20.3 minecraft:grass was renamed to minecraft:short_grass" +
                    ". You are advised to perform this rename in your config packs as this translation will be removed in the next major " +
                    "version of Terra.");
            }
            net.minecraft.block.BlockState state = BlockArgumentParser.block(Registries.BLOCK.getReadOnlyWrapper(), data, true)
                .blockState();
            if(state == null) throw new IllegalArgumentException("Invalid data: " + data);
            return (BlockState) state;
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public @NotNull BlockState air() {
        return AIR;
    }

    @Override
    public @NotNull EntityType getEntity(@NotNull String id) {
        if (!id.contains(":")) { //TODO: remove in 7.0
            String newid = "minecraft:" + id.toLowerCase();;
            logger.warn(
                "Translating " + id + " to " + newid + ". In 1.20.3 entity parsing was reworked" +
                ". You are advised to perform this rename in your config packs as this translation will be removed in the next major " +
                "version of Terra.");
            id = newid;
        }
        if(!id.contains(":")) throw new IllegalArgumentException("Invalid entity identifier " + id);
        Identifier identifier = Identifier.tryParse(id);
        if(identifier == null) identifier = Identifier.tryParse(id);
        return (EntityType) Registries.ENTITY_TYPE.get(identifier);
    }
}
