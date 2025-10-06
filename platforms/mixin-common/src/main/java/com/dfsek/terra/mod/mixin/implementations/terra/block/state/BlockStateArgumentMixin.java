package com.dfsek.terra.mod.mixin.implementations.terra.block.state;

import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;
import java.util.function.Predicate;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.api.block.state.properties.Property;


@Mixin(BlockStateArgument.class)
@Implements(@Interface(iface = BlockStateExtended.class, prefix = "terra$"))
public abstract class BlockStateArgumentMixin implements Predicate<CachedBlockPosition> {

    @Shadow
    @Nullable
    @Final
    private NbtCompound data;

    @Shadow
    public abstract BlockState getBlockState();

    @Shadow
    public abstract Set<net.minecraft.state.property.Property<?>> getProperties();

    public boolean terra$matches(com.dfsek.terra.api.block.state.BlockState other) {
        return ((com.dfsek.terra.api.block.state.BlockState) getBlockState()).matches(other);
    }

    @Intrinsic
    public <T extends Comparable<T>> boolean terra$has(com.dfsek.terra.api.block.state.properties.Property<T> property) {
        return ((com.dfsek.terra.api.block.state.BlockState) getBlockState()).has(property);
    }

    @Intrinsic
    public <T extends Comparable<T>> T terra$get(com.dfsek.terra.api.block.state.properties.Property<T> property) {
        return ((com.dfsek.terra.api.block.state.BlockState) getBlockState()).get(property);
    }

    @Intrinsic
    public <T extends Comparable<T>> com.dfsek.terra.api.block.state.BlockState terra$set(Property<T> property, T value) {
        return ((com.dfsek.terra.api.block.state.BlockState) getBlockState()).set(property, value);
    }

    @Intrinsic
    public BlockType terra$getBlockType() {
        return ((com.dfsek.terra.api.block.state.BlockState) getBlockState()).getBlockType();
    }

    @Intrinsic
    public String terra$getAsString(boolean properties) {
        return ((com.dfsek.terra.api.block.state.BlockState) getBlockState()).getAsString(properties);
    }

    @Intrinsic
    public boolean terra$isAir() {
        return ((com.dfsek.terra.api.block.state.BlockState) getBlockState()).isAir();
    }

    @SuppressWarnings({ "ConstantValue", "DataFlowIssue", "EqualsBetweenInconvertibleTypes" })
    @Intrinsic
    public BlockStateExtended terra$setData(BlockData data) {
        return (BlockStateExtended) new BlockStateArgument(getBlockState(), getProperties(),
            data.getClass().equals(NbtCompound.class) ? ((NbtCompound) ((Object) data)) : null);
    }

    @SuppressWarnings("DataFlowIssue")
    @Intrinsic
    public BlockData terra$getData() {
        return ((BlockData) ((Object) data));
    }

    @Intrinsic
    public com.dfsek.terra.api.block.state.BlockState terra$getState() {
        return (com.dfsek.terra.api.block.state.BlockState) getBlockState();
    }
}
