package com.dfsek.terra.forge.world.entity;

import com.dfsek.terra.api.platform.CommandSender;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;

public class ForgeCommandSender implements CommandSender {
    private final CommandSource delegate;

    public ForgeCommandSender(CommandSource delegate) {
        this.delegate = delegate;
    }

    @Override
    public void sendMessage(String message) {
        delegate.sendSuccess(new StringTextComponent(message), true);
    }

    @Override
    public Object getHandle() {
        return delegate;
    }
}
