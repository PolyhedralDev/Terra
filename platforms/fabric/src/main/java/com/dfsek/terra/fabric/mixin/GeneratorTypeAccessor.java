package com.dfsek.terra.fabric.mixin;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(GeneratorType.class)
public interface GeneratorTypeAccessor {
    @Accessor
    static List<GeneratorType> getVALUES() {
        throw new AssertionError();
    }

    @Accessor
    static Map<Optional<GeneratorType>, GeneratorType.ScreenProvider> getSCREEN_PROVIDERS() {
        throw new UnsupportedOperationException();
    }

    @Mutable
    @Accessor
    static void setSCREEN_PROVIDERS(Map<Optional<GeneratorType>, GeneratorType.ScreenProvider> SCREEN_PROVIDERS) {
        throw new UnsupportedOperationException();
    }

    @Mutable
    @Accessor
    void setTranslationKey(Text translationKey);
}
