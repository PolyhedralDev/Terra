package com.dfsek.terra.forge.world.block.state;

import com.dfsek.terra.api.platform.block.state.MobSpawner;
import com.dfsek.terra.api.platform.block.state.SerialState;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.forge.TerraForgePlugin;
import com.dfsek.terra.forge.world.ForgeAdapter;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class ForgeMobSpawner extends ForgeBlockState implements MobSpawner { // TODO: finish implementation / refactor API because bukkit doesnt expose most of the stuff spawners can do


    public ForgeMobSpawner(MobSpawnerTileEntity blockEntity, IWorld worldAccess) {
        super(blockEntity, worldAccess);
    }

    @Override
    public EntityType getSpawnedType() {
        return ForgeAdapter.adapt(ForgeRegistries.ENTITIES.getValue(((MobSpawnerTileEntity) blockEntity).getSpawner().getSpawnerEntity().getType().getRegistryName()));
    }

    @Override
    public void setSpawnedType(@NotNull EntityType creatureType) {
        ((MobSpawnerTileEntity) blockEntity).getSpawner().setEntityId(ForgeAdapter.adapt(creatureType));
    }

    @Override
    public int getDelay() {
        return 0;
    }

    @Override
    public void setDelay(int delay) {

    }

    @Override
    public int getMinSpawnDelay() {
        return 0;
    }

    @Override
    public void setMinSpawnDelay(int delay) {

    }

    @Override
    public int getMaxSpawnDelay() {
        return 0;
    }

    @Override
    public void setMaxSpawnDelay(int delay) {

    }

    @Override
    public int getSpawnCount() {
        return 0;
    }

    @Override
    public void setSpawnCount(int spawnCount) {

    }

    @Override
    public int getMaxNearbyEntities() {
        return 0;
    }

    @Override
    public void setMaxNearbyEntities(int maxNearbyEntities) {

    }

    @Override
    public int getRequiredPlayerRange() {
        return 0;
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {

    }

    @Override
    public int getSpawnRange() {
        return 0;
    }

    @Override
    public void setSpawnRange(int spawnRange) {

    }

    @Override
    public void applyState(String state) {
        SerialState.parse(state).forEach((k, v) -> {
            switch(k) {
                case "type":
                    setSpawnedType(TerraForgePlugin.getInstance().getWorldHandle().getEntity(v));
                    return;
                case "delay":
                    setDelay(Integer.parseInt(v));
                    return;
                case "min_delay":
                    setMinSpawnDelay(Integer.parseInt(v));
                    return;
                case "max_delay":
                    setMaxSpawnDelay(Integer.parseInt(v));
                    return;
                case "spawn_count":
                    setSpawnCount(Integer.parseInt(v));
                    return;
                case "spawn_range":
                    setSpawnRange(Integer.parseInt(v));
                    return;
                case "max_nearby":
                    setMaxNearbyEntities(Integer.parseInt(v));
                    return;
                case "required_player_range":
                    setRequiredPlayerRange(Integer.parseInt(v));
                    return;
                default:
                    throw new IllegalArgumentException("Invalid property: " + k);
            }
        });
    }
}
