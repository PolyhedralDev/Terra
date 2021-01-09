package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.RedstoneWire;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

import java.util.Set;
import java.util.stream.Collectors;

public class BukkitRedstoneWire extends BukkitAnaloguePowerable implements RedstoneWire {
    public BukkitRedstoneWire(org.bukkit.block.data.type.RedstoneWire delegate) {
        super(delegate);
    }

    @Override
    public Set<BlockFace> getAllowedFaces() {
        return ((org.bukkit.block.data.type.RedstoneWire) getHandle()).getAllowedFaces().stream().map(BukkitAdapter::adapt).collect(Collectors.toSet());
    }

    @Override
    public Connection getFace(BlockFace face) {
        return BukkitAdapter.adapt(((org.bukkit.block.data.type.RedstoneWire) getHandle()).getFace(BukkitAdapter.adapt(face)));
    }

    @Override
    public void setFace(BlockFace face, Connection connection) {
        ((org.bukkit.block.data.type.RedstoneWire) getHandle()).setFace(BukkitAdapter.adapt(face), BukkitAdapter.adapt(connection));
    }
}
