package com.dfsek.terra.generation.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.Set;

public abstract class Ore implements GenerationEntity {
    protected final BlockData oreData;
    protected final double deform;
    protected final double deformFrequency;
    protected final String id;
    protected final boolean update;
    protected final int chunkEdgeOffset;
    protected final Set<Material> replaceable;

    public Ore(BlockData oreData, double deform, double deformFrequency, String id, boolean update,
               int chunkEdgeOffset, Set<Material> replaceable) {
        this.oreData = oreData;
        this.deform = deform;
        this.deformFrequency = deformFrequency;
        this.id = id;
        this.update = update;
        this.chunkEdgeOffset = chunkEdgeOffset;
        this.replaceable = replaceable;
    }

//    @Override
//    public abstract void generate(Location location, Random random, JavaPlugin plugin);

    @Override
    public boolean isValidLocation(Location location, JavaPlugin plugin) {
        Block block = location.getBlock();
        return (replaceable.contains(block.getType()) && (block.getLocation().getY() >= 0));
    }

    protected int randomInRange(Random r, int min, int max) {
        return r.nextInt(max - min + 1) + min;
    }
}
