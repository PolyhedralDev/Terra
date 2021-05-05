package com.dfsek.terra.fabric.mixin.init;

import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.util.UserCache;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectConstructor(Thread thread, DynamicRegistryManager.Impl impl, LevelStorage.Session session,
                                  SaveProperties saveProperties, ResourcePackManager resourcePackManager, Proxy proxy,
                                  DataFixer dataFixer, ServerResourceManager serverResourceManager,
                                  MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository,
                                  UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory,
                                  CallbackInfo callbackInfo) {
        TerraFabricPlugin.getInstance().packInit(); // Load during MinecraftServer construction, after other mods have registered blocks and stuff
    }
}
