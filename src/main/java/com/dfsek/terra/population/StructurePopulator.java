package com.dfsek.terra.population;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.generation.items.TerraStructure;
import com.dfsek.terra.procgen.math.Vector2;
import com.dfsek.terra.structure.Rotation;
import com.dfsek.terra.structure.Structure;
import com.dfsek.terra.structure.StructureContainedInventory;
import com.dfsek.terra.structure.features.Feature;
import com.dfsek.terra.util.PopulationUtil;
import com.dfsek.terra.util.structure.RotationUtil;
import net.jafama.FastMath;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.BlockInventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.structures.loot.LootTable;
import org.polydev.gaea.util.FastRandom;

import java.util.Random;

public class StructurePopulator extends BlockPopulator {
    private final Terra main;

    public StructurePopulator(Terra main) {
        this.main = main;
    }

    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Random r, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("StructureTime")) {
            Random random = PopulationUtil.getRandom(chunk);
            int cx = (chunk.getX() << 4);
            int cz = (chunk.getZ() << 4);
            TerraWorld tw = main.getWorld(world);
            if(!tw.isSafe()) return;
            TerraBiomeGrid grid = tw.getGrid();
            ConfigPack config = tw.getConfig();
            structure:
            for(TerraStructure conf : config.getStructures()) {
                Location spawn = conf.getSpawn().getNearestSpawn(cx + 8, cz + 8, world.getSeed()).toLocation(world);
                if(!((UserDefinedBiome) grid.getBiome(spawn)).getConfig().getStructures().contains(conf)) continue;
                Random r2 = new FastRandom(spawn.hashCode());
                Structure struc = conf.getStructures().get(r2);
                Rotation rotation = Rotation.fromDegrees(r2.nextInt(4) * 90);
                for(int y = conf.getSpawnStart().get(r2); y > 0; y--) {
                    if(!conf.getBound().isInRange(y)) continue structure;
                    spawn.setY(y);
                    if(!struc.checkSpawns(spawn, rotation, main)) continue;
                    double horizontal = struc.getStructureInfo().getMaxHorizontal();
                    if(FastMath.abs((cx + 8) - spawn.getBlockX()) <= horizontal && FastMath.abs((cz + 8) - spawn.getBlockZ()) <= horizontal) {
                        struc.paste(spawn, chunk, rotation);
                        for(StructureContainedInventory i : struc.getInventories()) {
                            try {
                                Vector2 lootCoords = RotationUtil.getRotatedCoords(new Vector2(i.getX() - struc.getStructureInfo().getCenterX(), i.getZ() - struc.getStructureInfo().getCenterZ()), rotation.inverse());
                                Location inv = spawn.clone().add(lootCoords.getX(), i.getY(), lootCoords.getZ());
                                if(FastMath.floorDiv(inv.getBlockX(), 16) != chunk.getX() || FastMath.floorDiv(inv.getBlockZ(), 16) != chunk.getZ())
                                    continue;
                                LootTable table = conf.getLoot().get(i.getUid());
                                if(table == null) continue;
                                table.fillInventory(((BlockInventoryHolder) inv.getBlock().getState()).getInventory(), random);
                            } catch(ClassCastException e) {
                                Debug.error("Could not populate structure loot!");
                                Debug.stack(e);
                            }
                        }
                        for(Feature f : conf.getTemplate().getFeatures()) f.apply(struc, rotation, spawn, chunk); // Apply features.
                        break;
                    }
                }
            }
        }
    }
}
