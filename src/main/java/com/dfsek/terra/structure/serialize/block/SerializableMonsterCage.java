package com.dfsek.terra.structure.serialize.block;

import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class SerializableMonsterCage implements SerializableBlockState {
    private static final long serialVersionUID = 529892860847864007L;
    private final EntityType type;
    private final int minDelay;
    private final int maxDelay;
    private final int maxNear;
    private final int playerRange;
    private final int delay;
    private final int count;

    public SerializableMonsterCage(CreatureSpawner orig) {
        this.type = orig.getSpawnedType();
        this.minDelay = orig.getMinSpawnDelay();
        this.maxDelay = orig.getMaxSpawnDelay();
        this.maxNear = orig.getMaxNearbyEntities();
        this.playerRange = orig.getRequiredPlayerRange();
        this.delay = orig.getDelay();
        this.count = orig.getSpawnCount();
    }

    @Override
    public BlockState getState(BlockState orig) {
        if(!(orig instanceof CreatureSpawner))
            throw new IllegalArgumentException("BlockState is not a Monster Spawner!");
        CreatureSpawner spawner = (CreatureSpawner) orig;
        spawner.setSpawnedType(type);
        spawner.setMinSpawnDelay(minDelay);
        spawner.setMaxSpawnDelay(maxDelay);
        spawner.setMaxNearbyEntities(maxNear);
        spawner.setRequiredPlayerRange(playerRange);
        spawner.setDelay(delay);
        spawner.setSpawnCount(count);
        return spawner;
    }
}
