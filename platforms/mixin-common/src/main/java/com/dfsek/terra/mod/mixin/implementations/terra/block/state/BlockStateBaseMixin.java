package com.dfsek.terra.mod.mixin.implementations.terra.block.state;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.level.block.state.StateHolder;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.stream.Collectors;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;


@Mixin(BlockStateBase.class)
@Implements(@Interface(iface = BlockState.class, prefix = "terra$"))
public abstract class BlockStateBaseMixin extends StateHolder<Block, net.minecraft.world.level.block.state.BlockState> {
    private BlockStateBaseMixin(Block owner, net.minecraft.world.level.block.state.properties.Property<?>[] propertyKeys,
                            Comparable<?>[] propertyValues) {
        super(owner, propertyKeys, propertyValues);
    }

    @Shadow
    public abstract Block getBlock();

    @Shadow
    public abstract boolean isAir();

    public boolean terra$matches(BlockState other) {
        return getBlock() == ((net.minecraft.world.level.block.state.BlockState) other).getBlock();
    }

    @Intrinsic
    public <T extends Comparable<T>> boolean terra$has(Property<T> property) {
        if(property instanceof net.minecraft.world.level.block.state.properties.Property<?> minecraftProperty) {
            return hasProperty(minecraftProperty);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Intrinsic
    public <T extends Comparable<T>> T terra$get(Property<T> property) {
        return getValue((net.minecraft.world.level.block.state.properties.Property<T>) property);
    }

    @SuppressWarnings("unchecked")
    @Intrinsic
    public <T extends Comparable<T>> BlockState terra$set(Property<T> property, T value) {
        return (BlockState) setValue((net.minecraft.world.level.block.state.properties.Property<T>) property, value);
    }

    @Intrinsic
    public BlockType terra$getBlockType() {
        return (BlockType) getBlock();
    }

    @Intrinsic
    public String terra$getAsString(boolean properties) {
        StringBuilder data = new StringBuilder(BuiltInRegistries.BLOCK.getKey(getBlock()).toString());
        if(properties && !isSingletonState()) {
            data.append('[');
            data.append(
                getValues().map(net.minecraft.world.level.block.state.properties.Property.Value::toString).collect(Collectors.joining(",")));
            data.append(']');
        }
        return data.toString();
    }

    @Intrinsic
    public boolean terra$isAir() {
        return isAir();
    }
}
