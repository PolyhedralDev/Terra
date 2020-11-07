package com.dfsek.terra.async;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.genconfig.structure.StructureConfig;
import com.dfsek.terra.structure.Structure;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Runnable to locate structures asynchronously
 */
public class AsyncStructureFinder implements Runnable {
    private final TerraBiomeGrid grid;
    private final StructureConfig target;
    private final int startRadius;
    private final int maxRadius;
    private final int centerX;
    private final int centerZ;
    private final long seed;
    private final World world;
    private final Consumer<Vector> callback;

    public AsyncStructureFinder(TerraBiomeGrid grid, StructureConfig target, @NotNull Location origin, int startRadius, int maxRadius, Consumer<Vector> callback) {
        this.grid = grid;
        this.target = target;
        this.startRadius = startRadius;
        this.maxRadius = maxRadius;
        this.centerX = origin.getBlockX();
        this.centerZ = origin.getBlockZ();
        this.world = origin.getWorld();
        assert world != null;
        this.seed = world.getSeed();
        this.callback = callback;
    }

    @Override
    public void run() {
        int x = centerX;
        int z = centerZ;

        final int wid = target.getSpawn().getWidth() + 2 * target.getSpawn().getSeparation();
        x /= wid;
        z /= wid;

        int run = 1;
        boolean toggle = true;
        boolean found = false;
        Vector spawn = null;
        main:
        for(int i = startRadius; i < maxRadius; i++) {
            for(int j = 0; j < run; j++) {
                spawn = target.getSpawn().getChunkSpawn(x, z, seed);
                if(isValidSpawn(spawn.getBlockX(), spawn.getBlockZ())) {
                    found = true;
                    break main;
                }
                if(toggle) x += 1;
                else x -= 1;
            }
            for(int j = 0; j < run; j++) {
                spawn = target.getSpawn().getChunkSpawn(x, z, seed);
                if(isValidSpawn(spawn.getBlockX(), spawn.getBlockZ())) {
                    found = true;
                    break main;
                }
                if(toggle) z += 1;
                else z -= 1;
            }
            run++;
            toggle = !toggle;
        }
        final Vector finalSpawn = found ? spawn : null;
        Bukkit.getScheduler().runTask(Terra.getInstance(), () -> callback.accept(finalSpawn));
    }

    /**
     * Check if coordinate pair is a valid structure spawn
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Whether location is a valid spawn for StructureConfig
     */
    private boolean isValidSpawn(int x, int z) {
        Location spawn = target.getSpawn().getNearestSpawn(x, z, world.getSeed()).toLocation(world);
        if(!TerraWorld.getWorld(world).getConfig().getBiome((UserDefinedBiome) grid.getBiome(spawn)).getStructures().contains(target))
            return false;
        Random r2 = new Random(spawn.hashCode());
        Structure struc = target.getStructure(r2);
        Structure.Rotation rotation = Structure.Rotation.fromDegrees(r2.nextInt(4) * 90);
        for(int y = target.getSearchStart().get(r2); y > 0; y--) {
            if(!target.getBound().isInRange(y)) return false;
            spawn.setY(y);
            if(!struc.checkSpawns(spawn, rotation)) continue;
            return true;
        }
        return false;
    }
}
