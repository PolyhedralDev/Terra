package com.dfsek.terra.api.gaea.world;

import com.dfsek.terra.api.bukkit.world.block.BukkitBlockData;
import com.dfsek.terra.api.gaea.math.Range;
import com.dfsek.terra.api.gaea.util.GlueList;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.BlockFace;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum FloraType implements Flora {
    TALL_GRASS(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:tall_grass[half=lower]"), Bukkit.createBlockData("minecraft:tall_grass[half=upper]")),
    TALL_FERN(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:large_fern[half=lower]"), Bukkit.createBlockData("minecraft:large_fern[half=upper]")),
    SUNFLOWER(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:sunflower[half=lower]"), Bukkit.createBlockData("minecraft:sunflower[half=upper]")),
    ROSE_BUSH(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:rose_bush[half=lower]"), Bukkit.createBlockData("minecraft:rose_bush[half=upper]")),
    LILAC(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:lilac[half=lower]"), Bukkit.createBlockData("minecraft:lilac[half=upper]")),
    PEONY(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:peony[half=lower]"), Bukkit.createBlockData("minecraft:peony[half=upper]")),
    GRASS(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:grass")),
    FERN(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:fern")),
    AZURE_BLUET(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:azure_bluet")),
    LILY_OF_THE_VALLEY(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:lily_of_the_valley")),
    BLUE_ORCHID(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:blue_orchid")),
    POPPY(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:poppy")),
    DANDELION(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:dandelion")),
    WITHER_ROSE(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:wither_rose")),
    DEAD_BUSH(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL, Material.SAND, Material.RED_SAND,
            Material.TERRACOTTA, Material.BLACK_TERRACOTTA, Material.BLUE_TERRACOTTA, Material.BROWN_TERRACOTTA, Material.CYAN_TERRACOTTA, Material.GRAY_TERRACOTTA,
            Material.GREEN_TERRACOTTA, Material.LIGHT_BLUE_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA, Material.LIME_TERRACOTTA, Material.MAGENTA_TERRACOTTA,
            Material.ORANGE_TERRACOTTA, Material.PINK_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.RED_TERRACOTTA, Material.RED_TERRACOTTA, Material.WHITE_TERRACOTTA,
            Material.YELLOW_TERRACOTTA), Bukkit.createBlockData("minecraft:dead_bush")),
    RED_TULIP(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:red_tulip")),
    ORANGE_TULIP(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:orange_tulip")),
    WHITE_TULIP(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:white_tulip")),
    PINK_TULIP(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:pink_tulip")),
    OXEYE_DAISY(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:oxeye_daisy")),
    ALLIUM(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:allium")),
    CORNFLOWER(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL), Bukkit.createBlockData("minecraft:cornflower")),
    LILY_PAD(Sets.newHashSet(Material.WATER), Bukkit.createBlockData("minecraft:lily_pad")),
    RED_MUSHROOM(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL, Material.DIRT, Material.STONE, Material.NETHERRACK, Material.MYCELIUM), Bukkit.createBlockData("minecraft:red_mushroom")),
    BROWN_MUSHROOM(Sets.newHashSet(Material.GRASS_BLOCK, Material.PODZOL, Material.DIRT, Material.STONE, Material.NETHERRACK, Material.MYCELIUM), Bukkit.createBlockData("minecraft:brown_mushroom")),
    ;

    private final List<BlockData> data = new GlueList<>();

    private final Set<Material> spawns;

    FloraType(Set<Material> validSpawns, org.bukkit.block.data.BlockData... type) {
        data.addAll(Arrays.stream(type).map(BukkitBlockData::new).collect(Collectors.toList()));
        this.spawns = validSpawns;
    }

    @Override
    public List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range check) {
        List<Block> blocks = new GlueList<>();
        for(int y : check) {
            Block block = chunk.getBlock(x, y, z);
            if(spawns.contains(block.getType()) && valid(block)) {
                blocks.add(chunk.getBlock(x, y, z));
            }
        }
        return blocks;
    }

    private boolean valid(Block block) {
        for(int i = 1; i < data.size() + 1; i++) {
            block = block.getRelative(BlockFace.UP);
            if(!block.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public boolean plant(Location l) {
        for(int i = 1; i < data.size() + 1; i++) {
            l.clone().add(0, i, 0).getBlock().setBlockData(data.get(i - 1), false);
        }
        return true;
    }
}
