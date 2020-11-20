package com.dfsek.terra.async;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.TerraBiomeGrid;
import com.dfsek.terra.config.genconfig.structure.StructureConfig;
import com.dfsek.terra.structure.Rotation;
import com.dfsek.terra.structure.Structure;
import it.unimi.dsi.util.XoRoShiRo128PlusPlusRandom;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Runnable to locate structures asynchronously
 */
public class AsyncStructureFinder extends AsyncFeatureFinder<StructureConfig> {
    public AsyncStructureFinder(TerraBiomeGrid grid, StructureConfig target, @NotNull Location origin, int startRadius, int maxRadius, Consumer<Vector> callback) {
        super(grid, target, origin, startRadius, maxRadius, callback);
        setSearchSize(target.getSpawn().getWidth() + 2 * target.getSpawn().getSeparation());
    }

    /**
     * Check if coordinate pair is a valid structure spawn
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Whether location is a valid spawn for StructureConfig
     */
    public boolean isValid(int x, int z, StructureConfig target) {
        World world = getWorld();
        Location spawn = target.getSpawn().getChunkSpawn(x, z, world.getSeed()).toLocation(world);
        if(!TerraWorld.getWorld(world).getConfig().getBiome((UserDefinedBiome) getGrid().getBiome(spawn)).getStructures().contains(target))
            return false;
        Random r2 = new XoRoShiRo128PlusPlusRandom(spawn.hashCode());
        Structure struc = target.getStructure(r2);
        Rotation rotation = Rotation.fromDegrees(r2.nextInt(4) * 90);
        for(int y = target.getSearchStart().get(r2); y > 0; y--) {
            if(!target.getBound().isInRange(y)) return false;
            spawn.setY(y);
            if(!struc.checkSpawns(spawn, rotation)) continue;
            return true;
        }
        return false;
    }

    @Override
    public Vector finalizeVector(Vector orig) {
        return getTarget().getSpawn().getNearestSpawn(orig.getBlockX() * getSearchSize(), orig.getBlockZ() * getSearchSize(), getWorld().getSeed());
    }
}
