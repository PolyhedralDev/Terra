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

package com.dfsek.terra.fabric.mixin.lifecycle.client;

import com.dfsek.terra.fabric.util.FabricUtil;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.text.LiteralText;
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
import com.dfsek.terra.fabric.generation.TerraGeneratorType;
import com.dfsek.terra.fabric.mixin.access.GeneratorTypeAccessor;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Final
    private static Logger LOGGER;
    
    @Redirect(method = "method_40187(Lnet/minecraft/util/registry/DynamicRegistryManager;Lnet/minecraft/world/gen/GeneratorOptions;" +
                       "Lnet/minecraft/world/level/LevelInfo;Lnet/minecraft/resource/ResourceManager;" +
                       "Lnet/minecraft/resource/DataPackSettings;)Lcom/mojang/datafixers/util/Pair;",
              at = @At(value = "INVOKE",
                       target = "net/minecraft/util/registry/DynamicRegistryManager.createAndLoad ()" +
                                "Lnet/minecraft/util/registry/DynamicRegistryManager$Mutable;"))
    private static Mutable injectBiomes() {
        Mutable mutable = DynamicRegistryManager.createAndLoad();
        LOGGER.info("Injecting Terra biomes...");
        FabricUtil.registerBiomes(mutable.get(Registry.BIOME_KEY));
        return mutable;
    }
    
    @Inject(method = "<init>", at = @At(value = "INVOKE",
                                        target = "Lnet/minecraft/client/util/WindowProvider;createWindow" +
                                                 "(Lnet/minecraft/client/WindowSettings;Ljava/lang/String;Ljava/lang/String;)" +
                                                 "Lnet/minecraft/client/util/Window;",
                                        // sorta arbitrary position, after mod init, before window opens
                                        shift = At.Shift.BEFORE))
    public void injectConstructor(RunArgs args, CallbackInfo callbackInfo) {
        FabricEntryPoint.getPlatform().getEventManager().callEvent(new PlatformInitializationEvent());
        FabricEntryPoint.getPlatform().getConfigRegistry().forEach(pack -> {
            final GeneratorType generatorType = new TerraGeneratorType(pack);
            //noinspection ConstantConditions
            ((GeneratorTypeAccessor) generatorType).setDisplayName(new LiteralText("Terra:" + pack.getID()));
            GeneratorTypeAccessor.getValues().add(1, generatorType);
        });
    }
}
