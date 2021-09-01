package com.dfsek.terra.forge.mixin.access;

import net.minecraft.client.gui.screen.BiomeGeneratorTypeScreens;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;


@Mixin(BiomeGeneratorTypeScreens.class)
public interface BiomeGeneratorTypeScreensAccessor {
    @Accessor("PRESETS")
    static List<BiomeGeneratorTypeScreens> getPresets() {
        throw new UnsupportedOperationException();
    }
    
    @Mutable
    @Accessor
    void setDescription(ITextComponent description);
}
