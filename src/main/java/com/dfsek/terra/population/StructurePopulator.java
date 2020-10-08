package com.dfsek.terra.population;

import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.genconfig.StructureConfig;
import com.dfsek.terra.structure.GaeaStructure;
import com.dfsek.terra.structure.StructureSpawnRequirement;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.profiler.ProfileFuture;

import java.util.Collections;
import java.util.Random;

public class StructurePopulator extends BlockPopulator {

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("StructureTime")) {
            int cx = (chunk.getX() << 4);
            int cz = (chunk.getZ() << 4);
            TerraWorld tw = TerraWorld.getWorld(world);
            if(!tw.isSafe()) return;
            TerraBiomeGrid grid = tw.getGrid();
            ConfigPack config = tw.getConfig();
            UserDefinedBiome b = (UserDefinedBiome) grid.getBiome(cx+ 8, cz + 8, GenerationPhase.POPULATE);
            structure: for(StructureConfig conf : config.getBiome(b).getStructures()) {
                Location spawn = conf.getSpawn().getNearestSpawn(cx + 8, cz + 8, world.getSeed()).toLocation(world);
                Random r2 = new Random(spawn.hashCode());
                GaeaStructure struc = conf.getStructure(r2);
                GaeaStructure.Rotation rotation = GaeaStructure.Rotation.fromDegrees(r2.nextInt(4) * 90);
                main: for(int y = conf.getSearchStart().get(r2); y > 0; y--) {
                    if(y > conf.getBound().getMax() || y < conf.getBound().getMin()) continue structure;
                    spawn.setY(y);
                    if(!struc.checkSpawns(spawn, rotation)) continue;
                    double horizontal = struc.getStructureInfo().getMaxHorizontal();
                    if(Math.abs((cx + 8) - spawn.getBlockX()) <= horizontal && Math.abs((cz + 8) - spawn.getBlockZ()) <= horizontal) {
                        try(ProfileFuture ignore = TerraProfiler.fromWorld(world).measure("StructurePasteTime")) {
                            struc.paste(spawn, chunk, rotation);
                            break;
                        }
                    }
                }
            }
        }
    }
    public enum SearchType {
        UP, DOWN
    }
}
