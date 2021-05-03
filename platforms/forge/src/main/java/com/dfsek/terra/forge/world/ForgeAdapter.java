package com.dfsek.terra.forge.world;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.forge.inventory.ForgeEnchantment;
import com.dfsek.terra.forge.inventory.ForgeItem;
import com.dfsek.terra.forge.inventory.ForgeItemStack;
import com.dfsek.terra.forge.world.block.ForgeBlockData;
import com.dfsek.terra.forge.world.block.ForgeBlockType;
import com.dfsek.terra.forge.world.block.data.ForgeDirectional;
import com.dfsek.terra.forge.world.block.data.ForgeMultipleFacing;
import com.dfsek.terra.forge.world.block.data.ForgeOrientable;
import com.dfsek.terra.forge.world.block.data.ForgeRotatable;
import com.dfsek.terra.forge.world.block.data.ForgeSlab;
import com.dfsek.terra.forge.world.block.data.ForgeStairs;
import com.dfsek.terra.forge.world.block.data.ForgeWaterlogged;
import com.dfsek.terra.forge.world.entity.ForgeCommandSender;
import com.dfsek.terra.forge.world.entity.ForgeEntityType;
import com.dfsek.terra.forge.world.entity.ForgePlayer;
import com.dfsek.terra.forge.world.handles.world.ForgeWorldHandle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Arrays;

public final class ForgeAdapter {
    public static BlockPos adapt(Vector3 v) {
        return new BlockPos(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }

    public static Vector3 adapt(BlockPos pos) {
        return new Vector3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static ForgeBlockData adapt(BlockState state) {
        if(state.hasProperty(BlockStateProperties.STAIRS_SHAPE)) return new ForgeStairs(state);

        if(state.hasProperty(BlockStateProperties.SLAB_TYPE)) return new ForgeSlab(state);

        if(state.hasProperty(BlockStateProperties.AXIS)) return new ForgeOrientable(state, BlockStateProperties.AXIS);
        if(state.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) return new ForgeOrientable(state, BlockStateProperties.HORIZONTAL_AXIS);

        if(state.hasProperty(BlockStateProperties.ROTATION_16)) return new ForgeRotatable(state);

        if(state.hasProperty(BlockStateProperties.FACING)) return new ForgeDirectional(state, BlockStateProperties.FACING);
        if(state.hasProperty(BlockStateProperties.FACING_HOPPER)) return new ForgeDirectional(state, BlockStateProperties.FACING_HOPPER);
        if(state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) return new ForgeDirectional(state, BlockStateProperties.HORIZONTAL_FACING);

        if(state.getProperties().containsAll(Arrays.asList(BlockStateProperties.NORTH, BlockStateProperties.SOUTH, BlockStateProperties.EAST, BlockStateProperties.WEST)))
            return new ForgeMultipleFacing(state);
        if(state.hasProperty(BlockStateProperties.WATERLOGGED)) return new ForgeWaterlogged(state);
        return new ForgeBlockData(state);
    }

    public static CommandSender adapt(CommandSource serverCommandSource) {
        if(serverCommandSource.getEntity() instanceof PlayerEntity) return new ForgePlayer((PlayerEntity) serverCommandSource.getEntity());
        return new ForgeCommandSender(serverCommandSource);
    }

    public static Direction adapt(BlockFace face) {
        switch(face) {
            case NORTH:
                return Direction.NORTH;
            case WEST:
                return Direction.WEST;
            case SOUTH:
                return Direction.SOUTH;
            case EAST:
                return Direction.EAST;
            case UP:
                return Direction.UP;
            case DOWN:
                return Direction.DOWN;
            default:
                throw new IllegalArgumentException("Illegal direction: " + face);
        }
    }

    public static BlockType adapt(Block block) {
        return new ForgeBlockType(block);
    }

    public static EntityType adapt(net.minecraft.entity.EntityType<?> entityType) {
        return new ForgeEntityType(entityType);
    }

    public static net.minecraft.entity.EntityType<? extends Entity> adapt(EntityType entityType) {
        return ((ForgeEntityType) entityType).getHandle();
    }

    public static ItemStack adapt(com.dfsek.terra.api.platform.inventory.ItemStack itemStack) {
        return ((ForgeItemStack) itemStack).getHandle();
    }

    public static com.dfsek.terra.api.platform.inventory.ItemStack adapt(ItemStack itemStack) {
        return new ForgeItemStack(itemStack);
    }

    public static com.dfsek.terra.api.platform.inventory.Item adapt(Item item) {
        return new ForgeItem(item);
    }

    public static Enchantment adapt(net.minecraft.enchantment.Enchantment enchantment) {
        return new ForgeEnchantment(enchantment);
    }

    public static net.minecraft.enchantment.Enchantment adapt(Enchantment enchantment) {
        return ((ForgeEnchantment) enchantment).getHandle();
    }

    public IWorld adapt(ForgeWorldHandle worldHandle) {
        return worldHandle.getWorld();
    }
}
