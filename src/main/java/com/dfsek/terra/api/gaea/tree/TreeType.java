package com.dfsek.terra.api.gaea.tree;

import com.dfsek.terra.api.bukkit.world.block.BukkitMaterialData;
import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.google.common.collect.Sets;
import org.bukkit.Material;

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public enum TreeType implements Tree {
    SHATTERED_SMALL(null, Collections.singleton(Material.END_STONE)),
    SHATTERED_LARGE(null, Collections.singleton(Material.END_STONE)),
    GIANT_OAK(null, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    GIANT_SPRUCE(null, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    SMALL_SHATTERED_PILLAR(null, Collections.singleton(Material.END_STONE)),
    LARGE_SHATTERED_PILLAR(null, Collections.singleton(Material.END_STONE)),
    CACTUS(null, Sets.newHashSet(Material.SAND, Material.RED_SAND)),
    ICE_SPIKE(null, Sets.newHashSet(Material.SNOW_BLOCK, Material.SNOW, Material.STONE, Material.GRASS_BLOCK)),
    OAK(org.bukkit.TreeType.TREE, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    LARGE_OAK(org.bukkit.TreeType.BIG_TREE, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    SPRUCE(org.bukkit.TreeType.REDWOOD, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    LARGE_SPRUCE(org.bukkit.TreeType.TALL_REDWOOD, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    MEGA_SPRUCE(org.bukkit.TreeType.MEGA_REDWOOD, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    BIRCH(org.bukkit.TreeType.BIRCH, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    CHORUS_PLANT(org.bukkit.TreeType.CHORUS_PLANT, Sets.newHashSet(Material.END_STONE)),
    ACACIA(org.bukkit.TreeType.ACACIA, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    TALL_BIRCH(org.bukkit.TreeType.TALL_BIRCH, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    JUNGLE(org.bukkit.TreeType.JUNGLE, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    SMALL_JUNGLE(org.bukkit.TreeType.SMALL_JUNGLE, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    JUNGLE_COCOA(org.bukkit.TreeType.COCOA_TREE, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    JUNGLE_BUSH(org.bukkit.TreeType.JUNGLE_BUSH, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    DARK_OAK(org.bukkit.TreeType.DARK_OAK, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    BROWN_MUSHROOM(org.bukkit.TreeType.BROWN_MUSHROOM, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL, Material.MYCELIUM, Material.NETHERRACK, Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM)),
    RED_MUSHROOM(org.bukkit.TreeType.RED_MUSHROOM, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL, Material.MYCELIUM, Material.NETHERRACK, Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM)),
    SWAMP_OAK(org.bukkit.TreeType.SWAMP, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    WARPED_FUNGUS(org.bukkit.TreeType.WARPED_FUNGUS, Collections.singleton(Material.WARPED_NYLIUM)),
    CRIMSON_FUNGUS(org.bukkit.TreeType.CRIMSON_FUNGUS, Collections.singleton(Material.CRIMSON_NYLIUM));

    private final org.bukkit.TreeType vanillaType;
    private final Set<Material> spawnable;

    TreeType(org.bukkit.TreeType vanillaType, Set<Material> spawnable) {
        this.vanillaType = vanillaType;
        this.spawnable = spawnable;
    }

    public static com.dfsek.terra.api.gaea.tree.TreeType fromBukkit(org.bukkit.TreeType type) {
        switch(type) {
            case TREE: return OAK;
            case BIRCH: return BIRCH;
            case ACACIA: return ACACIA;
            case SWAMP: return SWAMP_OAK;
            case JUNGLE: return JUNGLE;
            case REDWOOD: return SPRUCE;
            case BIG_TREE: return LARGE_OAK;
            case DARK_OAK: return DARK_OAK;
            case COCOA_TREE: return JUNGLE_COCOA;
            case TALL_BIRCH: return TALL_BIRCH;
            case JUNGLE_BUSH: return JUNGLE_BUSH;
            case CHORUS_PLANT: return CHORUS_PLANT;
            case MEGA_REDWOOD: return MEGA_SPRUCE;
            case RED_MUSHROOM: return RED_MUSHROOM;
            case SMALL_JUNGLE: return SMALL_JUNGLE;
            case TALL_REDWOOD: return LARGE_SPRUCE;
            case WARPED_FUNGUS: return WARPED_FUNGUS;
            case BROWN_MUSHROOM: return BROWN_MUSHROOM;
            case CRIMSON_FUNGUS: return CRIMSON_FUNGUS;
            default: throw new IllegalArgumentException();
        }
    }

    public boolean isCustom() {
        return this.vanillaType == null;
    }

    public org.bukkit.TreeType getVanillaTreeType() {
        return vanillaType;
    }

    public CustomTreeType getCustomTreeType() {
        if(getVanillaTreeType() != null) return null;
        return CustomTreeType.valueOf(this.toString());
    }

    public boolean plant(Location l, Random r) {
        if(this.getVanillaTreeType() == null) {
            if(!spawnable.contains(l.subtract(0, 1, 0).getBlock().getType())) return false;
            FractalTree tree = getCustomTreeType().getTree(l, r);
            tree.grow();
            tree.plant();
            return true;
        }
        return l.getWorld().generateTree(l, this.getVanillaTreeType());
    }

    @Override
    public Set<MaterialData> getSpawnable() {
        return spawnable.stream().map(BukkitMaterialData::new).collect(Collectors.toSet());
    }
}
