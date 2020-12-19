package com.dfsek.terra.bukkit.command.command.geometry;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.BukkitPlayer;
import com.dfsek.terra.bukkit.BukkitWorld;
import com.dfsek.terra.bukkit.command.PlayerCommand;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.procgen.voxel.Sphere;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SphereCommand extends PlayerCommand {
    public SphereCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int radius;
        try {
            radius = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            LangUtil.send("command.geometry.sphere.invalid-radius", new BukkitCommandSender(sender), args[0]);
            return true;
        }
        Sphere sphere = new Sphere(new BukkitPlayer(sender).getLocation().toVector(), radius);
        for(Vector3 v : sphere.getGeometry()) {
            v.toLocation(new BukkitWorld(sender.getWorld())).getBlock().setBlockData(getMain().getWorldHandle().createBlockData("minecraft:stone"), false);
        }
        return true;
    }

    @Override
    public String getName() {
        return "sphere";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
