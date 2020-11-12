package com.dfsek.terra.generation.entities;

import com.dfsek.terra.structure.Rotation;
import com.dfsek.terra.structure.Structure;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.math.ProbabilityCollection;

import java.util.Random;
import java.util.Set;

public class Tree implements GenerationEntity {
    private final Set<Material> spawnable;
    private final String id;
    private final int yOffset;
    private final ProbabilityCollection<Structure> structure;

    public Tree(Set<Material> spawnable, String id, int yOffset, ProbabilityCollection<Structure> structure) {
        this.spawnable = spawnable;
        this.id = id;
        this.yOffset = yOffset;
        this.structure = structure;
    }

    @Override
    public void generate(Location location, Random random, JavaPlugin plugin) {
        location.subtract(0, 1, 0);
        Location mut = location.clone().subtract(0, yOffset, 0);
        if(!spawnable.contains(location.getBlock().getType()))
            return;
        Structure structure = this.structure.get(random);
        Rotation rotation = Rotation.fromDegrees(random.nextInt(4) * 90);
        if(!structure.checkSpawns(mut, rotation))
            return;
        structure.paste(mut, rotation);
    }

    @Override
    public boolean isValidLocation(Location location, JavaPlugin plugin) {
        return spawnable.contains(location.getBlock().getType());
    }
}
