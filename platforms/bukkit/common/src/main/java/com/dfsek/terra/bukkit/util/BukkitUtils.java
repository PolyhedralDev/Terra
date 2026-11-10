package com.dfsek.terra.bukkit.util;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.RegionAccessor;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.generator.LimitedRegion;

import java.math.BigDecimal;
import java.util.Locale;

import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.entity.EntityTypeExtended;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityTypeExtended;


public class BukkitUtils {
    public static boolean isLiquid(BlockData blockState) {
        Material material = blockState.getMaterial();
        return material == Material.WATER || material == Material.LAVA;
    }

    public static EntityType getEntityType(String data) {
        int nbtStart = data.indexOf('{');
        String id = nbtStart >= 0 ? data.substring(0, nbtStart) : data;

        NamespacedKey key = NamespacedKey.fromString(id.toLowerCase(Locale.ROOT));
        org.bukkit.entity.EntityType type = key == null ? null : RegistryAccess.registryAccess().getRegistry(RegistryKey.ENTITY_TYPE).get(key);
        if(type == null) throw new IllegalArgumentException("Invalid entity identifier " + data);
        if(nbtStart < 0) return new BukkitEntityType(type);

        String snbt = data.substring(nbtStart);
        createSnapshot(type, snbt, null); // fail on invalid SNBT while the pack loads
        return new BukkitEntityTypeExtended(type, snbt);
    }

    public static Entity spawnEntity(RegionAccessor region, Location location, EntityType entityType) {
        if(entityType instanceof BukkitEntityTypeExtended extended) {
            World world = region instanceof LimitedRegion limitedRegion ? limitedRegion.getWorld() : (World) region;
            // LimitedRegion only accepts entities already inside it.
            Entity entity = createSnapshot(extended.getHandle(), (String) extended.getData().getHandle(), location).createEntity(world);
            return region.addEntity(entity);
        }
        return region.spawnEntity(location, ((BukkitEntityType) entityType).getHandle());
    }

    public static void setSpawnerEntity(CreatureSpawner spawner, EntityTypeExtended type) {
        if(!(type instanceof BukkitEntityTypeExtended extended)) {
            throw new IllegalArgumentException("Expected a Bukkit entity type with NBT data");
        }
        spawner.setSpawnedEntity(createSnapshot(extended.getHandle(), (String) extended.getData().getHandle(), null));
    }

    private static EntitySnapshot createSnapshot(org.bukkit.entity.EntityType type, String snbt, Location position) {
        String body = snbt.strip();
        if(!body.startsWith("{") || !body.endsWith("}")) throw new IllegalArgumentException("Invalid entity NBT: " + snbt);
        body = body.substring(1, body.length() - 1).strip();

        StringBuilder compound = new StringBuilder("{id:\"").append(type.getKey()).append('"');
        if(position != null) {
            compound.append(",Pos:[").append(snbtDouble(position.getX())).append(',').append(snbtDouble(position.getY()))
                .append(',').append(snbtDouble(position.getZ())).append(']');
        }
        if(!body.isEmpty()) compound.append(',').append(body);
        compound.append('}');

        return Bukkit.getEntityFactory().createEntitySnapshot(compound.toString());
    }

    private static String snbtDouble(double value) {
        return BigDecimal.valueOf(value).toPlainString() + "d";
    }
}
