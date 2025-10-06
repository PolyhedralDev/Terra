package com.dfsek.terra.mod.mixin.implementations.terra.nbt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

import com.dfsek.terra.api.block.BlockData;


@Mixin(NbtCompound.class)
@Implements(@Interface(iface = BlockData.class, prefix = "terra$"))
public abstract class NbtCompoundMixin implements NbtElement {
    @Intrinsic
    public String terra$toString() {
        return this.toString();
    }
}
