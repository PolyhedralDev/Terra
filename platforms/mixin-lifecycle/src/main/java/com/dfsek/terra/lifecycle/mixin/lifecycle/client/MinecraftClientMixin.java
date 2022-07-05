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

package com.dfsek.terra.lifecycle.mixin.lifecycle.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dfsek.terra.lifecycle.util.LifecycleUtil;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE",
                                        target = "Lnet/minecraft/client/util/WindowProvider;createWindow" +
                                                 "(Lnet/minecraft/client/WindowSettings;Ljava/lang/String;Ljava/lang/String;)" +
                                                 "Lnet/minecraft/client/util/Window;",
                                        // sorta arbitrary position, after mod init, before window opens
                                        shift = At.Shift.BEFORE))
    public void injectConstructor(RunArgs args, CallbackInfo callbackInfo) {
        LifecycleUtil.initialize();
    }
}
