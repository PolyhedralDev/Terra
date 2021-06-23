package com.dfsek.terra.forge.mixin.init;

import com.dfsek.terra.forge.TerraForgePlugin;
import com.dfsek.terra.forge.generation.TerraGeneratorType;
import com.dfsek.terra.forge.mixin.access.BiomeGeneratorTypeScreensAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.BiomeGeneratorTypeScreens;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/resources/ResourcePackList;reload()V" // sorta arbitrary position, after mod init, before window opens
    ))
    public void injectConstructor(ResourcePackList resourcePackList) {
        TerraForgePlugin.getInstance().init(); // Load during MinecraftClient construction, after other mods have registered blocks and stuff
        TerraForgePlugin.getInstance().getConfigRegistry().forEach(pack -> {
            final BiomeGeneratorTypeScreens generatorType = new TerraGeneratorType(pack);
            //noinspection ConstantConditions
            ((BiomeGeneratorTypeScreensAccessor) generatorType).setDescription(new StringTextComponent("Terra:" + pack.getTemplate().getID()));
            BiomeGeneratorTypeScreensAccessor.getPresets().add(1, generatorType);
        });
        resourcePackList.reload();
    }
}
