package com.dfsek.terra.bukkit.command.command.geometry;

import com.dfsek.terra.api.math.FastNoiseLite;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.BukkitPlayer;
import com.dfsek.terra.bukkit.BukkitWorld;
import com.dfsek.terra.bukkit.command.PlayerCommand;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.procgen.voxel.DeformedSphere;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class DeformedSphereCommand extends PlayerCommand {
    public DeformedSphereCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int radius;
        try {
            radius = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            LangUtil.send("command.geometry.deform.invalid-radius", new BukkitCommandSender(sender), args[0]);
            return true;
        }
        double deform;
        try {
            deform = Double.parseDouble(args[1]);
        } catch(NumberFormatException e) {
            LangUtil.send("command.geometry.deform.invalid-deform", new BukkitCommandSender(sender), args[1]);
            return true;
        }

        double freq;
        try {
            freq = Float.parseFloat(args[2]);
        } catch(NumberFormatException e) {
            LangUtil.send("command.geometry.deform.invalid-frequency", new BukkitCommandSender(sender), args[2]);
            return true;
        }
        FastNoiseLite n = new FastNoiseLite((int) sender.getWorld().getSeed());
        n.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        n.setFrequency(freq);
        DeformedSphere sphere = new DeformedSphere(new BukkitPlayer(sender).getLocation().toVector(), radius, deform, n);
        for(Vector3 v : sphere.getGeometry()) {
            v.toLocation(new BukkitWorld(sender.getWorld())).getBlock().setBlockData(getMain().getWorldHandle().createBlockData("minecraft:stone"), false);
        }
        return true;
    }

    @Override
    public String getName() {
        return "deformedsphere";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 3;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
