package com.dfsek.terra.bukkit.nms.v1_21_7;

import com.dfsek.terra.bukkit.BukkitAddon;

import org.bukkit.Bukkit;

import com.dfsek.terra.bukkit.PlatformImpl;
import com.dfsek.terra.bukkit.nms.Initializer;


public class NMSInitializer implements Initializer {
    @Override
    public void initialize(PlatformImpl platform) {
        AwfulBukkitHacks.registerBiomes(platform);
        Bukkit.getPluginManager().registerEvents(new NMSInjectListener(), platform.getPlugin());
    }

    @Override
    public BukkitAddon getNMSAddon(PlatformImpl plugin) {
        return new NMSAddon(plugin);
    }
}
