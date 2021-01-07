package com.dfsek.terra.bukkit.command.command.image;

import com.dfsek.terra.bukkit.command.WorldCommand;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.image.WorldImageGenerator;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class RenderCommand extends WorldCommand {
    public RenderCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        try {
            WorldImageGenerator g = new WorldImageGenerator(BukkitAdapter.adapt(world), Integer.parseInt(args[0]), Integer.parseInt(args[1]), getMain());
            g.drawWorld(sender.getLocation().getBlockX(), sender.getLocation().getBlockZ());
            File file = new File(getMain().getDataFolder() + File.separator + "export" + File.separator + "map" + File.separator + "map_" + System.currentTimeMillis() + ".png");
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            g.save(file);
            LangUtil.send("command.image.render.save", BukkitAdapter.adapt(sender), file.getAbsolutePath());
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            LangUtil.send("command.image.render.error", BukkitAdapter.adapt(sender));
            return true;
        }
    }

    @Override
    public String getName() {
        return "render";
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
        return Collections.emptyList();
    }
}
