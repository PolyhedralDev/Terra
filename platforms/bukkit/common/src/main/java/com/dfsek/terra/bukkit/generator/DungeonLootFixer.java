package com.dfsek.terra.bukkit.generator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Restores vanilla loot tables on dungeon chests (mob spawner rooms) when worldgen
 * mutations remove their loot-table metadata.
 */
public final class DungeonLootFixer {
    private DungeonLootFixer() {}

    public static void apply(@NotNull WorldInfo worldInfo,
                             @NotNull Random random,
                             int chunkX,
                             int chunkZ,
                             @NotNull LimitedRegion region,
                             int radiusXZ,
                             int radiusY,
                             @NotNull LootTable dungeonLoot,
                             boolean debug) {
        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;

        int minY = worldInfo.getMinHeight();
        int maxY = worldInfo.getMaxHeight() - 1;

        for(int dx = 0; dx < 16; dx++) {
            for(int dz = 0; dz < 16; dz++) {
                for(int y = minY; y <= maxY; y++) {
                    int x = baseX + dx;
                    int z = baseZ + dz;
                    if(!region.isInRegion(x, y, z)) continue;
                    if(region.getType(x, y, z) != Material.SPAWNER) continue;

                    fixNearby(region, random, radiusXZ, radiusY, dungeonLoot, debug, x, y, z);
                }
            }
        }
    }

    private static void fixNearby(@NotNull LimitedRegion region,
                                  @NotNull Random random,
                                  int radiusXZ,
                                  int radiusY,
                                  @NotNull LootTable dungeonLoot,
                                  boolean debug,
                                  int spawnerX,
                                  int spawnerY,
                                  int spawnerZ) {
        for(int x = spawnerX - radiusXZ; x <= spawnerX + radiusXZ; x++) {
            for(int z = spawnerZ - radiusXZ; z <= spawnerZ + radiusXZ; z++) {
                for(int y = spawnerY - radiusY; y <= spawnerY + radiusY; y++) {
                    if(!region.isInRegion(x, y, z)) continue;

                    Material type = region.getType(x, y, z);
                    if(type != Material.CHEST && type != Material.TRAPPED_CHEST) continue;

                    if(!shouldAssignLoot(region.getBlockData(x, y, z))) continue;

                    BlockState state = region.getBlockState(x, y, z);
                    if(!(state instanceof Lootable lootable)) continue;
                    if(lootable.getLootTable() != null) continue;

                    lootable.setLootTable(dungeonLoot);
                    try {
                        lootable.setSeed(random.nextLong());
                    } catch (NoSuchMethodError ignored) {
                        // Seed not required for correctness
                    }
                    state.update(true, false);

                    if(debug) {
                        Bukkit.getLogger().info("[Terra LootFix] Restored dungeon loot table at " + x + "," + y + "," + z);
                    }
                }
            }
        }
    }

    private static boolean shouldAssignLoot(@NotNull BlockData data) {
        if(data instanceof org.bukkit.block.data.type.Chest chest) {
            return chest.getType() != org.bukkit.block.data.type.Chest.Type.RIGHT;
        }
        return true;
    }

    public static @NotNull LootTable resolveDungeonLootTable(@NotNull String key) {
        // Prefer LootTables enum if possible.
        if("minecraft:chests/simple_dungeon".equalsIgnoreCase(key) || "chests/simple_dungeon".equalsIgnoreCase(key)) {
            try {
                return LootTables.SIMPLE_DUNGEON.getLootTable();
            } catch (Throwable ignored) {
                LootTable tbl = Bukkit.getLootTable(NamespacedKey.minecraft("chests/simple_dungeon"));
                if(tbl != null) return tbl;
            }
        }
        NamespacedKey nk = NamespacedKey.fromString(key);
        LootTable tbl = nk == null ? null : Bukkit.getLootTable(nk);
        if(tbl == null) {
            tbl = Bukkit.getLootTable(NamespacedKey.minecraft("chests/simple_dungeon"));
        }
        if(tbl == null) {
            // Should never happen on modern Paper.
            throw new IllegalStateException("Unable to resolve loot table: " + key);
        }
        return tbl;
    }
}
