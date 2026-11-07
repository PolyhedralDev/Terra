package com.dfsek.terra.mod.mixin.implementations.terra;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

import com.dfsek.terra.api.Handle;


/**
 * A ton of Minecraft classes must implement Handle identically, we can just take care of it here
 */
@Mixin({
    ServerLevel.class,
    WorldGenRegion.class,

    Block.class,
    BlockState.class,

    BlockEntity.class,
    RandomizableContainerBlockEntity.class,
    BaseContainerBlockEntity.class,

    ProtoChunk.class,
    LevelChunk.class,

    Entity.class,
    EntityType.class,

    CommandSourceStack.class,

    Item.class,
    ItemStack.class,
    Enchantment.class,

    Biome.class
})
@Implements(@Interface(iface = Handle.class, prefix = "terra$"))
public class HandleImplementationMixin {
    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }
}
