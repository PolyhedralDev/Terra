package com.dfsek.terra.bukkit.world;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.enums.Axis;
import com.dfsek.terra.api.block.state.properties.enums.Half;
import com.dfsek.terra.api.block.state.properties.enums.RailShape;
import com.dfsek.terra.api.block.state.properties.enums.RedstoneConnection;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.BukkitEntity;
import com.dfsek.terra.bukkit.BukkitPlayer;
import com.dfsek.terra.bukkit.world.block.BukkitBlockTypeAndItem;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;
import com.dfsek.terra.bukkit.world.inventory.BukkitItemStack;
import com.dfsek.terra.bukkit.world.inventory.meta.BukkitEnchantment;
import com.dfsek.terra.transform.MapTransform;
import com.dfsek.terra.transform.TransformerImpl;


/**
 * Utility class to adapt Bukkit enums to Terra enums.
 */
public final class BukkitAdapter {
    public static TransformerImpl<TreeType, String> TREE_TRANSFORMER = new TransformerImpl.Builder<TreeType, String>()
            .addTransform(new MapTransform<TreeType, String>()
                                  .add(TreeType.COCOA_TREE, "JUNGLE_COCOA")
                                  .add(TreeType.BIG_TREE, "LARGE_OAK")
                                  .add(TreeType.TALL_REDWOOD, "LARGE_SPRUCE")
                                  .add(TreeType.REDWOOD, "SPRUCE")
                                  .add(TreeType.TREE, "OAK")
                                  .add(TreeType.MEGA_REDWOOD, "MEGA_SPRUCE")
                                  .add(TreeType.SWAMP, "SWAMP_OAK"))
            .addTransform(TreeType::toString)
            .build();
    
    
    public static BlockState adapt(org.bukkit.block.data.BlockData data) {
        return BukkitBlockState.newInstance(data);
    }
    
    public static org.bukkit.block.data.BlockData adapt(BlockState data) {
        return ((BukkitBlockState) data).getHandle();
    }
    
    public static Axis adapt(org.bukkit.Axis axis) {
        switch(axis) {
            case X:
                return Axis.X;
            case Y:
                return Axis.Y;
            case Z:
                return Axis.Z;
            default:
                throw new IllegalStateException();
        }
    }
    
    public static Half adapt(org.bukkit.block.data.Bisected.Half half) {
        switch(half) {
            case BOTTOM:
                return Half.BOTTOM;
            case TOP:
                return Half.TOP;
            default:
                throw new IllegalStateException();
        }
    }
    
    public static RedstoneConnection adapt(org.bukkit.block.data.type.RedstoneWire.Connection connection) {
        switch(connection) {
            case NONE:
                return RedstoneConnection.NONE;
            case UP:
                return RedstoneConnection.UP;
            case SIDE:
                return RedstoneConnection.SIDE;
            default:
                throw new IllegalStateException();
        }
    }
    
    public static org.bukkit.block.data.type.RedstoneWire.Connection adapt(RedstoneConnection connection) {
        switch(connection) {
            case SIDE:
                return org.bukkit.block.data.type.RedstoneWire.Connection.SIDE;
            case UP:
                return org.bukkit.block.data.type.RedstoneWire.Connection.UP;
            case NONE:
                return org.bukkit.block.data.type.RedstoneWire.Connection.NONE;
            default:
                throw new IllegalStateException();
        }
    }
    
    public static RailShape adapt(org.bukkit.block.data.Rail.Shape shape) {
        switch(shape) {
            case SOUTH_WEST:
                return RailShape.SOUTH_WEST;
            case SOUTH_EAST:
                return RailShape.SOUTH_EAST;
            case NORTH_EAST:
                return RailShape.NORTH_EAST;
            case NORTH_WEST:
                return RailShape.NORTH_WEST;
            case ASCENDING_EAST:
                return RailShape.ASCENDING_EAST;
            case ASCENDING_WEST:
                return RailShape.ASCENDING_WEST;
            case ASCENDING_SOUTH:
                return RailShape.ASCENDING_SOUTH;
            case ASCENDING_NORTH:
                return RailShape.ASCENDING_NORTH;
            case NORTH_SOUTH:
                return RailShape.NORTH_SOUTH;
            case EAST_WEST:
                return RailShape.EAST_WEST;
            default:
                throw new IllegalStateException();
        }
    }
    
