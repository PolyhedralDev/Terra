/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.fabric.mixin.lifecycle.server;

import com.dfsek.terra.fabric.util.FabricUtil;

import net.minecraft.server.Main;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.DynamicRegistryManager.Mutable;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.fabric.FabricEntryPoint;


@Mixin(Main.class)
public class ServerMainMixin {
    @Shadow
    @Final
    private static Logger LOGGER;
    
    @Inject(method = "main([Ljava/lang/String;)V",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/resource/ResourcePackManager;<init>(Lnet/minecraft/resource/ResourceType;[Lnet/minecraft/resource/ResourcePackProvider;)V") // after registry manager creation
            )
    private static void injectConstructor(String[] args, CallbackInfo ci) {
        FabricEntryPoint.getPlatform().getEventManager().callEvent(
                new PlatformInitializationEvent()); // Load during MinecraftServer construction, after other mods have registered blocks
        // and stuff
    }
    
    @Redirect(method = "method_40373(Lnet/minecraft/world/level/storage/LevelStorage$Session;Ljoptsimple/OptionSet;" +
                       "Ljoptsimple/OptionSpec;Lnet/minecraft/server/dedicated/ServerPropertiesLoader;Ljoptsimple/OptionSpec;" +
                       "Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/resource/DataPackSettings;)" +
                       "Lcom/mojang/datafixers/util/Pair;",
              at = @At(value = "INVOKE",
                       target = "net/minecraft/util/registry/DynamicRegistryManager.createAndLoad ()" +
                                "Lnet/minecraft/util/registry/DynamicRegistryManager$Mutable;"))
    private static Mutable injectBiomes() {
        Mutable mutable = DynamicRegistryManager.createAndLoad();
        LOGGER.info("Injecting Terra biomes...");
        FabricUtil.registerBiomes(mutable.get(Registry.BIOME_KEY));
        return mutable;
    }
}
