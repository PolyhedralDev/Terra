package com.dfsek.terra.async;

import com.dfsek.terra.Terra;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GenerationPhase;

/**
 * Runnable that locates a biome asynchronously
 */
public class AsyncBiomeFinder implements Runnable {
    private final TerraBiomeGrid grid;
    private final int centerX;
    private final int centerZ;
    private final int startRadius;
    private final int maxRadius;
    private final Biome target;
    private final Player p;
    private final boolean tp;

    public AsyncBiomeFinder(TerraBiomeGrid grid, Biome target, Player p, int startRadius, int maxRadius, boolean tp) {
        this.grid = grid;
        this.target = target;
        this.p = p;
        this.centerX = p.getLocation().getBlockX();
        this.centerZ = p.getLocation().getBlockZ();
        this.startRadius = startRadius;
        this.maxRadius = maxRadius;
        this.tp = tp;
    }

    @Override
    public void run() {
        int x = centerX;
        int z = centerZ;
        int run = 1;
        boolean toggle = true;
        main:
        for(int i = startRadius; i < maxRadius; i++) {
            for(int j = 0; j < run; j++) {
                if(checkBiome(x, z).equals(target)) break main;
                if(toggle) x++;
                else x--;
            }
            for(int j = 0; j < run; j++) {
                if(checkBiome(x, z).equals(target)) break main;
                if(toggle) z++;
                else z--;
            }
            run++;
            toggle = !toggle;
        }
        if(p.isOnline()) {
            if(checkBiome(x, z).equals(target)) {
                LangUtil.send("command.biome.biome-found", p, String.valueOf(x), String.valueOf(z));
                if(tp) {
                    int finalX = x;
                    int finalZ = z;
                    Bukkit.getScheduler().runTask(Terra.getInstance(), () -> p.teleport(new Location(p.getWorld(), finalX, p.getLocation().getY(), finalZ)));
                }
            } else LangUtil.send("command.biome.unable-to-locate", p);
        }
    }

    /**
     * Helper method to get biome at location
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Biome at coordinates
     */
    private Biome checkBiome(int x, int z) {
        return grid.getBiome(x, z, GenerationPhase.POST_GEN);
    }
}
