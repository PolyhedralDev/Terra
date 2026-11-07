package com.dfsek.terra.mod.mixin.implementations.terra.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

import com.dfsek.terra.api.data.ExtendedData;


@Mixin(CompoundTag.class)
@Implements(@Interface(iface = ExtendedData.class, prefix = "terra$"))
public abstract class CompoundTagMixin implements Tag {
    @Intrinsic
    public String terra$toString() {
        return this.toString();
    }
}
