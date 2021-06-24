package com.dfsek.terra.forge.mixin.implementations.block.state;

import com.dfsek.terra.api.block.state.SerialState;
import com.dfsek.terra.api.block.state.Sign;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SignTileEntity.class)
@Implements(@Interface(iface = Sign.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class SignTileEntityMixin {
    @Shadow
    @Final
    private ITextComponent[] messages;

    @Shadow
    public abstract void setMessage(int p_212365_1_, ITextComponent p_212365_2_);

    public @NotNull
    String[] terra$getLines() {
        String[] lines = new String[messages.length];
        for(int i = 0; i < messages.length; i++) {
            lines[i] = messages[i].getString();
        }
        return lines;
    }

    public @NotNull
    String terra$getLine(int index) throws IndexOutOfBoundsException {

        return messages[index].getString();
    }

    public void terra$setLine(int index, @NotNull String line) throws IndexOutOfBoundsException {
        setMessage(index, new StringTextComponent(line));
    }

    public void terra$applyState(String state) {
        SerialState.parse(state).forEach((k, v) -> {
            if(!k.startsWith("text")) throw new IllegalArgumentException("Invalid property: " + k);
            terra$setLine(Integer.parseInt(k.substring(4)), v);
        });
    }
}
