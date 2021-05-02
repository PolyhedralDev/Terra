package com.dfsek.terra.forge.mixin;

import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.biome.ParticleEffectAmbience;
import net.minecraft.world.biome.SoundAdditionsAmbience;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(BiomeAmbience.class)
public interface BiomeAmbienceAccessor {
    @Accessor
    int getFogColor();

    @Accessor
    int getWaterColor();

    @Accessor
    int getWaterFogColor();

    @Accessor
    int getSkyColor();

    @Accessor
    Optional<Integer> getFoliageColorOverride();

    @Accessor
    Optional<Integer> getGrassColorOverride();

    @Accessor
    BiomeAmbience.GrassColorModifier getGrassColorModifier();

    @Accessor
    Optional<ParticleEffectAmbience> getAmbientParticleSettings();

    @Accessor
    Optional<SoundEvent> getAmbientLoopSoundEvent();

    @Accessor
    Optional<MoodSoundAmbience> getAmbientMoodSettings();

    @Accessor
    Optional<SoundAdditionsAmbience> getAmbientAdditionsSettings();

    @Accessor
    Optional<BackgroundMusicSelector> getBackgroundMusic();
}
