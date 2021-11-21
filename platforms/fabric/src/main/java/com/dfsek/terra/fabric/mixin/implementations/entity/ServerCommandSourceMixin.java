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

package com.dfsek.terra.fabric.mixin.implementations.entity;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.entity.CommandSender;


@Mixin(ServerCommandSource.class)
@Implements(@Interface(iface = CommandSender.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ServerCommandSourceMixin {
    @Shadow
    public abstract void sendFeedback(Text message, boolean broadcastToOps);
    
    public void terra$sendMessage(String message) {
        sendFeedback(new LiteralText(message), true);
    }
    
    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }
}
