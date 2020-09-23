package com.dfsek.terra;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class WorldEditUtil {
    public static WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        Bukkit.getLogger().severe("[Terra] a command requiring WorldEdit was executed, but WorldEdit was not detected!");
        return null;
    }
}
