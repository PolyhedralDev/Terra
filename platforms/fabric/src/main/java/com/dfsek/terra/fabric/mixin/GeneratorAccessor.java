package com.dfsek.terra.fabric.mixin;

import net.minecraft.client.world.GeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(GeneratorType.class)
public class GeneratorAccessor {
    @Accessor("VALUES")
    public static List<GeneratorType> getValues() {
        throw new AssertionError();
    }
}
