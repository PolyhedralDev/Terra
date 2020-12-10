package com.dfsek.terra.api.gaea.command;

import com.dfsek.terra.api.gaea.generation.GaeaChunkGenerator;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A command that must be executed by a player, in a Terra world.
 */
public abstract class WorldCommand extends PlayerCommand {
    public WorldCommand(com.dfsek.terra.api.gaea.command.Command parent) {
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
    public final boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Class<? extends GaeaChunkGenerator> clazz = getMain().getGeneratorClass();
        if(clazz.isInstance(sender.getWorld().getGenerator())) {
            return execute(sender, command, label, args, sender.getWorld());
        } else {
            getMain().getLanguage().send("command.world", sender);
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
    public abstract boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world);
}
