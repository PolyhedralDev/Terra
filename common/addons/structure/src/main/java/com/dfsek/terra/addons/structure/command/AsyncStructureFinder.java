package com.dfsek.terra.addons.structure.command;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.structure.ConfiguredStructure;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Consumer;

public class AsyncStructureFinder extends AsyncFeatureFinder<ConfiguredStructure> {
    public AsyncStructureFinder(BiomeProvider provider, ConfiguredStructure target, @NotNull Vector3 origin, World world, int startRadius, int maxRadius, Consumer<Vector3> callback, TerraPlugin main) {
        super(provider, target, origin, world, startRadius, maxRadius, callback, main);
        setSearchSize(target.getSpawn().getWidth() + 2 * target.getSpawn().getSeparation());
    }

    @Override
    public Vector3 finalizeVector(Vector3 orig) {
        return target.getSpawn().getChunkSpawn(orig.getBlockX(), orig.getBlockZ(), world.getSeed());
    }

    @Override
    public boolean isValid(int x, int z, ConfiguredStructure target) {
        Vector3 spawn = target.getSpawn().getChunkSpawn(x, z, world.getSeed());
        if(!((UserDefinedBiome) provider.getBiome(spawn)).getConfig().getStructures().contains(target)) return false;
        Random random = new Random(PopulationUtil.getCarverChunkSeed(FastMath.floorDiv(spawn.getBlockX(), 16), FastMath.floorDiv(spawn.getBlockZ(), 16), world.getSeed()));
        return target.getStructure().get(random).test(spawn.setY(target.getSpawnStart().get(random)), world, random, Rotation.fromDegrees(90 * random.nextInt(4)));
    }
}
