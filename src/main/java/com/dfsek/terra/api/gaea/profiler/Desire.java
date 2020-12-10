package com.dfsek.terra.api.gaea.profiler;

import net.md_5.bungee.api.ChatColor;

/**
 * Enum to represent the "goal" of a value, whether it is desirable for the value to be high (e.g. Frequency), or low (e.g. Period)
 */
public enum Desire {
    LOW(ChatColor.RED, ChatColor.GREEN), HIGH(ChatColor.GREEN, ChatColor.RED);

    private final ChatColor high;
    private final ChatColor low;

    Desire(ChatColor high, ChatColor low) {
        this.high = high;
        this.low = low;
    }

    /**
     * Gets the color to display when the numerical value is higher than desired.
     *
     * @return ChatColor - color of the value.
     */
    public ChatColor getHighColor() {
        return high;
    }

    /**
     * Gets the color to display when the numerical value is lower than desired.
     *
     * @return ChatColor - color of the value.
     */
    public ChatColor getLowColor() {
        return low;
    }
}
