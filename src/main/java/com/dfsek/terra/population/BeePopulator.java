package com.dfsek.terra.population;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.population.AsyncGaeaBlockPopulator;
import org.polydev.gaea.population.AsyncPopulationReturn;
import org.polydev.gaea.population.BlockCoordinate;
import org.polydev.gaea.util.GlueList;

import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BeePopulator extends AsyncGaeaBlockPopulator {

    Material material = Material.BEE_NEST;
    private final BlockData beeHive = material.createBlockData();
    private static final BlockFace[] possibleFaces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

    @Override
    public CompletableFuture<AsyncPopulationReturn> populate(@NotNull World world, @NotNull Random random, Chunk chunk, int i) {
        CompletableFuture completableFuture = new CompletableFuture<>();
        completableFuture.runAsync(() -> {doBees(world.getUID(), random, snapshot, i);});
        return completableFuture;
    }

    private CompletableFuture<AsyncPopulationReturn> doBees (@NotNull UUID worldUUID, @NotNull Random random, ChunkSnapshot chunk, int i) {
        GlueList<Location> logsInChunk = getLogsInChunk(chunk);
        HashSet<BlockCoordinate> changeList = new HashSet<>();
        for(Location l : logsInChunk) {
            if(random.nextInt(10000) <= 5) {
                changeList.add(findLowestLeavesLocations(l));
            }
        }
        return CompletableFuture.completedFuture(new AsyncPopulationReturn(changeList, i, worldUUID, chunk.getX(), chunk.getZ()));
    }

    /**
     * Method to get all logs in a chunk
     *
     * @param snapshot to get logs for
     * @return List of Block containing logs
     */
    private GlueList<Location> getLogsInChunk(ChunkSnapshot snapshot) {
        GlueList<Location> logsInChunk = new GlueList<>();

        if (snapshot.contains(Material.OAK_LOG.createBlockData())
                || snapshot.contains(Material.BIRCH_LOG.createBlockData())
                || snapshot.contains(Material.ACACIA_LOG.createBlockData())
                || snapshot.contains(Material.DARK_OAK_LOG.createBlockData())
                || snapshot.contains(Material.JUNGLE_LOG.createBlockData())
                || snapshot.contains(Material.SPRUCE_LOG.createBlockData())) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 40; y < snapshot.getHighestBlockYAt(x, z); y++) {

                        Material material = snapshot.getBlockType(x, y, z);
                        Biome biome = snapshot.getBiome(x, y, z);

                        if (checkLog(material)) {
                            if (5 != 0 && checkLeaves(snapshot, x, y, z)) {
                                World world = Bukkit.getWorld(snapshot.getWorldName());

                                int finalX = (snapshot.getX() * 16) + x;
                                int finalZ = (snapshot.getZ() * 16) + z;

                                logsInChunk.add(new Location(world, finalX, y, finalZ));
                            }
                        }
                    }
                }
            }
        }
        return logsInChunk;
    }
    /**
     * Method to check and see if there's a tree at the given coords
     *
     * @param snapshot of the chunk to look in
     * @param x        of the given block
     * @param y        of the given block
     * @param z        of the given block
     * @return whether or not there's leaves
     */
    private boolean checkLeaves(ChunkSnapshot snapshot, int x, int y, int z) {
        for (int d = 0; d < 5; d++) {
            if (snapshot.getBlockType(x, y + d, z).toString().contains("LEAVES")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLog(Material material) {
        return material.equals(Material.OAK_LOG)
                || material.equals(Material.BIRCH_LOG)
                || material.equals(Material.ACACIA_LOG)
                || material.equals(Material.DARK_OAK_LOG)
                || material.equals(Material.JUNGLE_LOG)
                || material.equals(Material.SPRUCE_LOG);
    }

    /**
     * Method to find the lowest location of leaves on a tree
     *
     * @param location of tree
     * @return List of Location of leaves
     */
    private BlockCoordinate findLowestLeavesLocations(Location location) {
        Block block = location.getBlock();

        for (BlockFace possibleFace : possibleFaces) {
            Block relative = block.getRelative(possibleFace);
            Location relativeLocation = relative.getLocation();

            if (relativeLocation.getBlock().getType() == Material.AIR) {
                return new BlockCoordinate(location.getBlockX(), location.getBlockX(), location.getBlockZ(), beeHive);
            }
        }

        return null;
    }
}
