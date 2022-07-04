package com.dfsek.terra.mod.mixin.implementations.terra.block.state;


import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.Block;
import net.minecraft.state.State;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.stream.Collectors;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;
import com.dfsek.terra.mod.mixin.access.StateAccessor;


@Mixin(AbstractBlockState.class)
@Implements(@Interface(iface = BlockState.class, prefix = "terra$"))
public abstract class BlockStateMixin extends State<Block, net.minecraft.block.BlockState> {
    private BlockStateMixin(Block owner, ImmutableMap<net.minecraft.state.property.Property<?>, Comparable<?>> entries,
                            MapCodec<net.minecraft.block.BlockState> codec) {
        super(owner, entries, codec);
    }
    
    @Shadow
    public abstract Block getBlock();
    
    @Shadow
    public abstract boolean isAir();
    
    public boolean terra$matches(BlockState other) {
        return getBlock() == ((net.minecraft.block.BlockState) other).getBlock();
    }
    
    @Intrinsic
    public <T extends Comparable<T>> boolean terra$has(Property<T> property) {
        if(property instanceof net.minecraft.state.property.Property<?> minecraftProperty) {
            return contains(minecraftProperty);
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    @Intrinsic
    public <T extends Comparable<T>> T terra$get(Property<T> property) {
        return get((net.minecraft.state.property.Property<T>) property);
    }
    
    @SuppressWarnings("unchecked")
    @Intrinsic
    public <T extends Comparable<T>> BlockState terra$set(Property<T> property, T value) {
        return (BlockState) with((net.minecraft.state.property.Property<T>) property, value);
    }
    
    @Intrinsic
    public BlockType terra$getBlockType() {
        return (BlockType) getBlock();
    }
    
    @Intrinsic
    public String terra$getAsString(boolean properties) {
        StringBuilder data = new StringBuilder(Registry.BLOCK.getId(getBlock()).toString());
        if(properties && !getEntries().isEmpty()) {
            data.append('[');
            data.append(
                    getEntries().entrySet().stream().map(StateAccessor.getPropertyMapPrinter()).collect(Collectors.joining(",")));
            data.append(']');
        }
        return data.toString();
    }
    
    @Intrinsic
    public boolean terra$isAir() {
        return isAir();
    }
}
