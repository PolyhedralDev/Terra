package com.dfsek.terra.util;

import com.dfsek.terra.generation.Sampler;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.Vector;
import org.polydev.gaea.world.palette.Palette;

import java.util.Map;

public final class SlabUtil {
    public static void prepareBlockPartFloor(BlockData down, BlockData orig, ChunkGenerator.ChunkData chunk, Vector block, Map<Material, Palette<BlockData>> slabs,
                                             Map<Material, Palette<BlockData>> stairs, double thresh, Sampler sampler) {
        if(sampler.sample(block.getBlockX(), block.getBlockY() - 0.4, block.getBlockZ()) > thresh) {
            if(stairs != null) {
                Palette<BlockData> stairPalette = stairs.get(down.getMaterial());
                if(stairPalette != null) {
                    BlockData stair = stairPalette.get(0, block.getBlockX(), block.getBlockZ());
                    Stairs stairNew = (Stairs) stair.clone();
                    if(placeStair(orig, chunk, block, thresh, sampler, stairNew)) return; // Successfully placed part.
                }
            }
            BlockData slab = slabs.getOrDefault(down.getMaterial(), PaletteUtil.BLANK_PALETTE).get(0, block.getBlockX(), block.getBlockZ());
            if(slab instanceof Waterlogged) {
                ((Waterlogged) slab).setWaterlogged(orig.matches(PaletteUtil.WATER));
            } else if(orig.matches(PaletteUtil.WATER)) return;
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), slab);
        }
    }

    public static void prepareBlockPartCeiling(BlockData up, BlockData orig, ChunkGenerator.ChunkData chunk, Vector block, Map<Material, Palette<BlockData>> slabs,
                                               Map<Material, Palette<BlockData>> stairs, double thresh, Sampler sampler) {
        if(sampler.sample(block.getBlockX(), block.getBlockY() + 0.4, block.getBlockZ()) > thresh) {
            if(stairs != null) {
                Palette<BlockData> stairPalette = stairs.get(up.getMaterial());
                if(stairPalette != null) {
                    BlockData stair = stairPalette.get(0, block.getBlockX(), block.getBlockZ()).clone();
                    Stairs stairNew = (Stairs) stair.clone();
                    stairNew.setHalf(Bisected.Half.TOP);
                    if(placeStair(orig, chunk, block, thresh, sampler, stairNew)) return; // Successfully placed part.
                }
            }
            BlockData slab = slabs.getOrDefault(up.getMaterial(), PaletteUtil.BLANK_PALETTE).get(0, block.getBlockX(), block.getBlockZ()).clone();
            if(slab instanceof Bisected) ((Bisected) slab).setHalf(Bisected.Half.TOP);
            if(slab instanceof Slab) ((Slab) slab).setType(Slab.Type.TOP);
            if(slab instanceof Waterlogged) {
                ((Waterlogged) slab).setWaterlogged(orig.matches(PaletteUtil.WATER));
            } else if(orig.matches(PaletteUtil.WATER)) return; // Only replace water if waterlogged.
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), slab);
        }
    }

    private static boolean placeStair(BlockData orig, ChunkGenerator.ChunkData chunk, Vector block, double thresh, Sampler sampler, Stairs stairNew) {
        if(sampler.sample(block.getBlockX() - 0.55, block.getBlockY(), block.getBlockZ()) > thresh) {
            stairNew.setFacing(BlockFace.WEST);
        } else if(sampler.sample(block.getBlockX(), block.getBlockY(), block.getBlockZ() - 0.55) > thresh) {
            stairNew.setFacing(BlockFace.NORTH);
        } else if(sampler.sample(block.getBlockX(), block.getBlockY(), block.getBlockZ() + 0.55) > thresh) {
            stairNew.setFacing(BlockFace.SOUTH);
        } else if(sampler.sample(block.getBlockX() + 0.55, block.getBlockY(), block.getBlockZ()) > thresh) {
            stairNew.setFacing(BlockFace.EAST);
        } else stairNew = null;
        if(stairNew != null) {
            if(orig.matches(PaletteUtil.WATER)) stairNew.setWaterlogged(true);
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), stairNew);
            return true;
        }
        return false;
    }
}
