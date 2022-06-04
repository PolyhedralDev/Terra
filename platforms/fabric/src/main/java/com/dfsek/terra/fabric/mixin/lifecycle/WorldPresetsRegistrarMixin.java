package com.dfsek.terra.fabric.mixin.lifecycle;

import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;

import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;


@Mixin(targets = "net/minecraft/world/gen/WorldPresets$Registrar")
public abstract class WorldPresetsRegistrarMixin {
    @Shadow
    protected abstract RegistryEntry<WorldPreset> register(RegistryKey<WorldPreset> key, DimensionOptions dimensionOptions);
    
    @Shadow
    protected abstract DimensionOptions createOverworldOptions(ChunkGenerator chunkGenerator);
    
    @Shadow
    @Final
    private Registry<StructureSet> structureSetRegistry;
    
    @Shadow
    @Final
    private Registry<NoiseParameters> noiseParametersRegistry;
    @Shadow
    @Final
    private Registry<Biome> biomeRegistry;
    @Shadow
    @Final
    private Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @Inject(method = "initAndGetDefault()Lnet/minecraft/util/registry/RegistryEntry;", at = @At("HEAD"))
    private void injectInit(CallbackInfoReturnable<RegistryEntry<WorldPreset>> cir) {
        LOGGER.info("Registering Terra world types...");
        RegistryEntry<ChunkGeneratorSettings> overworld = chunkGeneratorSettingsRegistry.getOrCreateEntry(ChunkGeneratorSettings.OVERWORLD);
        FabricEntryPoint
                .getPlatform()
                .getRawConfigRegistry()
                .forEach((id, pack) -> register(
                                 terraWorldKey(id.getNamespace(), id.getID()),
                                 createOverworldOptions(
                                         new FabricChunkGeneratorWrapper(
                                                 structureSetRegistry,
                                                 new TerraBiomeSource(biomeRegistry, pack),
                                                 pack,
                                                 overworld)
                                                       )
                                               )
                        );
    }
    
    private static RegistryKey<WorldPreset> terraWorldKey(String packNamespace, String id) {
        return RegistryKey.of(Registry.WORLD_PRESET_KEY, Identifier.of("terra", id.toLowerCase(Locale.ROOT) + "/" + id.toLowerCase(
                Locale.ROOT)));
    }
}
