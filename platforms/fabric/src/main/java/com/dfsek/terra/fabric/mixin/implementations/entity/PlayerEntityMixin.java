package com.dfsek.terra.fabric.mixin.implementations.entity;

import com.dfsek.terra.api.platform.entity.Player;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
@Implements(@Interface(iface = Player.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class PlayerEntityMixin extends EntityMixin {

}
