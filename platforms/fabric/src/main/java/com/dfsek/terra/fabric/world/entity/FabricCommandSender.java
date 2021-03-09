package com.dfsek.terra.fabric.world.entity;

import com.dfsek.terra.api.platform.CommandSender;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class FabricCommandSender implements CommandSender {
    private final ServerCommandSource delegate;

    public FabricCommandSender(ServerCommandSource delegate) {
        this.delegate = delegate;
    }

    @Override
    public void sendMessage(String message) {
        delegate.sendFeedback(new LiteralText(message), true);
    }

    @Override
    public Object getHandle() {
        return delegate;
    }
}
