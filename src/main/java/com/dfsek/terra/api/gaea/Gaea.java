package com.dfsek.terra.api.gaea;

import org.bukkit.World;

import java.io.File;

public class Gaea  {
    private static boolean debug;

    public static File getGaeaFolder(World w) {
        File f = new File(w.getWorldFolder(), "gaea");
        f.mkdirs();
        return f;
    }

    public static boolean isDebug() {
        return debug;
    }
}
