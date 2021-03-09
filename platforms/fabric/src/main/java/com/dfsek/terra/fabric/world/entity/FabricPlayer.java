package com.dfsek.terra.fabric.world.entity;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.fabric.world.FabricAdapter;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

public class FabricPlayer implements Player {
    private final PlayerEntity delegate;

    public FabricPlayer(PlayerEntity delegate) {
        this.delegate = delegate;
    }

    @Override
    public void sendMessage(String message) {
        delegate.sendMessage(new LiteralText(message), false);
    }

    @Override
    public Object getHandle() {
        return delegate;
    }

    @Override
    public Location getLocation() {
        return FabricAdapter.adapt(delegate.getBlockPos()).toLocation(new FabricWorldAccess(delegate.world));
    }

    @Override
    public World getWorld() {
        return new FabricWorldAccess(delegate.world);
    }
}
