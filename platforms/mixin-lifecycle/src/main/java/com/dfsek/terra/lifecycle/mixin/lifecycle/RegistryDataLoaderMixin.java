package com.dfsek.terra.lifecycle.mixin.lifecycle;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryLoadTask;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceManagerRegistryLoadTask;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.timeline.Timeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.dfsek.terra.lifecycle.LifecyclePlatform;
import com.dfsek.terra.lifecycle.mixin.RegistryLoadTaskAccessor;
import com.dfsek.terra.lifecycle.util.LifecycleUtil;
import com.dfsek.terra.lifecycle.util.LoadContextHolder;
import com.dfsek.terra.lifecycle.util.RegistryHack;
import com.dfsek.terra.minecraft.attribute.VanillaContext;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.ModPlatform;
import com.dfsek.terra.mod.mixin.access.VillagerTypeAccessor;


@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
    // Until the load finishes, the static registries' tags are only in these lookups.
    @Inject(method = "createContext", at = @At("HEAD"))
    private static void captureContext(List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryLoadTask<?>> loadTasks,
                                       CallbackInfoReturnable<RegistryOps.RegistryInfoLookup> cir) {
        for(RegistryLoadTask<?> task : loadTasks) ((LoadContextHolder) task).terra_setContextRegistries(contextRegistries);
    }

    // The thenApplyAsync callback in load(LoaderFactory, List, List, Executor): every registry populated, none frozen.
    @Inject(
        method = "lambda$load$2(Ljava/util/List;Ljava/util/Map;Ljava/lang/Void;)Lnet/minecraft/core/RegistryAccess$Frozen;",
        at = @At("HEAD")
    )
    private static void beforeFreeze(List<RegistryLoadTask<?>> loadTasks, Map<ResourceKey<?>, Exception> loadingErrors, Void ignored,
                                     CallbackInfoReturnable<RegistryAccess.Frozen> cir) {
        boolean loadingDynamicRegistries = loadTasks.stream().anyMatch(
            task -> task instanceof ResourceManagerRegistryLoadTask<?> && getRegistry(task).key() == Registries.BIOME);
        if(loadingDynamicRegistries) {
            ModPlatform platform = CommonPlatform.get();
            platform.getRawConfigRegistry().clear();
            WritableRegistry<Biome> biomes = extractRegistry(loadTasks, Registries.BIOME).orElseThrow();
            WritableRegistry<DimensionType> dimensionTypes = extractRegistry(loadTasks, Registries.DIMENSION_TYPE).orElseThrow();
            WritableRegistry<WorldPreset> worldPresets = extractRegistry(loadTasks, Registries.WORLD_PRESET).orElseThrow();
            WritableRegistry<NoiseGeneratorSettings> chunkGeneratorSettings = extractRegistry(loadTasks,
                Registries.NOISE_SETTINGS).orElseThrow();
            WritableRegistry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists = extractRegistry(loadTasks,
                Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST).orElseThrow();
            WritableRegistry<Enchantment> enchantments = extractRegistry(loadTasks, Registries.ENCHANTMENT).orElseThrow();
            // Binds the datapack's timelines before the freeze, for the time-marker check.
            WritableRegistry<Timeline> timelines = extractRegistry(loadTasks, Registries.TIMELINE).orElseThrow();

            LifecyclePlatform.setRegistries(biomes, dimensionTypes, chunkGeneratorSettings, multiNoiseBiomeSourceParameterLists,
                enchantments);
            // After extractRegistry: its terra_bind gives the parents' holders their values before the freeze.
            List<? extends WritableRegistry<?>> loaded = loadTasks.stream().map(RegistryDataLoaderMixin::getRegistry).toList();
            List<HolderLookup.RegistryLookup<?>> context = Objects.requireNonNull(
                ((LoadContextHolder) loadTasks.getFirst()).terra_contextRegistries(), "the load built no context");
            platform.attributeParents().beginLoad(
                VanillaContext.ofLoad(loaded, context, VillagerTypeAccessor.getBiomeTypeToIdMap()::get));
            LifecycleUtil.initialize(biomes, timelines, worldPresets);
        }
    }

    @Unique
    private static WritableRegistry<?> getRegistry(RegistryLoadTask<?> task) {
        return ((RegistryLoadTaskAccessor) task).getRegistry();
    }

    @Unique
    @SuppressWarnings("unchecked")
    private static <T> Optional<WritableRegistry<T>> extractRegistry(List<RegistryLoadTask<?>> instance,
                                                                    ResourceKey<Registry<T>> key) {
        List<? extends WritableRegistry<?>> matches = instance
            .stream().map(RegistryDataLoaderMixin::getRegistry)
            .filter(r -> r.key().equals(key))
            .toList();
        if(matches.size() > 1) {
            throw new IllegalStateException("Illegal number of registries returned: " + matches);
        } else if(matches.isEmpty()) {
            return Optional.empty();
        }
        WritableRegistry<T> registry = (WritableRegistry<T>) matches.getFirst();
        ((RegistryHack) registry).terra_bind();
        return Optional.of(registry);
    }
}
