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

package com.dfsek.terra.mod.mixin.implementations.terra.entity;

import com.dfsek.terra.api.util.generic.data.types.Maybe;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.Player;


@Mixin(ServerCommandSource.class)
@Implements(@Interface(iface = CommandSender.class, prefix = "terra$"))
public abstract class ServerCommandSourceMixin {
    @Shadow
    public abstract ServerPlayerEntity getPlayer() throws CommandSyntaxException;

    @Shadow
    @Nullable
    public abstract net.minecraft.entity.@Nullable Entity getEntity();

    @Shadow
    public abstract void sendMessage(Text message);

    public void terra$sendMessage(String message) {
        sendMessage(Text.literal(message));
    }

    @Nullable
    public Maybe<Entity> terra$entity() {
        return Maybe.ofNullable((Entity) getEntity());
    }

    public Maybe<Player> terra$player() {
        try {
            return Maybe.ofNullable((Player) getPlayer());
        } catch(CommandSyntaxException e) {
            return Maybe.nothing();
        }
    }
}
