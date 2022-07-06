package com.dfsek.terra.bukkit.nms.v1_19_R1;

import org.bukkit.Bukkit;

import com.dfsek.terra.bukkit.PlatformImpl;
import com.dfsek.terra.bukkit.nms.Initializer;


public class NMSInitializer implements Initializer {
    @Override
    public void initialize(PlatformImpl platform) {
        AwfulBukkitHacks.registerBiomes(platform.getRawConfigRegistry());
        Bukkit.getPluginManager().registerEvents(new NMSInjectListener(), platform.getPlugin());
    }
}
