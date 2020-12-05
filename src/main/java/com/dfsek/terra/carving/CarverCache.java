package com.dfsek.terra.carving;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.TerraBiomeGrid;
import com.dfsek.terra.config.base.PluginConfig;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.MathUtil;
import org.polydev.gaea.util.FastRandom;
import org.polydev.gaea.util.GlueList;
import org.polydev.gaea.world.carving.Worm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CarverCache {
    private final Map<Long, List<Worm.WormPoint>> carvers = new HashMap<>();

    public List<Worm.WormPoint> getPoints(int chunkX, int chunkZ, World w, UserDefinedCarver carver) {
        if(carvers.size() > PluginConfig.getCacheSize() * 2) carvers.clear();
        return carvers.computeIfAbsent((((long) chunkX) << 32) | (chunkZ & 0xffffffffL) ^ w.getSeed(), key -> {
            TerraBiomeGrid grid = TerraWorld.getWorld(w).getGrid();
            if(carver.isChunkCarved(w, chunkX, chunkZ, new FastRandom(MathUtil.hashToLong(this.getClass().getName() + "_" + chunkX + "&" + chunkZ)))) {
                long seed = MathUtil.getCarverChunkSeed(chunkX, chunkZ, w.getSeed());
                carver.getSeedVar().setValue(seed);
                Random r = new FastRandom(seed);
                Worm carving = carver.getWorm(seed, new Vector((chunkX << 4) + r.nextInt(16), carver.getConfig().getHeight().get(r), (chunkZ << 4) + r.nextInt(16)));
                List<Worm.WormPoint> points = new GlueList<>();
                for(int i = 0; i < carving.getLength(); i++) {
                    carving.step();
                    Biome biome = grid.getBiome(carving.getRunning().toLocation(w), GenerationPhase.POPULATE);
                    if(!((UserDefinedBiome) biome).getConfig().getCarvers().containsKey(carver)) { // Stop if we enter a biome this carver is not present in
                        return new GlueList<>();
                    }
                    points.add(carving.getPoint());
                }
                return points;
            }
            return new GlueList<>();
        });
    }
}
