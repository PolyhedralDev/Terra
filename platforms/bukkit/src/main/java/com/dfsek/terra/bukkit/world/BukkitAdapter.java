package com.dfsek.terra.bukkit.world;


import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.block.Axis;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Bisected;
import com.dfsek.terra.api.platform.block.data.Rail;
import com.dfsek.terra.api.platform.block.data.RedstoneWire;
import com.dfsek.terra.api.platform.block.data.Slab;
import com.dfsek.terra.api.platform.block.data.Stairs;
import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.transform.MapTransform;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.BukkitPlayer;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockData;
import com.dfsek.terra.bukkit.world.inventory.meta.BukkitEnchantment;
import org.bukkit.Location;
import org.bukkit.TreeType;
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
        switch(shape) {
            case STRAIGHT:
                return Stairs.Shape.STRAIGHT;
            case INNER_LEFT:
                return Stairs.Shape.INNER_LEFT;
            case OUTER_LEFT:
                return Stairs.Shape.OUTER_LEFT;
            case INNER_RIGHT:
                return Stairs.Shape.INNER_RIGHT;
            case OUTER_RIGHT:
                return Stairs.Shape.OUTER_RIGHT;
            default:
                throw new IllegalStateException();
        }
    }

    public static BlockData adapt(org.bukkit.block.data.BlockData data) {
        return BukkitBlockData.newInstance(data);
    }

    public static org.bukkit.block.data.BlockData adapt(BlockData data) {
        return ((BukkitBlockData) data).getHandle();
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

    public static Bisected.Half adapt(org.bukkit.block.data.Bisected.Half half) {
        switch(half) {
            case BOTTOM:
                return Bisected.Half.BOTTOM;
            case TOP:
                return Bisected.Half.TOP;
            default:
                throw new IllegalStateException();
        }
    }

    public static BlockFace adapt(org.bukkit.block.BlockFace face) {
        switch(face) {
            case DOWN:
                return BlockFace.DOWN;
            case UP:
                return BlockFace.UP;
            case NORTH_WEST:
                return BlockFace.NORTH_WEST;
            case NORTH_EAST:
                return BlockFace.NORTH_EAST;
            case SOUTH_EAST:
                return BlockFace.SOUTH_EAST;
            case SOUTH_WEST:
                return BlockFace.SOUTH_WEST;
            case NORTH_NORTH_WEST:
                return BlockFace.NORTH_NORTH_WEST;
            case WEST_NORTH_WEST:
                return BlockFace.WEST_NORTH_WEST;
            case WEST_SOUTH_WEST:
                return BlockFace.WEST_SOUTH_WEST;
            case SOUTH_SOUTH_WEST:
                return BlockFace.SOUTH_SOUTH_WEST;
            case EAST_NORTH_EAST:
                return BlockFace.EAST_NORTH_EAST;
            case WEST:
                return BlockFace.WEST;
            case SOUTH:
                return BlockFace.SOUTH;
            case EAST:
                return BlockFace.EAST;
            case NORTH:
                return BlockFace.NORTH;
            case SELF:
                return BlockFace.SELF;
            case EAST_SOUTH_EAST:
                return BlockFace.EAST_SOUTH_EAST;
            case NORTH_NORTH_EAST:
                return BlockFace.NORTH_NORTH_EAST;
            case SOUTH_SOUTH_EAST:
                return BlockFace.SOUTH_SOUTH_EAST;
            default:
                throw new IllegalStateException();
        }
    }

    public static Slab.Type adapt(org.bukkit.block.data.type.Slab.Type type) {
        switch(type) {
            case BOTTOM:
                return Slab.Type.BOTTOM;
            case TOP:
                return Slab.Type.TOP;
            case DOUBLE:
                return Slab.Type.DOUBLE;
            default:
                throw new IllegalStateException();
        }
    }

    public static RedstoneWire.Connection adapt(org.bukkit.block.data.type.RedstoneWire.Connection connection) {
        switch(connection) {
            case NONE:
                return RedstoneWire.Connection.NONE;
            case UP:
                return RedstoneWire.Connection.UP;
            case SIDE:
                return RedstoneWire.Connection.SIDE;
            default:
                throw new IllegalStateException();
        }
    }

    public static org.bukkit.block.data.type.RedstoneWire.Connection adapt(RedstoneWire.Connection connection) {
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

    public static org.bukkit.block.data.type.Stairs.Shape adapt(Stairs.Shape shape) {
        switch(shape) {
            case STRAIGHT:
                return org.bukkit.block.data.type.Stairs.Shape.STRAIGHT;
            case INNER_LEFT:
                return org.bukkit.block.data.type.Stairs.Shape.INNER_LEFT;
            case OUTER_LEFT:
                return org.bukkit.block.data.type.Stairs.Shape.OUTER_LEFT;
            case INNER_RIGHT:
                return org.bukkit.block.data.type.Stairs.Shape.INNER_RIGHT;
            case OUTER_RIGHT:
                return org.bukkit.block.data.type.Stairs.Shape.OUTER_RIGHT;
            default:
                throw new IllegalStateException();
        }
    }

    public static Rail.Shape adapt(org.bukkit.block.data.Rail.Shape shape) {
        switch(shape) {
            case SOUTH_WEST:
                return Rail.Shape.SOUTH_WEST;
            case SOUTH_EAST:
                return Rail.Shape.SOUTH_EAST;
            case NORTH_EAST:
                return Rail.Shape.NORTH_EAST;
            case NORTH_WEST:
                return Rail.Shape.NORTH_WEST;
            case ASCENDING_EAST:
                return Rail.Shape.ASCENDING_EAST;
            case ASCENDING_WEST:
                return Rail.Shape.ASCENDING_WEST;
            case ASCENDING_SOUTH:
                return Rail.Shape.ASCENDING_SOUTH;
            case ASCENDING_NORTH:
                return Rail.Shape.ASCENDING_NORTH;
            case NORTH_SOUTH:
                return Rail.Shape.NORTH_SOUTH;
            case EAST_WEST:
                return Rail.Shape.EAST_WEST;
            default:
                throw new IllegalStateException();
        }
    }

    public static org.bukkit.block.data.Rail.Shape adapt(Rail.Shape shape) {
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


    public static org.bukkit.block.data.Bisected.Half adapt(Bisected.Half half) {
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

    public static org.bukkit.block.BlockFace adapt(BlockFace face) {
        switch(face) {
            case DOWN:
                return org.bukkit.block.BlockFace.DOWN;
            case UP:
                return org.bukkit.block.BlockFace.UP;
            case NORTH_WEST:
                return org.bukkit.block.BlockFace.NORTH_WEST;
            case NORTH_EAST:
                return org.bukkit.block.BlockFace.NORTH_EAST;
            case SOUTH_EAST:
                return org.bukkit.block.BlockFace.SOUTH_EAST;
            case SOUTH_WEST:
                return org.bukkit.block.BlockFace.SOUTH_WEST;
            case NORTH_NORTH_WEST:
                return org.bukkit.block.BlockFace.NORTH_NORTH_WEST;
            case WEST_NORTH_WEST:
                return org.bukkit.block.BlockFace.WEST_NORTH_WEST;
            case WEST_SOUTH_WEST:
                return org.bukkit.block.BlockFace.WEST_SOUTH_WEST;
            case SOUTH_SOUTH_WEST:
                return org.bukkit.block.BlockFace.SOUTH_SOUTH_WEST;
            case EAST_NORTH_EAST:
                return org.bukkit.block.BlockFace.EAST_NORTH_EAST;
            case WEST:
                return org.bukkit.block.BlockFace.WEST;
            case SOUTH:
                return org.bukkit.block.BlockFace.SOUTH;
            case EAST:
                return org.bukkit.block.BlockFace.EAST;
            case NORTH:
                return org.bukkit.block.BlockFace.NORTH;
            case SELF:
                return org.bukkit.block.BlockFace.SELF;
            case EAST_SOUTH_EAST:
                return org.bukkit.block.BlockFace.EAST_SOUTH_EAST;
            case NORTH_NORTH_EAST:
                return org.bukkit.block.BlockFace.NORTH_NORTH_EAST;
            case SOUTH_SOUTH_EAST:
                return org.bukkit.block.BlockFace.SOUTH_SOUTH_EAST;
            default:
                throw new IllegalStateException();
        }
    }

    public static org.bukkit.block.data.type.Slab.Type adapt(Slab.Type type) {
        switch(type) {
            case TOP:
                return org.bukkit.block.data.type.Slab.Type.TOP;
            case DOUBLE:
                return org.bukkit.block.data.type.Slab.Type.DOUBLE;
            case BOTTOM:
                return org.bukkit.block.data.type.Slab.Type.BOTTOM;
            default:
                throw new IllegalStateException();
        }
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

    public static Player adapt(com.dfsek.terra.api.platform.Player player) {
        return ((BukkitPlayer) player).getHandle();
    }

    public static com.dfsek.terra.api.platform.Player adapt(Player player) {
        return new BukkitPlayer(player);
    }
}
