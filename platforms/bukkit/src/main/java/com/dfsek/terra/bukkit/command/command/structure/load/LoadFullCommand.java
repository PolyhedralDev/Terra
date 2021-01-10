package com.dfsek.terra.bukkit.command.command.structure.load;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.bukkit.command.DebugCommand;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.BukkitChunk;
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
        TerraWorld terraWorld = getMain().getWorld(BukkitAdapter.adapt(sender.getWorld()));
        long t = System.nanoTime();
        FastRandom chunk = PopulationUtil.getRandom(new BukkitChunk(sender.getLocation().getChunk()));
        Rotation r;
        try {
            r = Rotation.fromDegrees(Integer.parseInt(args[1]));
        } catch(Exception e) {
            sender.sendMessage("Invalid rotation: " + args[1]);
            return true;
        }
        StructureScript script = terraWorld.getConfig().getScriptRegistry().get(args[0]);
        if(script == null) {
            sender.sendMessage("Invalid structure: " + args[0]);
            return true;
        }
        if(this.chunk) {
            script.execute(BukkitAdapter.adapt(sender.getLocation()), BukkitAdapter.adapt(sender.getLocation().getChunk()), chunk, r);
        } else {
            script.execute(BukkitAdapter.adapt(sender.getLocation()), chunk, r);
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
        World w = BukkitAdapter.adapt(((Player) commandSender).getWorld());
        if(!TerraWorld.isTerraWorld(w)) return Collections.emptyList();
        switch(args.length) {
            case 1:
                return getMain().getWorld(w).getConfig().getScriptRegistry().entries().stream().map(StructureScript::getId).filter(id -> id.startsWith(args[0])).sorted().collect(Collectors.toList());
            case 2:
                return Stream.of("0", "90", "180", "270").filter(string -> string.toUpperCase().startsWith(args[1].toUpperCase())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
