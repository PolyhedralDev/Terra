package com.dfsek.terra.command.structure;

import com.dfsek.terra.structure.StructureSpawnRequirement;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.DebugCommand;
import org.polydev.gaea.command.WorldCommand;

import java.util.Collections;
import java.util.List;

public class SpawnCommand extends WorldCommand implements DebugCommand {
    public SpawnCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        Location p = sender.getLocation();
        int x = p.getBlockX();
        int y = p.getBlockY();
        int z = p.getBlockZ();
        boolean air = StructureSpawnRequirement.AIR.getInstance(world).matches(x, y, z);
        boolean ground = StructureSpawnRequirement.LAND.getInstance(world).matches(x, y, z);
        boolean sea = StructureSpawnRequirement.OCEAN.getInstance(world).matches(x, y, z);

        sender.sendMessage("AIR: " + air + "\nLAND: " + ground + "\nOCEAN: " + sea);
        return true;
    }

    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
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
