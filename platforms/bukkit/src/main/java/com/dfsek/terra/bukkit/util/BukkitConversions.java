package com.dfsek.terra.bukkit.util;

import com.dfsek.terra.api.generic.world.vector.Vector3;
import org.bukkit.util.Vector;

public final class BukkitConversions {
    public static Vector3 toTerraVector(Vector bukkit) {
        return new Vector3(bukkit.getX(), bukkit.getY(), bukkit.getZ());
    }
}
