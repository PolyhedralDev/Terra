package com.dfsek.terra.bukkit.nms.v1_19_R3;

import com.dfsek.terra.bukkit.PlatformImpl;
import com.dfsek.terra.bukkit.nms.Initializer;

import org.bukkit.Bukkit;


public class NMSInitializer implements Initializer {
    @Override
    public void initialize(PlatformImpl platform) {
        AwfulBukkitHacks.registerBiomes(platform.getRawConfigRegistry());
        Bukkit.getPluginManager().registerEvents(new NMSInjectListener(), platform.getPlugin());
    }
}
