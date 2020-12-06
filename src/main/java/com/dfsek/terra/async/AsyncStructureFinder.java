package com.dfsek.terra.async;

import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.TerraBiomeGrid;
import com.dfsek.terra.generation.items.TerraStructure;
import com.dfsek.terra.procgen.GridSpawn;
import com.dfsek.terra.structure.Rotation;
import com.dfsek.terra.structure.Structure;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.GaeaPlugin;
import org.polydev.gaea.util.FastRandom;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Runnable to locate structures asynchronously
 */
public class AsyncStructureFinder extends AsyncFeatureFinder<TerraStructure> {
    public AsyncStructureFinder(TerraBiomeGrid grid, TerraStructure target, @NotNull Location origin, int startRadius, int maxRadius, Consumer<Vector> callback, GaeaPlugin main) {
        super(grid, target, origin, startRadius, maxRadius, callback, main);
        setSearchSize(target.getSpawn().getWidth() + 2 * target.getSpawn().getSeparation());
    }

    /**
     * Check if coordinate pair is a valid structure spawn
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Whether location is a valid spawn for StructureConfig
     */
    public boolean isValid(int x, int z, TerraStructure target) {
        World world = getWorld();
        Location spawn = target.getSpawn().getChunkSpawn(x, z, world.getSeed()).toLocation(world);
        if(!((UserDefinedBiome) grid.getBiome(spawn)).getConfig().getStructures().contains(target)) return false;
        Random r2 = new FastRandom(spawn.hashCode());
        Structure struc = target.getStructures().get(r2);
        Rotation rotation = Rotation.fromDegrees(r2.nextInt(4) * 90);
        for(int y = target.getSpawnStart().get(r2); y > target.getBound().getMin(); y--) {
            if(!target.getBound().isInRange(y)) return false;
            spawn.setY(y);
            if(!struc.checkSpawns(spawn, rotation)) continue;
            return true;
        }
        return false;
    }

    @Override
    public Vector finalizeVector(Vector orig) {
        GridSpawn spawn = target.getSpawn();
        return spawn.getChunkSpawn(orig.getBlockX(), orig.getBlockZ(), world.getSeed());
    }
}
