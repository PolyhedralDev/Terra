package com.dfsek.terra.bukkit.command;

import com.dfsek.terra.bukkit.BukkitCommandSender;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * A command that must be executed by a player, in a Terra world.
 */
public abstract class WorldCommand extends PlayerCommand {
    public WorldCommand(Command parent) {
        super(parent);
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean execute(@NotNull Player sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.getWorld().getGenerator() instanceof ChunkGenerator) { // TODO: implementation
            return execute(sender, command, label, args, sender.getWorld());
        } else {
            getMain().getLanguage().send("command.world", new BukkitCommandSender(sender));
        }
        return true;
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender Player that executed command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @param world World in which command was executed
     * @return true if a valid command, otherwise false
     */
    public abstract boolean execute(@NotNull Player sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args, World world);
}
