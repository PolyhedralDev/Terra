package com.dfsek.terra.mod.mixin.implementations.terra.nbt;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.state.BlockStateExtended;

import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Predicate;


@Mixin(NbtCompound.class)
@Implements(@Interface(iface = BlockData.class, prefix = "terra$"))
public abstract class NbtCompoundMixin implements NbtElement {
    @Intrinsic
    public String terra$toString() {
        return this.toString();
    }
}
