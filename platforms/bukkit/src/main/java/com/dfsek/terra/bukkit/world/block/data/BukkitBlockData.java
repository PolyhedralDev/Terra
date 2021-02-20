package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.bukkit.world.block.BukkitMaterialData;
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

public class BukkitBlockData implements BlockData {
    private org.bukkit.block.data.BlockData delegate;

    protected BukkitBlockData(org.bukkit.block.data.BlockData delegate) {
        this.delegate = delegate;
    }

    public static BukkitBlockData newInstance(org.bukkit.block.data.BlockData bukkitData) {

        if(bukkitData instanceof Rail) return new BukkitRail((Rail) bukkitData);
        if(bukkitData instanceof Stairs) return new BukkitStairs((Stairs) bukkitData);
        if(bukkitData instanceof Slab) return new BukkitSlab((Slab) bukkitData);
        if(TerraBukkitPlugin.BUKKIT_VERSION.above(TerraBukkitPlugin.BukkitVersion.V1_16) && bukkitData instanceof Wall) { // Wall only exists on 1.16 and up.
            return new BukkitWall((Wall) bukkitData);
        }

        if(bukkitData instanceof RedstoneWire) return new BukkitRedstoneWire((RedstoneWire) bukkitData);
        if(bukkitData instanceof AnaloguePowerable) return new BukkitAnaloguePowerable((AnaloguePowerable) bukkitData);

        if(bukkitData instanceof MultipleFacing) return new BukkitMultipleFacing((MultipleFacing) bukkitData);
        if(bukkitData instanceof Rotatable) return new BukkitRotatable((Rotatable) bukkitData);
        if(bukkitData instanceof Directional) return new BukkitDirectional((Directional) bukkitData);
        if(bukkitData instanceof Orientable) return new BukkitOrientable((Orientable) bukkitData);

        if(bukkitData instanceof Waterlogged) return new BukkitWaterlogged((Waterlogged) bukkitData);

        return new BukkitBlockData(bukkitData);
    }


    @Override
    public org.bukkit.block.data.BlockData getHandle() {
        return delegate;
    }

    @Override
    public MaterialData getMaterial() {
        return new BukkitMaterialData(delegate.getMaterial());
    }

    @Override
    public boolean matches(MaterialData materialData) {
        return delegate.getMaterial().equals(((BukkitMaterialData) materialData).getHandle());
    }

    @Override
    public BukkitBlockData clone() {
        try {
            BukkitBlockData n = (BukkitBlockData) super.clone();
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
}
