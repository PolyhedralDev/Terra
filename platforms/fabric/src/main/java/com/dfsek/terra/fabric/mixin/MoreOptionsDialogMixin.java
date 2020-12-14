package com.dfsek.terra.fabric.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MoreOptionsDialog.class)
public class MoreOptionsDialogMixin {

    @Inject(at = @At("HEAD"), method = "method_28092(Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/font/TextRenderer;)V")
    private void draw(final CreateWorldScreen parent, MinecraftClient client, TextRenderer textRenderer, CallbackInfo info) {
        System.out.println("More options opened");
    }

    /*@Inject(at = @At("RETURN"), method = "setVisible(B)V")
    private void setVisible(boolean visible, CallbackInfo info) {
        System.out.println("redraw");
    }
     */
}
