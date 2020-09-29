package com.dfsek.terra.structure;

import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.biome.Generator;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.FastNoise;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StructureSpawnRequirement  implements Serializable {
    private static final long serialVersionUID = -175639605885943679L;
    private static final transient Map<World, FastNoise> noiseMap = new HashMap<>();
    private final RequirementType type;
    private final int x, y, z;

    public StructureSpawnRequirement(RequirementType type, int x, int y, int z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public RequirementType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public boolean isValidSpawn(Location origin) {
        return type.matches(origin.getWorld(), origin.getBlockX()+x, origin.getBlockY()+y, origin.getBlockZ()+z);
    }

    public enum RequirementType {
        AIR {
            @Override
            public boolean matches(World w, int x, int y, int z) {
                UserDefinedBiome b = (UserDefinedBiome) TerraBiomeGrid.fromWorld(w).getBiome(x, z, GenerationPhase.POPULATE);
                if(y <= BiomeConfig.fromBiome(b).getSeaLevel()) return false;
                return b.getGenerator().getNoise(getNoise(w), w, x, y, z) <= 0;
            }
        }, OCEAN {
            @Override
            public boolean matches(World w, int x, int y, int z) {
                UserDefinedBiome b = (UserDefinedBiome) TerraBiomeGrid.fromWorld(w).getBiome(x, z, GenerationPhase.POPULATE);
                if(y > BiomeConfig.fromBiome(b).getSeaLevel()) return false;
                return b.getGenerator().getNoise(getNoise(w), w, x, y, z) <= 0;
            }
        }, LAND {
            @Override
            public boolean matches(World w, int x, int y, int z) {
                UserDefinedBiome b = (UserDefinedBiome) TerraBiomeGrid.fromWorld(w).getBiome(x, z, GenerationPhase.POPULATE);
                return b.getGenerator().getNoise(getNoise(w), w, x, y, z) > 0;
            }
        };
        public abstract boolean matches(World w, int x, int y, int z);
    }
    public static void putNoise(World w, FastNoise noise) {
        noiseMap.putIfAbsent(w, noise);
    }
    private static FastNoise getNoise(World w) {
        return noiseMap.get(w);
    }
}
