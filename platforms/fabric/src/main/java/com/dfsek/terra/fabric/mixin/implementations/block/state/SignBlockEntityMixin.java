package com.dfsek.terra.fabric.mixin.implementations.block.state;

import com.dfsek.terra.api.platform.block.state.SerialState;
import com.dfsek.terra.api.platform.block.state.Sign;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SignBlockEntity.class)
@Implements(@Interface(iface = Sign.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class SignBlockEntityMixin {
    @Shadow
    public abstract void setTextOnRow(int row, Text text);

    @Shadow
    @Final
    private Text[] text;

    public @NotNull String[] terra$getLines() {
        String[] lines = new String[text.length];
        for(int i = 0; i < text.length; i++) {
            lines[i] = text[i].asString();
        }
        return lines;
    }

    public @NotNull String terra$getLine(int index) throws IndexOutOfBoundsException {
        return text[index].asString();
    }

    public void terra$setLine(int index, @NotNull String line) throws IndexOutOfBoundsException {
        setTextOnRow(index, new LiteralText(line));
    }

    public void terra$applyState(String state) {
        SerialState.parse(state).forEach((k, v) -> {
            if(!k.startsWith("text")) throw new IllegalArgumentException("Invalid property: " + k);
            terra$setLine(Integer.parseInt(k.substring(4)), v);
        });
    }
}
