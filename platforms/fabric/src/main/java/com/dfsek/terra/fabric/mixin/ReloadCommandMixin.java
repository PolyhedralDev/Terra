package com.dfsek.terra.fabric.mixin;

import com.dfsek.terra.fabric.FabricEntryPoint;

import net.minecraft.server.command.ReloadCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;


@Mixin(ReloadCommand.class)
public class ReloadCommandMixin {
    @Inject(method = "tryReloadDataPacks", at = @At("HEAD"))
    private static void inject(Collection<String> dataPacks, ServerCommandSource source, CallbackInfo ci) {
        source.sendFeedback(Text.literal("Reloading Terra..."), true);
        FabricEntryPoint.getPlatform().reload();
        source.sendFeedback(Text.literal("Done."), true);
    }
}
