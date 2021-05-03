package com.dfsek.terra.forge.world.entity;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.forge.world.ForgeAdapter;
import com.dfsek.terra.forge.world.handles.world.ForgeWorldAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class ForgePlayer implements Player {
    private final PlayerEntity delegate;

    public ForgePlayer(PlayerEntity delegate) {
        this.delegate = delegate;
    }

    @Override
    public void sendMessage(String message) {
        delegate.displayClientMessage(new StringTextComponent(message), false);
    }

    @Override
    public Object getHandle() {
        return delegate;
    }

    @Override
    public Location getLocation() {
        return ForgeAdapter.adapt(delegate.blockPosition()).toLocation(new ForgeWorldAccess(delegate.level));
    }

    @Override
    public World getWorld() {
        return new ForgeWorldAccess(delegate.level);
    }

    @Override
    public void setLocation(Location location) {
        delegate.teleportTo(location.getX(), location.getY(), location.getZ());
    }
}
