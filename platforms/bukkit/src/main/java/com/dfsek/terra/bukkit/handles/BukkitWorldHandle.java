package com.dfsek.terra.bukkit.handles;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.bukkit.structure.WorldEditUtil;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.BukkitBiome;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Locale;

public class BukkitWorldHandle implements WorldHandle {
    private final BlockState air;

    public BukkitWorldHandle() {
        this.air = BukkitBlockState.newInstance(Material.AIR.createBlockData());
    }

    @Override
    public BlockState createBlockData(String data) {
        org.bukkit.block.data.BlockData bukkitData = Bukkit.createBlockData(data);
        return BukkitBlockState.newInstance(bukkitData);
    }

    @Override
    public BlockState air() {
        return air;
    }

    @Override
    public EntityType getEntity(String id) {
        if(!id.startsWith("minecraft:")) throw new LoadException("Invalid entity identifier " + id);
        return new BukkitEntityType(org.bukkit.entity.EntityType.valueOf(id.toUpperCase(Locale.ROOT).substring(10)));
    }

    @Override
    public BlockEntity createBlockEntity(Vector3 location, BlockState block, String snbt) {
        return null;
    }

    @Override
    public Pair<Vector3, Vector3> getSelectedLocation(Player player) {
        org.bukkit.Location[] locations = WorldEditUtil.getSelectionLocations(BukkitAdapter.adapt(player));
        return Pair.of(BukkitAdapter.adapt(locations[0]), BukkitAdapter.adapt(locations[1]));
    }
}
