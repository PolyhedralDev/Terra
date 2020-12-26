package com.dfsek.terra.bukkit.command.command.structure.load;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.bukkit.BukkitChunk;
import com.dfsek.terra.bukkit.BukkitWorld;
import com.dfsek.terra.bukkit.command.DebugCommand;
import com.dfsek.terra.util.PopulationUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoadFullCommand extends LoadCommand implements DebugCommand {
    private final boolean chunk;

    public LoadFullCommand(com.dfsek.terra.bukkit.command.Command parent, boolean chunk) {
        super(parent);
        this.chunk = chunk;
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        TerraWorld terraWorld = getMain().getWorld(new BukkitWorld(sender.getWorld()));
        long t = System.nanoTime();
        FastRandom chunk = PopulationUtil.getRandom(new BukkitChunk(sender.getLocation().getChunk()));

        if(this.chunk) {
            terraWorld.getConfig().getScriptRegistry().get(args[0]).execute(new Location(new BukkitWorld(sender.getWorld()), sender.getLocation().getX(), sender.getLocation().getY(), sender.getLocation().getZ()), new BukkitChunk(sender.getLocation().getChunk()), chunk, Rotation.fromDegrees(90 * chunk.nextInt(4)));
        } else {
            terraWorld.getConfig().getScriptRegistry().get(args[0]).execute(new Location(new BukkitWorld(sender.getWorld()), sender.getLocation().getX(), sender.getLocation().getY(), sender.getLocation().getZ()), chunk, Rotation.fromDegrees(90 * chunk.nextInt(4)));
        }
        long l = System.nanoTime() - t;

        sender.sendMessage("Took " + ((double) l) / 1000000 + "ms");
        return true;
    }

    @Override
    public String getName() {
        return chunk ? "chunk" : "full";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        switch(args.length) {
            case 1:
                return Collections.emptyList(); //getStructureNames().stream().filter(string -> string.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());
            case 2:
                return Stream.of("0", "90", "180", "270").filter(string -> string.toUpperCase().startsWith(args[1].toUpperCase())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
