package com.dfsek.terra.util;

public final class StringUtils {
    /**
     * Strip Minecraft namespace from string
     *
     * @param in String to strip namespace of
     * @return Stripped string/
     */
    public static String stripMinecraftNamespace(String in) {
        if(!in.startsWith("minecraft:")) return in;
        return in.substring(10);
    }
}
