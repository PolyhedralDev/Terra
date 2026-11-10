package com.dfsek.terra.mod.mixin.implementations.terra.block.state;

import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;
import java.util.function.Predicate;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.api.block.state.properties.Property;
import com.dfsek.terra.api.data.ExtendedData;


@Mixin(BlockInput.class)
@Implements(@Interface(iface = BlockStateExtended.class, prefix = "terra$"))
public abstract class BlockInputMixin implements Predicate<BlockInWorld> {

    @Shadow
    @Nullable
    @Final
    private CompoundTag tag;

    @Shadow
    public abstract BlockState getState();

    @Shadow
    public abstract Set<net.minecraft.world.level.block.state.properties.Property<?>> getDefinedProperties();

    public boolean terra$matches(com.dfsek.terra.api.block.state.BlockState other) {
        return ((com.dfsek.terra.api.block.state.BlockState) getState()).matches(other);
    }

    @Intrinsic
    public <T extends Comparable<T>> boolean terra$has(com.dfsek.terra.api.block.state.properties.Property<T> property) {
        return ((com.dfsek.terra.api.block.state.BlockState) getState()).has(property);
    }

    @Intrinsic
    public <T extends Comparable<T>> T terra$get(com.dfsek.terra.api.block.state.properties.Property<T> property) {
        return ((com.dfsek.terra.api.block.state.BlockState) getState()).get(property);
    }

    @Intrinsic
    public <T extends Comparable<T>> com.dfsek.terra.api.block.state.BlockState terra$set(Property<T> property, T value) {
        return ((com.dfsek.terra.api.block.state.BlockState) getState()).set(property, value);
    }

    @Intrinsic
    public BlockType terra$getBlockType() {
        return ((com.dfsek.terra.api.block.state.BlockState) getState()).getBlockType();
    }

    @Intrinsic
    public String terra$getAsString(boolean properties) {
        return ((com.dfsek.terra.api.block.state.BlockState) getState()).getAsString(properties);
    }

    @Intrinsic
    public boolean terra$isAir() {
        return ((com.dfsek.terra.api.block.state.BlockState) getState()).isAir();
    }

    @SuppressWarnings({ "ConstantValue", "DataFlowIssue", "EqualsBetweenInconvertibleTypes" })
    @Intrinsic
    public BlockStateExtended terra$setData(ExtendedData data) {
        return (BlockStateExtended) new BlockInput(getState(), getDefinedProperties(),
            data.getClass().equals(CompoundTag.class) ? ((CompoundTag) ((Object) data)) : null);
    }

    @SuppressWarnings("DataFlowIssue")
    @Intrinsic
    public ExtendedData terra$getData() {
        return ((ExtendedData) ((Object) tag));
    }

    @Intrinsic
    public com.dfsek.terra.api.block.state.BlockState terra$getState() {
        return (com.dfsek.terra.api.block.state.BlockState) getState();
    }

    public Object terra$getHandle() {
        return getState();
    }
}
