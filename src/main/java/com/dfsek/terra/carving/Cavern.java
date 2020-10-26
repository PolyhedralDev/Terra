package com.dfsek.terra.carving;

import com.dfsek.terra.procgen.GridSpawn;
import com.dfsek.terra.procgen.voxel.DeformedSphere;
import com.dfsek.terra.procgen.voxel.Tube;
import com.dfsek.terra.procgen.voxel.VoxelGeometry;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.math.MathUtil;

import java.util.Random;

public class Cavern {
    private final Node node;
    private final long seed;

    public Cavern(World w) {
        this.node = new Node(w);
        this.seed = w.getSeed();
    }

    public VoxelGeometry carveChunk(int chunkX, int chunkZ) {
        long seedC = MathUtil.getCarverChunkSeed(chunkX, chunkZ, seed);
        Random chunk = new Random(seedC);
        Vector org = node.getNodeLocation((chunkX << 4) + 8, (chunkZ << 4) + 8).clone().setY(chunk.nextInt(128));
        VoxelGeometry carve = VoxelGeometry.getBlank();

        FastNoiseLite smpl = new FastNoiseLite((int) seedC);
        smpl.setFrequency(0.01f);
        smpl.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        Bukkit.getLogger().info("Cavern: " + org.toString());
        carve.merge(new DeformedSphere(org.clone(), chunk.nextInt(4) + 3, 0.75, smpl));

        Vector _00 = new Vector(org.getX() + 16, new Random(MathUtil.getCarverChunkSeed(chunkX + 1, chunkZ, seed)).nextInt(128), org.getZ());

        carve.merge(new Tube(org, _00, 4));
        return carve;
    }

    public static class Node {
        private final long seed;
        private final GridSpawn spawn = new GridSpawn(16, 0);

        public Node(World w) {
            this.seed = w.getSeed();
        }

        public Vector getNodeLocation(int x, int z) {
            return spawn.getNearestSpawn(x, z, seed);
        }
    }
}
