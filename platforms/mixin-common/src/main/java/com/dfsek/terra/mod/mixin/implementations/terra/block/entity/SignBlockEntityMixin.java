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

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.entity.SignTextSlot;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

import com.dfsek.terra.api.block.entity.SerialState;
import com.dfsek.terra.api.block.entity.Sign;


@Mixin(SignBlockEntity.class)
@Implements(@Interface(iface = Sign.class, prefix = "terra$"))
public abstract class SignBlockEntityMixin {
    @Shadow
    private SignText frontText;

    @Shadow
    public abstract void setText(SignText text, SignTextSlot slot);

    public void terra$setLine(int index, @NotNull String line) throws IndexOutOfBoundsException {
        setText(frontText.asMutable().setLine(index, Component.literal(line)).asImmutable(), SignTextSlot.FRONT);
    }

    public @NotNull String[] terra$getLines() {
        List<Component> texts = frontText.getMessages(false);
        String[] lines = new String[texts.size()];
        for(int i = 0; i < texts.size(); i++) {
            lines[i] = texts.get(i).getString();
        }
        return lines;
    }

    public @NotNull String terra$getLine(int index) throws IndexOutOfBoundsException {
        return frontText.getMessages(false).get(index).getString();
    }

    public void terra$applyState(String state) {
        SerialState.parse(state).forEach((k, v) -> {
            if(!k.startsWith("text")) throw new IllegalArgumentException("Invalid property: " + k);
            terra$setLine(Integer.parseInt(k.substring(4)), v);
        });
    }
}
