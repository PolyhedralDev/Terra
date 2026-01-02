package com.dfsek.terra.bukkit.lootfix;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public final class LootFixSettings {
    public final boolean enabled;
    public final boolean debug;

    public final int radiusXZ;
    public final int radiusY;

    public final boolean dungeonsEnabled;
    public final String dungeonLootTableKey;

    public final boolean retroactiveEnabled;
    public final boolean retroactiveOnlyIfEmpty;

    public final boolean lootinEnabled;
    public final boolean lootinPreOpenFix;

    public final boolean commandsEnabled;
    public final int defaultScanRadiusBlocks;

    private LootFixSettings(boolean enabled,
                            boolean debug,
                            int radiusXZ,
                            int radiusY,
                            boolean dungeonsEnabled,
                            String dungeonLootTableKey,
                            boolean retroactiveEnabled,
                            boolean retroactiveOnlyIfEmpty,
                            boolean lootinEnabled,
                            boolean lootinPreOpenFix,
                            boolean commandsEnabled,
                            int defaultScanRadiusBlocks) {
        this.enabled = enabled;
        this.debug = debug;
        this.radiusXZ = radiusXZ;
        this.radiusY = radiusY;
        this.dungeonsEnabled = dungeonsEnabled;
        this.dungeonLootTableKey = dungeonLootTableKey;
        this.retroactiveEnabled = retroactiveEnabled;
        this.retroactiveOnlyIfEmpty = retroactiveOnlyIfEmpty;
        this.lootinEnabled = lootinEnabled;
        this.lootinPreOpenFix = lootinPreOpenFix;
        this.commandsEnabled = commandsEnabled;
        this.defaultScanRadiusBlocks = defaultScanRadiusBlocks;
    }

    public static LootFixSettings load(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        ConfigurationSection root = plugin.getConfig().getConfigurationSection("loot-fix");
        if(root == null) {
            return new LootFixSettings(false, false, 4, 2, true,
                "minecraft:chests/simple_dungeon", false, true, true, true, true, 256);
        }

        boolean enabled = root.getBoolean("enabled", true);
        boolean debug = root.getBoolean("debug", false);

        ConfigurationSection radius = root.getConfigurationSection("radius");
        int radiusXZ = radius != null ? radius.getInt("xz", 4) : 4;
        int radiusY = radius != null ? radius.getInt("y", 2) : 2;

        ConfigurationSection dungeons = root.getConfigurationSection("dungeons");
        boolean dEnabled = dungeons == null || dungeons.getBoolean("enabled", true);
        String lootTable = dungeons != null ? dungeons.getString("loot-table", "minecraft:chests/simple_dungeon")
            : "minecraft:chests/simple_dungeon";

        ConfigurationSection retro = root.getConfigurationSection("retroactive");
        boolean rEnabled = retro != null && retro.getBoolean("enabled", false);
        boolean onlyIfEmpty = retro == null || retro.getBoolean("only-if-empty", true);

        ConfigurationSection integrations = root.getConfigurationSection("integrations");
        ConfigurationSection lootin = integrations != null ? integrations.getConfigurationSection("lootin") : null;
        boolean lootinEnabled = lootin == null || lootin.getBoolean("enabled", true);
        boolean lootinPreOpen = lootin == null || lootin.getBoolean("pre-open-fix", true);

        ConfigurationSection commands = root.getConfigurationSection("commands");
        boolean commandsEnabled = commands == null || commands.getBoolean("enabled", true);
        int defaultScan = commands != null ? commands.getInt("default-scan-radius-blocks", 256) : 256;

        return new LootFixSettings(enabled, debug, Math.max(1, radiusXZ), Math.max(0, radiusY),
            dEnabled, lootTable, rEnabled, onlyIfEmpty, lootinEnabled, lootinPreOpen, commandsEnabled, defaultScan);
    }
}
