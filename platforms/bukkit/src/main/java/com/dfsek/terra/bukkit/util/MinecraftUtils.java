package com.dfsek.terra.bukkit.util;

public final class MinecraftUtils {
    public static String stripMinecraftNamespace(String in) {
        if(in.startsWith("minecraft:")) return in.substring("minecraft:".length());
        return in;
    }
}
