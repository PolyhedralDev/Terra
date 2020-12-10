package com.dfsek.terra.api.gaea.profiler;

import net.jafama.FastMath;
import net.md_5.bungee.api.ChatColor;

/**
 * Class to hold a profiler data value. Contains formatting method to highlight value based on desired range.
 */
public class DataHolder {
    private final long desired;
    private final DataType type;
    private final double desiredRangePercent;

    /**
     * Constructs a DataHolder with a DataType and a desired value, including a percentage around the desired value considered acceptable
     *
     * @param type                The type of data held in this instance.
     * @param desired             The desired value. This should be the average value of whatever is being measured.
     * @param desiredRangePercent The percentage around the desired value to be considered acceptable.
     */
    public DataHolder(DataType type, long desired, double desiredRangePercent) {
        this.desired = desired;
        this.type = type;
        this.desiredRangePercent = desiredRangePercent;
    }

    /**
     * Returns a String, formatted with Bungee ChatColors.<br>
     * GREEN if the value is better than desired and outside of acceptable range.<br>
     * YELLOW if the value is better or worse than desired, and within acceptable range.<br>
     * RED if the value is worse than desired and outside of acceptable range.<br>
     *
     * @param data The data to format.
     * @return String - The formatted data.
     */
    public String getFormattedData(long data) {
        double range = desiredRangePercent * desired;
        ChatColor color = ChatColor.YELLOW;
        if(FastMath.abs(data - desired) > range) {
            if(data > desired) color = type.getDesire().getHighColor();
            else color = type.getDesire().getLowColor();
        }
        return color + type.getFormatted(data) + ChatColor.RESET;
    }
}
