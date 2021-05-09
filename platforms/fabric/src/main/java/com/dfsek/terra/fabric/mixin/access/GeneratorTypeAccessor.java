package com.dfsek.terra.fabric.mixin.access;

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
    @Accessor("VALUES")
    static List<GeneratorType> getValues() {
        throw new UnsupportedOperationException();
    }

    @Accessor("SCREEN_PROVIDERS")
    static Map<Optional<GeneratorType>, GeneratorType.ScreenProvider> getScreenProviders() {
        throw new UnsupportedOperationException();
    }

    @Mutable
    @Accessor("SCREEN_PROVIDERS")
    static void setScreenProviders(Map<Optional<GeneratorType>, GeneratorType.ScreenProvider> SCREEN_PROVIDERS) {
        throw new UnsupportedOperationException();
    }

    @Mutable
    @Accessor("translationKey")
    void setTranslationKey(Text translationKey);
}
