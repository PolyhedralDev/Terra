package com.dfsek.terra.generation.deserelized;

import com.dfsek.terra.structure.Rotation;
import com.dfsek.terra.structure.Structure;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.math.ProbabilityCollection;

import java.util.Random;
import java.util.Set;

@SuppressWarnings({"unused", "SpellCheckingInspection", "MismatchedQueryAndUpdateOfCollection"})
public class Tree implements GenerationEntity {
    private Set<Material> spawnable;
    private String id;
    @JsonProperty("y-offset")
    private int yOffset;
    @JsonProperty("files")
    private ProbabilityCollection<Structure> structure;

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
