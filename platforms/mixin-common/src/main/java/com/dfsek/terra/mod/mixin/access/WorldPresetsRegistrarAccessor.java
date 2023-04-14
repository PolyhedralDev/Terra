package com.dfsek.terra.mod.mixin.access;

import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.WorldPreset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(targets = "net.minecraft.world.gen.WorldPresets$Registrar")
public interface WorldPresetsRegistrarAccessor {
    @Invoker("createPreset")
    WorldPreset callCreatePreset(DimensionOptions dimensionOptions);
}
