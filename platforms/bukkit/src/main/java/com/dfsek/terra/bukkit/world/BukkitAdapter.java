package com.dfsek.terra.bukkit.world;


import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.block.Axis;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.api.platform.block.data.Bisected;
import com.dfsek.terra.api.platform.block.data.Rail;
import com.dfsek.terra.api.platform.block.data.RedstoneWire;
import com.dfsek.terra.api.platform.block.data.Slab;
import com.dfsek.terra.api.platform.block.data.Stairs;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.transform.MapTransform;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.BukkitEntity;
import com.dfsek.terra.bukkit.BukkitPlayer;
import com.dfsek.terra.bukkit.world.block.BukkitBlockTypeAndItem;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockData;
import com.dfsek.terra.bukkit.world.inventory.BukkitItemStack;
import com.dfsek.terra.bukkit.world.inventory.meta.BukkitEnchantment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Utility class to adapt Bukkit enums to Terra enums.
 */
public final class BukkitAdapter {
    public static Transformer<TreeType, String> TREE_TRANSFORMER = new Transformer.Builder<TreeType, String>()
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


    public static Stairs.Shape adapt(org.bukkit.block.data.type.Stairs.Shape shape) {
        return switch(shape) {
            case STRAIGHT -> Stairs.Shape.STRAIGHT;
            case INNER_LEFT -> Stairs.Shape.INNER_LEFT;
            case OUTER_LEFT -> Stairs.Shape.OUTER_LEFT;
            case INNER_RIGHT -> Stairs.Shape.INNER_RIGHT;
            case OUTER_RIGHT -> Stairs.Shape.OUTER_RIGHT;
        };
    }

    public static BlockData adapt(org.bukkit.block.data.BlockData data) {
        return BukkitBlockData.newInstance(data);
    }

    public static org.bukkit.block.data.BlockData adapt(BlockData data) {
        return ((BukkitBlockData) data).getHandle();
    }

    public static Axis adapt(org.bukkit.Axis axis) {
        return switch(axis) {
            case X -> Axis.X;
            case Y -> Axis.Y;
            case Z -> Axis.Z;
        };
    }

    public static Bisected.Half adapt(org.bukkit.block.data.Bisected.Half half) {
        return switch(half) {
            case BOTTOM -> Bisected.Half.BOTTOM;
            case TOP -> Bisected.Half.TOP;
        };
    }

    public static BlockFace adapt(org.bukkit.block.BlockFace face) {
        return switch(face) {
            case DOWN -> BlockFace.DOWN;
            case UP -> BlockFace.UP;
            case NORTH_WEST -> BlockFace.NORTH_WEST;
            case NORTH_EAST -> BlockFace.NORTH_EAST;
            case SOUTH_EAST -> BlockFace.SOUTH_EAST;
            case SOUTH_WEST -> BlockFace.SOUTH_WEST;
            case NORTH_NORTH_WEST -> BlockFace.NORTH_NORTH_WEST;
            case WEST_NORTH_WEST -> BlockFace.WEST_NORTH_WEST;
            case WEST_SOUTH_WEST -> BlockFace.WEST_SOUTH_WEST;
            case SOUTH_SOUTH_WEST -> BlockFace.SOUTH_SOUTH_WEST;
            case EAST_NORTH_EAST -> BlockFace.EAST_NORTH_EAST;
            case WEST -> BlockFace.WEST;
            case SOUTH -> BlockFace.SOUTH;
            case EAST -> BlockFace.EAST;
            case NORTH -> BlockFace.NORTH;
            case SELF -> BlockFace.SELF;
            case EAST_SOUTH_EAST -> BlockFace.EAST_SOUTH_EAST;
            case NORTH_NORTH_EAST -> BlockFace.NORTH_NORTH_EAST;
            case SOUTH_SOUTH_EAST -> BlockFace.SOUTH_SOUTH_EAST;
        };
    }

    public static Slab.Type adapt(org.bukkit.block.data.type.Slab.Type type) {
        return switch(type) {
            case BOTTOM -> Slab.Type.BOTTOM;
            case TOP -> Slab.Type.TOP;
            case DOUBLE -> Slab.Type.DOUBLE;
        };
    }

    public static RedstoneWire.Connection adapt(org.bukkit.block.data.type.RedstoneWire.Connection connection) {
        return switch(connection) {
            case NONE -> RedstoneWire.Connection.NONE;
            case UP -> RedstoneWire.Connection.UP;
            case SIDE -> RedstoneWire.Connection.SIDE;
        };
    }

    public static org.bukkit.block.data.type.RedstoneWire.Connection adapt(RedstoneWire.Connection connection) {
        return switch(connection) {
            case SIDE -> org.bukkit.block.data.type.RedstoneWire.Connection.SIDE;
            case UP -> org.bukkit.block.data.type.RedstoneWire.Connection.UP;
            case NONE -> org.bukkit.block.data.type.RedstoneWire.Connection.NONE;
        };
    }

    public static org.bukkit.block.data.type.Stairs.Shape adapt(Stairs.Shape shape) {
        return switch(shape) {
            case STRAIGHT -> org.bukkit.block.data.type.Stairs.Shape.STRAIGHT;
            case INNER_LEFT -> org.bukkit.block.data.type.Stairs.Shape.INNER_LEFT;
            case OUTER_LEFT -> org.bukkit.block.data.type.Stairs.Shape.OUTER_LEFT;
            case INNER_RIGHT -> org.bukkit.block.data.type.Stairs.Shape.INNER_RIGHT;
            case OUTER_RIGHT -> org.bukkit.block.data.type.Stairs.Shape.OUTER_RIGHT;
        };
    }

