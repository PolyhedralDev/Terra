package com.dfsek.terra.bukkit;

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
    public Optional<Entity> getEntity() {
        if(delegate instanceof org.bukkit.entity.Entity entity) {
            return Optional.of(BukkitAdapter.adapt(entity));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Player> getPlayer() {
        if(delegate instanceof org.bukkit.entity.Player player) {
            return Optional.of(BukkitAdapter.adapt(player));
        }
        return Optional.empty();
    }

    @Override
    public CommandSourceStack getHandle() {
        return delegate;
    }
}
