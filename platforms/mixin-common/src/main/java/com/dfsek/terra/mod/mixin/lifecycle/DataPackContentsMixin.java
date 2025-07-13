package com.dfsek.terra.mod.mixin.lifecycle;

import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registry.PendingTagLoad;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.ReloadableRegistries;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.DataPackContents;
import net.minecraft.server.command.CommandManager;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.dfsek.terra.mod.util.MinecraftUtil;
import com.dfsek.terra.mod.util.TagUtil;


@Mixin(DataPackContents.class)
public class DataPackContentsMixin {
    @Shadow
    @Final
    private ReloadableRegistries.Lookup reloadableRegistries;

    /*
     * #refresh populates all tags in the registries
     */
    @Inject(method = "reload(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/CombinedDynamicRegistries;Ljava/util/List;" +
                     "Lnet/minecraft/resource/featuretoggle/FeatureSet;" +
                     "Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;ILjava/util/concurrent/Executor;" +
                     "Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;",
            at = @At("RETURN"))
    private static void injectReload(ResourceManager resourceManager,
                                     CombinedDynamicRegistries<ServerDynamicRegistryType> dynamicRegistries,
                                     List<PendingTagLoad<?>> pendingTagLoads, FeatureSet enabledFeatures,
                                     CommandManager.RegistrationEnvironment environment, int functionPermissionLevel,
                                     Executor prepareExecutor,
                                     Executor applyExecutor, CallbackInfoReturnable<CompletableFuture<DataPackContents>> cir) {
        DynamicRegistryManager.Immutable dynamicRegistryManager = dynamicRegistries.getCombinedRegistryManager();
        TagUtil.registerWorldPresetTags(dynamicRegistryManager.getOrThrow(RegistryKeys.WORLD_PRESET));

        Registry<Biome> biomeRegistry = dynamicRegistryManager.getOrThrow(RegistryKeys.BIOME);
        TagUtil.registerBiomeTags(biomeRegistry);
        MinecraftUtil.registerFlora(biomeRegistry);
    }
}