    public static Rail.Shape adapt(org.bukkit.block.data.Rail.Shape shape) {
        return switch(shape) {
            case SOUTH_WEST -> Rail.Shape.SOUTH_WEST;
            case SOUTH_EAST -> Rail.Shape.SOUTH_EAST;
            case NORTH_EAST -> Rail.Shape.NORTH_EAST;
            case NORTH_WEST -> Rail.Shape.NORTH_WEST;
            case ASCENDING_EAST -> Rail.Shape.ASCENDING_EAST;
            case ASCENDING_WEST -> Rail.Shape.ASCENDING_WEST;
            case ASCENDING_SOUTH -> Rail.Shape.ASCENDING_SOUTH;
            case ASCENDING_NORTH -> Rail.Shape.ASCENDING_NORTH;
            case NORTH_SOUTH -> Rail.Shape.NORTH_SOUTH;
            case EAST_WEST -> Rail.Shape.EAST_WEST;
        };
    }

    public static org.bukkit.block.data.Rail.Shape adapt(Rail.Shape shape) {
        return switch(shape) {
            case EAST_WEST -> org.bukkit.block.data.Rail.Shape.EAST_WEST;
            case NORTH_SOUTH -> org.bukkit.block.data.Rail.Shape.NORTH_SOUTH;
            case ASCENDING_NORTH -> org.bukkit.block.data.Rail.Shape.ASCENDING_NORTH;
            case ASCENDING_SOUTH -> org.bukkit.block.data.Rail.Shape.ASCENDING_SOUTH;
            case ASCENDING_WEST -> org.bukkit.block.data.Rail.Shape.ASCENDING_WEST;
            case ASCENDING_EAST -> org.bukkit.block.data.Rail.Shape.ASCENDING_EAST;
            case NORTH_WEST -> org.bukkit.block.data.Rail.Shape.NORTH_WEST;
            case NORTH_EAST -> org.bukkit.block.data.Rail.Shape.NORTH_EAST;
            case SOUTH_EAST -> org.bukkit.block.data.Rail.Shape.SOUTH_EAST;
            case SOUTH_WEST -> org.bukkit.block.data.Rail.Shape.SOUTH_WEST;
        };
    }


    public static org.bukkit.block.data.Bisected.Half adapt(Bisected.Half half) {
        return switch(half) {
            case TOP -> org.bukkit.block.data.Bisected.Half.TOP;
            case BOTTOM -> org.bukkit.block.data.Bisected.Half.BOTTOM;
        };
    }

    public static org.bukkit.Axis adapt(Axis axis) {
        return switch(axis) {
            case Z -> org.bukkit.Axis.Z;
            case Y -> org.bukkit.Axis.Y;
            case X -> org.bukkit.Axis.X;
        };
    }

    public static org.bukkit.block.BlockFace adapt(BlockFace face) {
        return switch(face) {
            case DOWN -> org.bukkit.block.BlockFace.DOWN;
            case UP -> org.bukkit.block.BlockFace.UP;
            case NORTH_WEST -> org.bukkit.block.BlockFace.NORTH_WEST;
            case NORTH_EAST -> org.bukkit.block.BlockFace.NORTH_EAST;
            case SOUTH_EAST -> org.bukkit.block.BlockFace.SOUTH_EAST;
            case SOUTH_WEST -> org.bukkit.block.BlockFace.SOUTH_WEST;
            case NORTH_NORTH_WEST -> org.bukkit.block.BlockFace.NORTH_NORTH_WEST;
            case WEST_NORTH_WEST -> org.bukkit.block.BlockFace.WEST_NORTH_WEST;
            case WEST_SOUTH_WEST -> org.bukkit.block.BlockFace.WEST_SOUTH_WEST;
            case SOUTH_SOUTH_WEST -> org.bukkit.block.BlockFace.SOUTH_SOUTH_WEST;
            case EAST_NORTH_EAST -> org.bukkit.block.BlockFace.EAST_NORTH_EAST;
            case WEST -> org.bukkit.block.BlockFace.WEST;
            case SOUTH -> org.bukkit.block.BlockFace.SOUTH;
            case EAST -> org.bukkit.block.BlockFace.EAST;
            case NORTH -> org.bukkit.block.BlockFace.NORTH;
            case SELF -> org.bukkit.block.BlockFace.SELF;
            case EAST_SOUTH_EAST -> org.bukkit.block.BlockFace.EAST_SOUTH_EAST;
            case NORTH_NORTH_EAST -> org.bukkit.block.BlockFace.NORTH_NORTH_EAST;
            case SOUTH_SOUTH_EAST -> org.bukkit.block.BlockFace.SOUTH_SOUTH_EAST;
        };
    }

    public static org.bukkit.block.data.type.Slab.Type adapt(Slab.Type type) {
        return switch(type) {
            case TOP -> org.bukkit.block.data.type.Slab.Type.TOP;
            case DOUBLE -> org.bukkit.block.data.type.Slab.Type.DOUBLE;
            case BOTTOM -> org.bukkit.block.data.type.Slab.Type.BOTTOM;
        };
    }

    public static Location adapt(com.dfsek.terra.api.math.vector.Location location) {
        return new Location(((BukkitWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
    }

    public static com.dfsek.terra.api.math.vector.Location adapt(Location location) {
        return new com.dfsek.terra.api.math.vector.Location(adapt(location.getWorld()), location.getX(), location.getY(), location.getZ());
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

    public static Player adapt(com.dfsek.terra.api.platform.entity.Player player) {
        return ((BukkitPlayer) player).getHandle();
    }

    public static com.dfsek.terra.api.platform.entity.Player adapt(Player player) {
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
