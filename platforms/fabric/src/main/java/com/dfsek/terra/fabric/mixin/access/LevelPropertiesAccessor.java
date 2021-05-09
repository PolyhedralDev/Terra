package com.dfsek.terra.fabric.mixin.access;

import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LevelProperties.class)
public interface LevelPropertiesAccessor {
    @Accessor("generatorOptions")
    void setGeneratorOptions(GeneratorOptions generatorOptions);
}
