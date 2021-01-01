package com.dfsek.terra.bukkit.command.command.structure;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.async.AsyncStructureFinder;
import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.bukkit.command.WorldCommand;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.BukkitWorld;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.generation.items.TerraStructure;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LocateCommand extends WorldCommand {
    private final boolean tp;

    public LocateCommand(com.dfsek.terra.bukkit.command.Command parent, boolean tp) {
        super(parent);
        this.tp = tp;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        String id = args[0];
        int maxRadius;

        try {
            maxRadius = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            LangUtil.send("command.structure.invalid-radius", new BukkitCommandSender(sender), args[1]);
            return true;
        }
        TerraStructure s;
        try {
            s = Objects.requireNonNull(getMain().getWorld(new BukkitWorld(world)).getConfig().getStructure(id));
        } catch(IllegalArgumentException | NullPointerException e) {
            //LangUtil.send("command.structure.invalid", sender, id);
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously((TerraBukkitPlugin) getMain(), new AsyncStructureFinder(getMain().getWorld(new BukkitWorld(world)).getGrid(), s, BukkitAdapter.adapt(sender.getLocation()), 0, maxRadius, (location) -> {
            if(sender.isOnline()) {
                if(location != null) {
                    ComponentBuilder cm = new ComponentBuilder(String.format("The nearest %s is at ", id.toLowerCase()))
                            .append(String.format("[%d, ~, %d]", location.getBlockX(), location.getBlockZ()), ComponentBuilder.FormatRetention.NONE)
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/minecraft:tp %s %d.0 %.2f %d.0", sender.getName(), location.getBlockX(), sender.getLocation().getY(), location.getBlockZ())))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {new TextComponent("Click to teleport")}))
                            .color(ChatColor.GREEN)
                            .append(String.format(" (%.1f blocks away)", location.add(new Vector3(0, sender.getLocation().getY(), 0)).distance(
                                    new Vector3(sender.getLocation().getX(), sender.getLocation().getY(), sender.getLocation().getZ()))), ComponentBuilder.FormatRetention.NONE);
                    sender.spigot().sendMessage(cm.create());
                } else
                    sender.sendMessage("Unable to locate structure. ");
            }
        }, getMain()));

        return true;
    }

    @Override
    public String getName() {
        return tp ? "teleport" : "locate";
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
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        /*
        if(!(sender instanceof Player) || !(((Player) sender).getWorld().getGenerator() instanceof TerraChunkGenerator))
            return Collections.emptyList();

        List<String> ids = ((TerraBukkitPlugin) getMain()).getWorld(((Player) sender).getWorld()).getConfig().getStructureIDs();
        if(args.length == 1)
            return ids.stream().filter(string -> string.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());

         */
        return Collections.emptyList();
    }
}
