package com.dfsek.terra.fabric.mixin.lifecycle.client;

import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.event.GameInitializationEvent;
import com.dfsek.terra.fabric.generation.TerraGeneratorType;
import com.dfsek.terra.fabric.mixin.access.GeneratorTypeAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/util/WindowProvider;createWindow(Lnet/minecraft/client/WindowSettings;Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/client/util/Window;", // sorta arbitrary position, after mod init, before window opens
            shift = At.Shift.BEFORE))
    public void injectConstructor(RunArgs args, CallbackInfo callbackInfo) {
        TerraFabricPlugin.getInstance().getEventManager().callEvent(new GameInitializationEvent());
        TerraFabricPlugin.getInstance().getConfigRegistry().forEach(pack -> {
            final GeneratorType generatorType = new TerraGeneratorType(pack);
            //noinspection ConstantConditions
            ((GeneratorTypeAccessor) generatorType).setTranslationKey(new LiteralText("Terra:" + pack.getTemplate().getID()));
            GeneratorTypeAccessor.getValues().add(1, generatorType);
        });
    }
}
