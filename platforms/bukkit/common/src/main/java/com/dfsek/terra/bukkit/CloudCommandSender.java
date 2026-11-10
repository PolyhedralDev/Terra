package com.dfsek.terra.bukkit;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Optional;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class CloudCommandSender implements CommandSender {
    private static final LegacyComponentSerializer LEGACY_AMPERSAND = LegacyComponentSerializer.builder()
        .character(LegacyComponentSerializer.AMPERSAND_CHAR)
        .hexColors()
        .useUnusualXRepeatedCharacterHexFormat()
        .build();

    private final CommandSourceStack delegate;

    public CloudCommandSender(CommandSourceStack delegate) {
        this.delegate = delegate;
    }

    @Override
    public void sendMessage(String message) {
        delegate.getSender().sendMessage(LEGACY_AMPERSAND.deserialize(message));
    }

    @Override
    public Optional<Entity> getEntity() {
        if(delegate.getExecutor() != null) {
            return Optional.of(BukkitAdapter.adapt(delegate.getExecutor()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Player> getPlayer() {
        if(delegate.getSender() instanceof org.bukkit.entity.Player player) {
            return Optional.of(BukkitAdapter.adapt(player));
        }
        return Optional.empty();
    }

    @Override
    public CommandSourceStack getHandle() {
        return delegate;
    }
}
