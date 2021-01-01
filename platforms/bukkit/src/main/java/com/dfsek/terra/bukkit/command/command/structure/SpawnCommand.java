package com.dfsek.terra.bukkit.command.command.structure;

import com.dfsek.terra.api.structures.parser.lang.constants.NumericConstant;
import com.dfsek.terra.api.structures.script.functions.CheckFunction;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.StructureBuffer;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.bukkit.command.DebugCommand;
import com.dfsek.terra.bukkit.command.WorldCommand;
import com.dfsek.terra.bukkit.world.BukkitWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SpawnCommand extends WorldCommand implements DebugCommand {
    public SpawnCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        Location p = sender.getLocation();
        int x = p.getBlockX();
        int y = p.getBlockY();
        int z = p.getBlockZ();
        Position dummy = new Position(0, 0);
        com.dfsek.terra.api.platform.world.World w = new BukkitWorld(world);
        String check = new CheckFunction(getMain(), new NumericConstant(0, dummy), new NumericConstant(0, dummy), new NumericConstant(0, dummy), getMain().getWorld(w).getConfig().getCheckCache(), dummy).apply(new StructureBuffer(
                new com.dfsek.terra.api.math.vector.Location(w, x, y, z)
        ), Rotation.NONE, new FastRandom(), 0);

        sender.sendMessage("Found: " + check);
        return true;
    }

    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
