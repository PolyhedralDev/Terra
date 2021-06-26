package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import org.bukkit.Material;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Wall;

public class BukkitBlockState implements BlockState {
    private org.bukkit.block.data.BlockData delegate;

    protected BukkitBlockState(org.bukkit.block.data.BlockData delegate) {
        this.delegate = delegate;
    }

    public static BukkitBlockState newInstance(org.bukkit.block.data.BlockData bukkitData) {
        return new BukkitBlockState(bukkitData);
    }


    @Override
    public org.bukkit.block.data.BlockData getHandle() {
        return delegate;
    }

    @Override
    public BlockType getBlockType() {
        return BukkitAdapter.adapt(delegate.getMaterial());
    }

    @Override
    public boolean matches(BlockState data) {
        return delegate.getMaterial() == ((BukkitBlockState) data).getHandle().getMaterial();
    }

    @Override
    public BukkitBlockState clone() {
        try {
            BukkitBlockState n = (BukkitBlockState) super.clone();
            n.delegate = delegate.clone();
            return n;
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public String getAsString() {
        return delegate.getAsString(false);
    }

    @Override
    public boolean isAir() {
        return delegate.getMaterial().isAir();
    }

    @Override
    public boolean isStructureVoid() {
        return delegate.getMaterial() == Material.STRUCTURE_VOID;
    }
}
