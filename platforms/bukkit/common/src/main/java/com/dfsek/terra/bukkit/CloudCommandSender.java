package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.util.generic.data.types.Maybe;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.ChatColor;

import java.util.Optional;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class CloudCommandSender implements CommandSender {
    private final CommandSourceStack delegate;

    public CloudCommandSender(CommandSourceStack delegate) {
        this.delegate = delegate;
    }

    @Override
    public void sendMessage(String message) {
        delegate.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @Override
    public Maybe<Entity> entity() {
        if(delegate instanceof org.bukkit.entity.Entity entity) {
            return Maybe.just(BukkitAdapter.adapt(entity));
        }
        return Maybe.nothing();
    }

    @Override
    public Maybe<Player> player() {
        if(delegate instanceof org.bukkit.entity.Player player) {
            return Maybe.just(BukkitAdapter.adapt(player));
        }
        return Maybe.nothing();
    }

    @Override
    public CommandSourceStack getHandle() {
        return delegate;
    }
}