    public static org.bukkit.block.data.Rail.Shape adapt(RailShape shape) {
        switch(shape) {
            case EAST_WEST:
                return org.bukkit.block.data.Rail.Shape.EAST_WEST;
            case NORTH_SOUTH:
                return org.bukkit.block.data.Rail.Shape.NORTH_SOUTH;
            case ASCENDING_NORTH:
                return org.bukkit.block.data.Rail.Shape.ASCENDING_NORTH;
            case ASCENDING_SOUTH:
                return org.bukkit.block.data.Rail.Shape.ASCENDING_SOUTH;
            case ASCENDING_WEST:
                return org.bukkit.block.data.Rail.Shape.ASCENDING_WEST;
            case ASCENDING_EAST:
                return org.bukkit.block.data.Rail.Shape.ASCENDING_EAST;
            case NORTH_WEST:
                return org.bukkit.block.data.Rail.Shape.NORTH_WEST;
            case NORTH_EAST:
                return org.bukkit.block.data.Rail.Shape.NORTH_EAST;
            case SOUTH_EAST:
                return org.bukkit.block.data.Rail.Shape.SOUTH_EAST;
            case SOUTH_WEST:
                return org.bukkit.block.data.Rail.Shape.SOUTH_WEST;
            default:
                throw new IllegalStateException();
        }
    }
    
    
    public static org.bukkit.block.data.Bisected.Half adapt(Half half) {
        switch(half) {
            case TOP:
                return org.bukkit.block.data.Bisected.Half.TOP;
            case BOTTOM:
                return org.bukkit.block.data.Bisected.Half.BOTTOM;
            default:
                throw new IllegalStateException();
        }
    }
    
    public static org.bukkit.Axis adapt(Axis axis) {
        switch(axis) {
            case Z:
                return org.bukkit.Axis.Z;
            case Y:
                return org.bukkit.Axis.Y;
            case X:
                return org.bukkit.Axis.X;
            default:
                throw new IllegalStateException();
        }
    }
    
    public static Vector3 adapt(Location location) {
        return new Vector3(location.getX(), location.getY(), location.getZ());
    }
    
    public static Vector adapt(Vector3 vector3) {
        return new Vector(vector3.getX(), vector3.getY(), vector3.getZ());
    }
    
    public static Vector3 adapt(Vector vector) {
        return new Vector3(vector.getX(), vector.getY(), vector.getZ());
    }
    
    public static CommandSender adapt(org.bukkit.command.CommandSender sender) {
        if(sender instanceof Player) return new BukkitPlayer((Player) sender);
        if(sender instanceof Entity) return new BukkitEntity((Entity) sender);
        return new BukkitCommandSender(sender);
    }
    
    public static org.bukkit.command.CommandSender adapt(CommandSender sender) {
        return ((BukkitCommandSender) sender).getHandle();
    }
    
    public static World adapt(org.bukkit.World world) {
        return new BukkitWorld(world);
    }
    
    public static org.bukkit.World adapt(World world) {
        return (org.bukkit.World) world.getHandle();
    }
    
    public static Chunk adapt(org.bukkit.Chunk chunk) {
        return new BukkitChunk(chunk);
    }
    
    public static org.bukkit.Chunk adapt(Chunk chunk) {
        return (org.bukkit.Chunk) chunk.getHandle();
    }
    
    public static Enchantment adapt(org.bukkit.enchantments.Enchantment enchantment) {
        return new BukkitEnchantment(enchantment);
    }
    
    public static org.bukkit.enchantments.Enchantment adapt(Enchantment enchantment) {
        return ((BukkitEnchantment) enchantment).getHandle();
    }
    
    public static Player adapt(com.dfsek.terra.api.entity.Player player) {
        return ((BukkitPlayer) player).getHandle();
    }
    
    public static com.dfsek.terra.api.entity.Player adapt(Player player) {
        return new BukkitPlayer(player);
    }
    
    public static BukkitBlockTypeAndItem adapt(Material material) {
        return new BukkitBlockTypeAndItem(material);
    }
    
    public static Material adapt(BlockType type) {
        return ((BukkitBlockTypeAndItem) type).getHandle();
    }
    
    public static ItemStack adapt(org.bukkit.inventory.ItemStack in) {
        return new BukkitItemStack(in);
    }
    
    public static org.bukkit.inventory.ItemStack adapt(ItemStack in) {
        return ((BukkitItemStack) in).getHandle();
    }
}
