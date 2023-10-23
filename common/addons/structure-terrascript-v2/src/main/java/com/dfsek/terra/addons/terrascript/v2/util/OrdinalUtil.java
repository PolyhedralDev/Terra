package com.dfsek.terra.addons.terrascript.v2.util;

public class OrdinalUtil {
    public static String ordinalOf(int i) {
        String[] suffixes = new String[]{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        return switch(i % 100) {
            case 11, 12, 13 -> i + "th";
            default -> i + suffixes[i % 10];
        };
    }
}
