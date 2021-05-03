package com.dfsek.terra.fabric.mixin.entity;

import com.dfsek.terra.api.platform.CommandSender;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerCommandSource.class)
@Implements(@Interface(iface = CommandSender.class, prefix = "vw$"))
public abstract class ServerCommandSourceMixin {
    @Shadow
    public abstract void sendFeedback(Text message, boolean broadcastToOps);

    public void vw$sendMessage(String message) {
        sendFeedback(new LiteralText(message), true);
    }

    public Object vw$getHandle() {
        return this;
    }
}
