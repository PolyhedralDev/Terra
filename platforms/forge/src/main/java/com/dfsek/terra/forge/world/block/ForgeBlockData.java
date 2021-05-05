package com.dfsek.terra.forge.world.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.Property;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ForgeBlockData implements BlockData {
    private static final Function<Map.Entry<Property<?>, Comparable<?>>, String> PROPERTY_MAPPER = new Function<Map.Entry<Property<?>, Comparable<?>>, String>() {
        public String apply(@Nullable Map.Entry<Property<?>, Comparable<?>> entry) {
            if (entry == null) {
                return "<NULL>";
            } else {
                Property<?> property = entry.getKey();
                return property.getName() + "=" + this.getName(property, entry.getValue());
            }
        }

        @SuppressWarnings("unchecked")
        private <T extends Comparable<T>> String getName(Property<T> property, Comparable<?> comparable) {
            return property.getName((T)comparable);
        }
    };

    protected BlockState delegate;

    public ForgeBlockData(BlockState delegate) {
        this.delegate = delegate;
    }

    @Override
    public BlockType getBlockType() {
        return (BlockType) delegate.getBlock();
    }

    @Override
    public boolean matches(BlockData other) {
        return delegate.getBlock() == ((ForgeBlockData) other).delegate.getBlock();
    }

    @Override
    public BlockData clone() {
        try {
            return (ForgeBlockData) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public String getAsString() {
        StringBuilder data = new StringBuilder(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(delegate.getBlock())).toString());
        if(!delegate.getProperties().isEmpty()) {
            data.append('[');
            data.append(delegate.getValues().entrySet().stream().map(PROPERTY_MAPPER).collect(Collectors.joining(",")));
            data.append(']');
        }
        return data.toString();
    }

    @Override
    public boolean isAir() {
        return delegate.isAir();
    }

    @Override
    public boolean isStructureVoid() {
        return delegate.getBlock() == Blocks.STRUCTURE_VOID;
    }

    @Override
    public BlockState getHandle() {
        return delegate;
    }
}
