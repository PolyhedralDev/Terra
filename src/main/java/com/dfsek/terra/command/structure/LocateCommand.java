package com.dfsek.terra.command.structure;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.async.AsyncStructureFinder;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.command.type.WorldCommand;
import com.dfsek.terra.config.genconfig.StructureConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.generation.TerraChunkGenerator;
import com.dfsek.terra.procgen.GridSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.structures.UserDefinedStructure;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LocateCommand extends WorldCommand {
    private final boolean tp;
    public LocateCommand(boolean tp) {
        this.tp = tp;
    }
    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        String id = args[0];
        int maxRadius;
        try {
            maxRadius = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            LangUtil.send("command.structure.invalid-radius", sender, args[1]);
            return true;
        }
        StructureConfig s;
        try {
            s = Objects.requireNonNull(TerraWorld.getWorld(world).getConfig().getStructure(id));
        } catch(IllegalArgumentException | NullPointerException e) {
            LangUtil.send("command.structure.invalid", sender, id);
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Terra.getInstance(), new AsyncStructureFinder(TerraWorld.getWorld(world).getGrid(), s, sender, 0, maxRadius, tp));
        return true;
    }

    @Override
    public String getName() {
        return tp ? "teleport" : "locate";
    }

    @Override
    public List<com.dfsek.terra.command.type.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if(!(sender instanceof  Player) || !(((Player) sender).getWorld().getGenerator() instanceof TerraChunkGenerator)) return Collections.emptyList();
        List<String> ids = TerraWorld.getWorld(((Player) sender).getWorld()).getConfig().getStructureIDs();
        if(args.length == 1) return ids.stream().filter(string -> string.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
