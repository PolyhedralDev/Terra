package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.generic.world.block.BlockFace;
import com.dfsek.terra.api.generic.world.block.data.MultipleFacing;
import com.dfsek.terra.bukkit.world.block.BukkitBlockData;

import java.util.Set;
import java.util.stream.Collectors;

public class BukkitMultipleFacing extends BukkitBlockData implements MultipleFacing {
    private final org.bukkit.block.data.MultipleFacing delegate;

    public BukkitMultipleFacing(org.bukkit.block.data.MultipleFacing delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    @Override
    public Set<BlockFace> getFaces() {
        return delegate.getFaces().stream().map(BukkitEnumAdapter::fromBukkitBlockFace).collect(Collectors.toSet());
    }

    @Override
    public void setFace(BlockFace face, boolean facing) {
        delegate.setFace(TerraEnumAdapter.fromTerraBlockFace(face), facing);
    }

    @Override
    public Set<BlockFace> getAllowedFaces() {
        return delegate.getAllowedFaces().stream().map(BukkitEnumAdapter::fromBukkitBlockFace).collect(Collectors.toSet());
    }

    @Override
    public boolean hasFace(BlockFace f) {
        return delegate.hasFace(TerraEnumAdapter.fromTerraBlockFace(f));
    }
}
