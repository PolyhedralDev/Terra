/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.bukkit.util;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class FoliaUtil {
    private static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /*
     * Schedules a task to run on the global region.
     *
     * For non-Folia servers, runs on Bukkit scheduler.
     * For Folia servers, runs on the global region's scheduler.
     */
    @SuppressWarnings("deprecation")
    public static void runGlobalTask(Plugin plugin, Runnable task) {
        if (isFolia()) {
            Bukkit.getGlobalRegionScheduler().run(plugin, thisTask -> task.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    /*
     * Schedules a task to run on the global region, delayed.
     *
     * For non-Folia servers, runs on Bukkit scheduler.
     * For Folia servers, runs on the global region's scheduler.
     */
    @SuppressWarnings("deprecation")
    public static void runGlobalTaskDelayed(Plugin plugin, Runnable task, long delay) {
        if (isFolia()) {
            Bukkit.getGlobalRegionScheduler().runDelayed(plugin, thisTask -> task.run(), delay);
        } else {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, task, delay);
        }
    }

    /*
     * Schedules a task to run for the given location.
     *
     * For non-Folia servers, runs on Bukkit scheduler.
     * For Folia servers, runs on the location's region's scheduler.
     */
    @SuppressWarnings("deprecation")
    public static void runTaskAt(Plugin plugin, Runnable task, World world, int x, int z) {
        if (isFolia()) {
            Bukkit.getRegionScheduler().execute(plugin, world, x / 16, z / 16, task);
        } else {
            task.run();
        }
    }
}
