package com.dfsek.terra.async;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.genconfig.StructureConfig;
import com.dfsek.terra.structure.GaeaStructure;
import com.dfsek.terra.structure.StructureSpawnRequirement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.profiler.ProfileFuture;

import java.util.Collections;
import java.util.Random;

public class AsyncStructureFinder implements Runnable {
    private final TerraBiomeGrid grid;
    private final StructureConfig target;
    private final Player p;
    private final int startRadius;
    private final int maxRadius;
    private final boolean tp;
    private final int centerX;
    private final int centerZ;
    private final long seed;
    private final World world;

    public AsyncStructureFinder(TerraBiomeGrid grid, StructureConfig target, Player p, int startRadius, int maxRadius, boolean tp) {
        this.grid = grid;
        this.target = target;
        this.p = p;
        this.startRadius = startRadius;
        this.maxRadius = maxRadius;
        this.tp = tp;
        this.centerX = p.getLocation().getBlockX();
        this.centerZ = p.getLocation().getBlockZ();
        this.seed = p.getWorld().getSeed();
        this.world = p.getWorld();
    }

    @Override
    public void run() {
        int x = centerX;
        int z = centerZ;

        int wid = target.getSpawn().getWidth() + 2*target.getSpawn().getSeparation();

        int run = 1;
        boolean toggle = true;
        boolean found = false;
        main: for(int i = startRadius; i < maxRadius; i++) {
            for(int j = 0; j < run; j++) {
                if(toggle) x += 16;
                else x -= 16;
                if(hasValidSpawn(x, z)) {
                    found = true;
                    break main;
                }
            }
            for(int j = 0; j < run; j++) {
                if(toggle) z += 16;
                else z -= 16;
                if(hasValidSpawn(x, z)) {
                    found = true;
                    break main;
                }
            }
            run++;
            toggle = !toggle;
        }
        if(found) {
            Vector v = target.getSpawn().getNearestSpawn(x, z, seed);
            x = v.getBlockX();
            z = v.getBlockZ();
            p.sendMessage("Located structure at (" + x + ", " + z + ").");
            if(tp) {
                int finalX = x;
                int finalZ = z;
                Bukkit.getScheduler().runTask(Terra.getInstance(), () -> p.teleport(new Location(p.getWorld(), finalX, p.getLocation().getY(), finalZ)));
            }
        } else if(p.isOnline()) p.sendMessage("Unable to locate structure.");
    }
    private boolean hasValidSpawn(int x, int z) {
        UserDefinedBiome b = (UserDefinedBiome) grid.getBiome(x, z, GenerationPhase.POPULATE);
        Location spawn = target.getSpawn().getNearestSpawn(x, z, seed).toLocation(world); // Probably(tm) async safe
        Random r2 = new Random(spawn.hashCode());
        GaeaStructure struc = target.getStructure(r2);
        main: for(int y = target.getSearchStart().get(r2); y > 0; y--) {
            if(y > target.getBound().getMax() || y < target.getBound().getMin()) return false;
            spawn.setY(y);
            for(StructureSpawnRequirement s : struc.getSpawns()) {
                if(! s.isValidSpawn(spawn)) continue main; // Probably(tm) async safe
                if(!b.equals(grid.getBiome(spawn.clone().add(s.getX(), s.getY(), s.getZ()), GenerationPhase.POPULATE))) return false;
            }
        }
        return true;
    }
}
