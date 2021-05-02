package com.dfsek.terra.bukkit.population;


import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.bukkit.world.BukkitWorld;

import java.io.File;

public class Gaea {
    private static boolean debug;

    public static File getGaeaFolder(World w) {
        File f = new File(((BukkitWorld) w).getWorldFolder(), "gaea");
        f.mkdirs();
        return f;
    }

    public static boolean isDebug() {
        return debug;
    }
}
