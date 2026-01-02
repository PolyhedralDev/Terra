package com.dfsek.terra.bukkit.lootfix;

import com.dfsek.terra.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.bukkit.generator.DungeonLootFixer;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.event.Listener;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public final class LootFixService {
    private final TerraBukkitPlugin plugin;
    private final LootFixSettings settings;
    private final LootTable dungeonLoot;

    public LootFixService(@NotNull TerraBukkitPlugin plugin, @NotNull LootFixSettings settings) {
        this.plugin = plugin;
        this.settings = settings;
        this.dungeonLoot = DungeonLootFixer.resolveDungeonLootTable(settings.dungeonLootTableKey);
    }

    public LootFixSettings settings() {
        return settings;
    }

    public boolean isLootinPresent() {
        return plugin.getServer().getPluginManager().getPlugin("Lootin") != null
            && plugin.getServer().getPluginManager().isPluginEnabled("Lootin");
    }

    public void registerListeners() {
        PluginManager pm = plugin.getServer().getPluginManager();

        if(settings.enabled && settings.retroactiveEnabled) {
            pm.registerEvents(new LootFixChunkListener(this), plugin);
        }
        if(settings.enabled && settings.lootinEnabled && settings.lootinPreOpenFix && isLootinPresent()) {
            pm.registerEvents(new LootFixPreOpenListener(this), plugin);
        }
    }

    public void applyDuringWorldgen(@NotNull WorldInfo worldInfo,
                                    @NotNull Random random,
                                    int chunkX,
                                    int chunkZ,
                                    @NotNull LimitedRegion region) {
        if(!settings.enabled) return;
        if(settings.dungeonsEnabled) {
            DungeonLootFixer.apply(worldInfo, random, chunkX, chunkZ, region,
                settings.radiusXZ, settings.radiusY, dungeonLoot, settings.debug);
        }
    }

    public int fixChunk(@NotNull Chunk chunk, @NotNull Random random, boolean onlyIfEmpty) {
        if(!settings.enabled || !settings.dungeonsEnabled) return 0;

        int fixed = 0;

        // Prefer tile entities for speed.
        for(BlockState te : chunk.getTileEntities()) {
            if(te.getType() != Material.SPAWNER) continue;

            int sx = te.getX();
            int sy = te.getY();
            int sz = te.getZ();

            fixed += fixNearbyDungeonChests(chunk.getWorld(), random, sx, sy, sz, onlyIfEmpty);
        }
        return fixed;
    }

    private int fixNearbyDungeonChests(@NotNull World world,
                                       @NotNull Random random,
                                       int spawnerX,
                                       int spawnerY,
                                       int spawnerZ,
                                       boolean onlyIfEmpty) {
        int fixed = 0;

        for(int x = spawnerX - settings.radiusXZ; x <= spawnerX + settings.radiusXZ; x++) {
            for(int z = spawnerZ - settings.radiusXZ; z <= spawnerZ + settings.radiusXZ; z++) {
                for(int y = spawnerY - settings.radiusY; y <= spawnerY + settings.radiusY; y++) {
                    Material type = world.getBlockAt(x, y, z).getType();
                    if(type != Material.CHEST && type != Material.TRAPPED_CHEST) continue;

                    BlockState state = world.getBlockAt(x, y, z).getState();
                    if(!(state instanceof Lootable lootable)) continue;
                    if(lootable.getLootTable() != null) continue;

                    if(onlyIfEmpty && state instanceof Container container) {
                        if(!isInventoryEmpty(container)) continue;
                    }

                    lootable.setLootTable(dungeonLoot);
                    try {
                        lootable.setSeed(random.nextLong());
                    } catch (NoSuchMethodError ignored) {
                        // Seed not required.
                    }
                    state.update(true, false);
                    fixed++;

                    if(settings.debug) {
                        plugin.getLogger().info("[Terra LootFix] Restored dungeon loot at " + world.getName() +
                            " " + x + "," + y + "," + z);
                    }
                }
            }
        }

        return fixed;
    }

    private static boolean isInventoryEmpty(@NotNull Container container) {
        var inv = container.getInventory();
        for(var item : inv.getContents()) {
            if(item != null && item.getType() != Material.AIR) return false;
        }
        return true;
    }
}
