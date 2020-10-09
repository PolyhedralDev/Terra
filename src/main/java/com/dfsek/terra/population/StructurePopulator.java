package com.dfsek.terra.population;

import com.dfsek.terra.Debug;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.genconfig.StructureConfig;
import com.dfsek.terra.procgen.math.Vector2;
import com.dfsek.terra.structure.Structure;
import com.dfsek.terra.structure.StructureContainedInventory;
import com.dfsek.terra.util.structure.RotationUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.BlockInventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.structures.loot.LootTable;

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
            structure: for(StructureConfig conf : config.getAllStructures()) {
                Location spawn = conf.getSpawn().getNearestSpawn(cx + 8, cz + 8, world.getSeed()).toLocation(world);
                if(!config.getBiome((UserDefinedBiome) grid.getBiome(spawn)).getStructures().contains(conf)) continue;
                Random r2 = new Random(spawn.hashCode());
                Structure struc = conf.getStructure(r2);
                Structure.Rotation rotation = Structure.Rotation.fromDegrees(r2.nextInt(4) * 90);
                for(int y = conf.getSearchStart().get(r2); y > 0; y--) {
                    if(!conf.getBound().isInRange(y)) continue structure;
                    spawn.setY(y);
                    if(! struc.checkSpawns(spawn, rotation)) continue;
                    double horizontal = struc.getStructureInfo().getMaxHorizontal();
                    if(Math.abs((cx + 8) - spawn.getBlockX()) <= horizontal && Math.abs((cz + 8) - spawn.getBlockZ()) <= horizontal) {
                        try(ProfileFuture ignore = TerraProfiler.fromWorld(world).measure("StructurePasteTime")) {
                            struc.paste(spawn, chunk, rotation);
                            for(StructureContainedInventory i : struc.getInventories()) {
                                Debug.info("Attempting to populate loot: " + i.getUid());
                                Vector2 lootCoords = RotationUtil.getRotatedCoords(new Vector2(i.getX()-struc.getStructureInfo().getCenterX(), i.getZ()-struc.getStructureInfo().getCenterZ()), rotation);
                                Location inv = spawn.clone().add(lootCoords.getX(), 0, lootCoords.getZ());
                                Debug.info(spawn.toString() + " became: " + inv.toString());
                                Debug.info(Math.floorDiv(inv.getBlockX(), 16) + ":" + chunk.getX() + ", " + Math.floorDiv(inv.getBlockZ(), 16) + ":" + chunk.getZ() );
                                if(Math.floorDiv(inv.getBlockX(), 16) != chunk.getX() || Math.floorDiv(inv.getBlockZ(), 16) != chunk.getZ()) continue;
                                Debug.info("Target is in chunk.");
                                LootTable table = conf.getLoot(i.getUid());
                                if(table == null) continue;
                                Debug.info("Target has table assigned.");
                                table.fillInventory(((BlockInventoryHolder) inv.getBlock().getState()).getInventory(), random);
                            }
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
