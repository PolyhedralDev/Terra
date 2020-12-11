package com.dfsek.terra.population;

import com.dfsek.terra.api.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.api.generic.generator.TerraBlockPopulator;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class StructurePopulator implements TerraBlockPopulator {
    private final TerraBukkitPlugin main;

    public StructurePopulator(TerraBukkitPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Random r, @NotNull Chunk chunk) {
        /*
        TerraWorld tw = main.getWorld(world);
        try(ProfileFuture ignored = tw.getProfiler().measure("StructureTime")) {
            Random random = PopulationUtil.getRandom(chunk);
            int cx = (chunk.getX() << 4);
            int cz = (chunk.getZ() << 4);
            if(!tw.isSafe()) return;
            TerraBiomeGrid grid = tw.getGrid();
            ConfigPack config = tw.getConfig();
            structure:
            for(TerraStructure conf : config.getStructures()) {
                Location spawn = conf.getSpawn().getNearestSpawn(cx + 8, cz + 8, world.getSeed()).toLocation(world);
                if(!((UserDefinedBiome) grid.getBiome(spawn)).getConfig().getStructures().contains(conf))
                    continue;
                Random r2 = new FastRandom(spawn.hashCode());
                Structure struc = conf.getStructures().get(r2);
                Rotation rotation = Rotation.fromDegrees(r2.nextInt(4) * 90);
                for(int y = conf.getSpawnStart().get(r2); y > 0; y--) {
                    if(!conf.getBound().isInRange(y)) continue structure;
                    spawn.setY(y);
                    if(!struc.checkSpawns(spawn, rotation, main)) continue;
                    double horizontal = struc.getStructureInfo().getMaxHorizontal();
                    if(FastMath.abs((cx + 8) - spawn.getBlockX()) <= horizontal && FastMath.abs((cz + 8) - spawn.getBlockZ()) <= horizontal) {
                        struc.paste(spawn, chunk, rotation, main);
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

         */
    }
}
