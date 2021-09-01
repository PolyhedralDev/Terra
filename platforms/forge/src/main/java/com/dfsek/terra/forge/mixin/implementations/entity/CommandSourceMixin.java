package com.dfsek.terra.forge.mixin.implementations.entity;

import com.dfsek.terra.api.platform.CommandSender;

import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(CommandSource.class)
@Implements(@Interface(iface = CommandSender.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class CommandSourceMixin {
    @Shadow
    public abstract void sendSuccess(ITextComponent p_197030_1_, boolean p_197030_2_);
    
    public void terra$sendMessage(String message) {
        sendSuccess(new StringTextComponent(message), true);
    }
    
    public Object terra$getHandle() {
        return this;
    }
}
