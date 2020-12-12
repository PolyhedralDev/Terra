package com.dfsek.terra.bukkit.util;

import com.dfsek.terra.api.generic.world.vector.Vector3;
import com.dfsek.terra.bukkit.BukkitWorld;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class BukkitConversions {
    public static Vector3 toTerraVector(Vector bukkit) {
        return new Vector3(bukkit.getX(), bukkit.getY(), bukkit.getZ());
    }

    public static Location toBukkitLocation(com.dfsek.terra.api.generic.world.vector.Location terra) {
        return new Location(((BukkitWorld) terra.getWorld()).getHandle(), terra.getX(), terra.getY(), terra.getZ());
    }
}
