package com.dfsek.terra.fabric.mixin.entity;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.fabric.world.FabricAdapter;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(Entity.class)
@Implements(@Interface(iface = com.dfsek.terra.api.platform.entity.Entity.class, prefix = "terra$"))
public abstract class EntityMixin {
    @Shadow
    public net.minecraft.world.World world;

    @Shadow
    private BlockPos blockPos;

    @Shadow
    public abstract void teleport(double destX, double destY, double destZ);

    @Shadow
    @Nullable
    public abstract Entity moveToWorld(ServerWorld destination);

    @Shadow
    public abstract void sendSystemMessage(Text message, UUID senderUuid);

    public Object terra$getHandle() {
        return this;
    }

    public Location terra$getLocation() {
        return new Location((World) world, FabricAdapter.adapt(blockPos));
    }

    public void terra$setLocation(Location location) {
        teleport(location.getX(), location.getY(), location.getZ());
        moveToWorld((ServerWorld) location.getWorld());
    }

    public World getWorld() {
        return (World) world;
    }

    public void terra$sendMessage(String message) {
        sendSystemMessage(new LiteralText(message), UUID.randomUUID()); // TODO: look into how this actually works and make it less jank
    }
}
