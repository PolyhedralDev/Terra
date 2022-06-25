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

package com.dfsek.terra.mod.mixin.implementations.terra.block.entity;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.entity.SerialState;
import com.dfsek.terra.api.block.entity.Sign;


@Mixin(SignBlockEntity.class)
@Implements(@Interface(iface = Sign.class, prefix = "terra$"))
public abstract class SignBlockEntityMixin {
    @Shadow
    @Final
    private Text[] texts;
    
    @Shadow
    public abstract void setTextOnRow(int row, Text text);
    
    public void terra$setLine(int index, @NotNull String line) throws IndexOutOfBoundsException {
        setTextOnRow(index, Text.literal(line));
    }
    
    public @NotNull String[] terra$getLines() {
        String[] lines = new String[texts.length];
        for(int i = 0; i < texts.length; i++) {
            lines[i] = texts[i].getString();
        }
        return lines;
    }
    
    public @NotNull String terra$getLine(int index) throws IndexOutOfBoundsException {
        return texts[index].getString();
    }
    
    public void terra$applyState(String state) {
        SerialState.parse(state).forEach((k, v) -> {
            if(!k.startsWith("text")) throw new IllegalArgumentException("Invalid property: " + k);
            terra$setLine(Integer.parseInt(k.substring(4)), v);
        });
    }
}
