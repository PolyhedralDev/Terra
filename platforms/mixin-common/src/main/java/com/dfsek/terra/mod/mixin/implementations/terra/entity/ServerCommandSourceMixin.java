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

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.Player;


@Mixin(ServerCommandSource.class)
@Implements(@Interface(iface = CommandSender.class, prefix = "terra$"))
public abstract class ServerCommandSourceMixin {
    @Shadow
    public abstract void sendFeedback(Text message, boolean broadcastToOps);
    
    @Shadow
    public abstract ServerPlayerEntity getPlayer() throws CommandSyntaxException;
    
    @Shadow
    @Nullable
    public abstract net.minecraft.entity.@Nullable Entity getEntity();
    
    public void terra$sendMessage(String message) {
        sendFeedback(Text.literal(message), true);
    }
    
    @Nullable
    public Optional<Entity> terra$getEntity() {
        return Optional.ofNullable((Entity) getEntity());
    }
    
    public Optional<Player> terra$getPlayer() {
        try {
            return Optional.ofNullable((Player) getPlayer());
        } catch(CommandSyntaxException e) {
            return Optional.empty();
        }
    }
}
