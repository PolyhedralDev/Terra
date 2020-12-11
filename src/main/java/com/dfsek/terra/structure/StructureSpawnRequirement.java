package com.dfsek.terra.structure;

import com.dfsek.terra.api.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.structure.spawn.AirSpawn;
import com.dfsek.terra.structure.spawn.BlankSpawn;
import com.dfsek.terra.structure.spawn.LandSpawn;
import com.dfsek.terra.structure.spawn.OceanSpawn;
import com.dfsek.terra.structure.spawn.Requirement;
import org.bukkit.World;

import java.io.Serializable;

public enum StructureSpawnRequirement implements Serializable {
    AIR {
        @Override
        public Requirement getInstance(World world, TerraBukkitPlugin main) {
            return new AirSpawn(world, main);
        }
    }, OCEAN {
        @Override
        public Requirement getInstance(World world, TerraBukkitPlugin main) {
            return new OceanSpawn(world, main);
        }
    }, LAND {
        @Override
        public Requirement getInstance(World world, TerraBukkitPlugin main) {
            return new LandSpawn(world, main);
        }
    }, BLANK {
        @Override
        public Requirement getInstance(World world, TerraBukkitPlugin main) {
            return new BlankSpawn();
        }
    };
    private static final long serialVersionUID = -175639605885943679L;

    public abstract Requirement getInstance(World world, TerraBukkitPlugin main);
}
