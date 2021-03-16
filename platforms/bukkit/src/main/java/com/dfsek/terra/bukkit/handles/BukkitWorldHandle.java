package com.dfsek.terra.bukkit.handles;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.bukkit.structure.WorldEditUtil;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockData;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;
import org.bukkit.Bukkit;

public class BukkitWorldHandle implements WorldHandle {

    @Override
    public BlockData createBlockData(String data) {
        org.bukkit.block.data.BlockData bukkitData = Bukkit.createBlockData(data);
        return BukkitBlockData.newInstance(bukkitData);
    }

    @Override
    public EntityType getEntity(String id) {
        return new BukkitEntityType(org.bukkit.entity.EntityType.valueOf(id));
    }

    @Override
    public Pair<Location, Location> getSelectedLocation(Player player) {
        org.bukkit.Location[] locations = WorldEditUtil.getSelectionLocations(BukkitAdapter.adapt(player));
        return new Pair<>(BukkitAdapter.adapt(locations[0]), BukkitAdapter.adapt(locations[1]));
    }
}
