package com.dfsek.terra.command.structure;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.async.AsyncStructureFinder;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.generation.TerraChunkGenerator;
import com.dfsek.terra.generation.items.TerraStructure;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.WorldCommand;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LocateCommand extends WorldCommand {
    private final boolean tp;

    public LocateCommand(org.polydev.gaea.command.Command parent, boolean tp) {
        super(parent);
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
        TerraStructure s;
        try {
            s = Objects.requireNonNull(TerraWorld.getWorld(world).getConfig().getStructure(id));
        } catch(IllegalArgumentException | NullPointerException e) {
            LangUtil.send("command.structure.invalid", sender, id);
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(getMain(), new AsyncStructureFinder(TerraWorld.getWorld(world).getGrid(), s, sender.getLocation(), 0, maxRadius, (location) -> {
            if(sender.isOnline()) {
                if(location != null) {
                    sender.sendMessage("Located structure at (" + location.getBlockX() + ", " + location.getBlockZ() + ").");
                    if(tp) {
                        int finalX = location.getBlockX();
                        int finalZ = location.getBlockZ();
                        Bukkit.getScheduler().runTask(getMain(), () -> sender.teleport(new Location(sender.getWorld(), finalX, sender.getLocation().getY(), finalZ)));
                    }
                } else sender.sendMessage("Unable to locate structure. ");
            }
        }, getMain()));
        return true;
    }

    @Override
    public String getName() {
        return tp ? "teleport" : "locate";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if(!(sender instanceof Player) || !(((Player) sender).getWorld().getGenerator() instanceof TerraChunkGenerator))
            return Collections.emptyList();
        List<String> ids = TerraWorld.getWorld(((Player) sender).getWorld()).getConfig().getStructureIDs();
        if(args.length == 1)
            return ids.stream().filter(string -> string.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
