package com.dfsek.terra.fabric.util;

import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public final class WorldEditUtil {
    public static Pair<Location, Location> getSelection(Player player) {
        WorldEdit worldEdit = WorldEdit.getInstance();
        try {
            Region selection = worldEdit.getSessionManager()
                    .get(com.sk89q.worldedit.fabric.FabricAdapter.adaptPlayer((ServerPlayerEntity) player))
                    .getSelection(com.sk89q.worldedit.fabric.FabricAdapter.adapt((World) player.getWorld()));
            BlockVector3 min = selection.getMinimumPoint();
            BlockVector3 max = selection.getMaximumPoint();
            Location l1 = new Location(player.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ());
            Location l2 = new Location(player.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
            return Pair.of(l1, l2);
        } catch(IncompleteRegionException e) {
            throw new IllegalStateException("No selection has been made", e);
        }
    }
}
