package com.dfsek.terra.forge.mixin.implementations.entity;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.forge.ForgeAdapter;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(Entity.class)
@Implements(@Interface(iface = com.dfsek.terra.api.platform.entity.Entity.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class EntityMixin {
    @Shadow
    public net.minecraft.world.World level;

    @Shadow
    private BlockPos blockPosition;

    @Shadow
    public abstract void teleportTo(double destX, double destY, double destZ);


    @Shadow
    public abstract void sendMessage(ITextComponent p_145747_1_, UUID p_145747_2_);

    public Object terra$getHandle() {
        return this;
    }

    public Location terra$getLocation() {
        return new Location((World) level, ForgeAdapter.adapt(blockPosition));
    }

    public void terra$setLocation(Location location) {
        teleportTo(location.getX(), location.getY(), location.getZ());
    }

    public World terra$getWorld() {
        return (World) level;
    }

    public void terra$sendMessage(String message) {
        sendMessage(new StringTextComponent(message), UUID.randomUUID()); // TODO: look into how this actually works and make it less jank
    }
}
