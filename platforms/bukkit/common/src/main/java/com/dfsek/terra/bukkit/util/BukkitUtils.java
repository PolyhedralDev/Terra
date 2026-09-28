package com.dfsek.terra.bukkit.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;

import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.entity.EntityTypeExtended;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;


public class BukkitUtils {
    private static volatile BukkitNMSAccess nmsAccess;

    public static boolean isLiquid(BlockData blockState) {
        Material material = blockState.getMaterial();
        return material == Material.WATER || material == Material.LAVA;
    }

    public static void setNMSAccess(BukkitNMSAccess access) {
        nmsAccess = access;
    }

    public static EntityType getEntityType(String id) {
        BukkitNMSAccess access = nmsAccess;
        return access == null ? getBukkitEntityType(id) : access.getEntityType(id);
    }

    public static org.bukkit.entity.Entity spawnEntity(Object region, org.bukkit.Location location, EntityType entityType) {
        if(entityType instanceof EntityTypeExtended) {
            BukkitNMSAccess access = nmsAccess;
            if(access == null) throw new IllegalStateException("Entity NBT loading requires Terra's NMS bindings");
            return access.spawnEntity(region, location, entityType);
        }

        org.bukkit.entity.EntityType bukkitType = ((BukkitEntityType) entityType).getHandle();
        if(region instanceof org.bukkit.generator.LimitedRegion limitedRegion) {
            return limitedRegion.spawnEntity(location, bukkitType);
        }
        return ((org.bukkit.World) region).spawnEntity(location, bukkitType);
    }

    public static void setSpawnerEntity(CreatureSpawner spawner, EntityTypeExtended type) {
        BukkitNMSAccess access = nmsAccess;
        if(access == null) throw new IllegalStateException("Entity NBT loading requires Terra's NMS bindings");
        access.setSpawnerEntity(spawner, type);
    }

    private static EntityType getBukkitEntityType(String data) {
        int nbtStart = data.indexOf('{');
        if(nbtStart >= 0) {
            throw new IllegalArgumentException("Entity NBT loading requires Terra's NMS bindings: " + data);
        }

        NamespacedKey key = NamespacedKey.fromString(data);
        org.bukkit.entity.EntityType type = key == null ? null : Registry.ENTITY_TYPE.get(key);
        if(type == null) throw new IllegalArgumentException("Invalid entity identifier " + data);
        return new BukkitEntityType(type);
    }
}
