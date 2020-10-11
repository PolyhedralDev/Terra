package com.dfsek.terra.command.geometry;

import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.procgen.voxel.DeformedSphere;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.PlayerCommand;
import org.polydev.gaea.math.FastNoise;

import java.util.Collections;
import java.util.List;

public class DeformedSphereCommand extends PlayerCommand {
    public DeformedSphereCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int radius;
        try {
            radius = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            LangUtil.send("command.geometry.deform.invalid-radius", sender, args[0]);
            return true;
        }
        double deform;
        try {
            deform = Double.parseDouble(args[1]);
        } catch(NumberFormatException e) {
            LangUtil.send("command.geometry.deform.invalid-deform", sender, args[1]);
            return true;
        }

        float freq;
        try {
            freq = Float.parseFloat(args[2]);
        } catch(NumberFormatException e) {
            LangUtil.send("command.geometry.deform.invalid-frequency", sender, args[2]);
            return true;
        }
        FastNoise n = new FastNoise((int) sender.getWorld().getSeed());
        n.setNoiseType(FastNoise.NoiseType.Simplex);
        n.setFrequency(freq);
        DeformedSphere sphere = new DeformedSphere(sender.getLocation().toVector(), radius, deform, n);
        for(Vector v : sphere.getGeometry()) {
            v.toLocation(sender.getWorld()).getBlock().setType(Material.STONE);
        }
        return true;
    }

    @Override
    public String getName() {
        return "deformedsphere";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
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
