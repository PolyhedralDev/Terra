package com.dfsek.terra.fabric.mixin.access;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

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

    @Invoker("getChunkGenerator")
    ChunkGenerator callGetChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed);
}
