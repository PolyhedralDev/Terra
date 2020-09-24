package com.dfsek.terra;

import com.dfsek.terra.config.ConfigUtil;
import com.dfsek.terra.generation.TerraChunkGenerator;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileFormat;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class Terra extends JavaPlugin {
    private static FileConfiguration config;
    private static Terra instance;

    public static Terra getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        ConfigUtil.loadConfig(this);
        //getCommand("terra").setExecutor(new TerraCommand());

        PluginCommand command = getCommand("terra");
        command.setExecutor(new TerraCommand());
        if (CommodoreProvider.isSupported()) {
            Commodore commodore = CommodoreProvider.getCommodore(this);
            try {
                register(this, command, commodore);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else getLogger().severe("Brigadier is not properly supported! Commands will NOT work properly!");
        saveDefaultConfig();
        config = getConfig();
        instance = this;
    }

    public static void register(JavaPlugin plugin, Command pluginCommand, Commodore commodore) throws Exception {
        try (InputStream is = Terra.class.getResourceAsStream("/terra.commodore")) {
            if (is == null) {
                throw new Exception("Brigadier command data missing from jar");
            }

            LiteralCommandNode<?> commandNode = CommodoreFileFormat.parse(is);
            commodore.register(pluginCommand, commandNode, player -> player.hasPermission("terra.command"));
        }
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new TerraChunkGenerator();
    }
}
