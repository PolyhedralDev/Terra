package com.dfsek.terra.generation.items;

import com.dfsek.terra.procgen.math.Vector2;
import com.dfsek.terra.structure.Rotation;
import com.dfsek.terra.structure.Structure;
import com.dfsek.terra.structure.StructureContainedBlock;
import com.dfsek.terra.structure.StructureInfo;
import com.dfsek.terra.util.structure.RotationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;

import java.util.Random;
import java.util.Set;

public class TerraTree implements Tree {
    private final Set<Material> spawnable;
    private final int yOffset;
    private final ProbabilityCollection<Structure> structure;

    public TerraTree(Set<Material> spawnable, int yOffset, ProbabilityCollection<Structure> structure) {
        this.spawnable = spawnable;
        this.yOffset = yOffset;
        this.structure = structure;
    }

    @Override
    public boolean plant(Location location, Random random, JavaPlugin javaPlugin) {
        location.subtract(0, 1, 0);
        Location mut = location.clone().subtract(0, yOffset, 0);
        if(!spawnable.contains(location.getBlock().getType())) return false;
        Structure struc = structure.get(random);
        Rotation rotation = Rotation.fromDegrees(random.nextInt(4) * 90);
        if(!struc.checkSpawns(mut, rotation)) return false;
        struc.paste(mut, rotation);
        return true;
    }

    @Override
    public Set<Material> getSpawnable() {
        return spawnable;
    }

    public boolean plantBlockCheck(Location location, Random random) {
        location.subtract(0, 1, 0);
        Location mut = location.clone().subtract(0, yOffset, 0);
        if(!spawnable.contains(location.getBlock().getType())) return false;
        Structure struc = structure.get(random);
        Rotation rotation = Rotation.fromDegrees(random.nextInt(4) * 90);
        StructureInfo info = struc.getStructureInfo();
        for(StructureContainedBlock spawn : struc.getSpawns()) {
            Vector2 rot = RotationUtil.getRotatedCoords(new Vector2(spawn.getX() - info.getCenterX(), spawn.getZ() - info.getCenterZ()), rotation);
            int x = (int) rot.getX();
            int z = (int) rot.getZ();
            switch(spawn.getRequirement()) {
                case AIR:
                    if(!mut.clone().add(x, spawn.getY() - 1, z).getBlock().isPassable()) return false;
                    break;
                case LAND:
                    if(!mut.clone().add(x, spawn.getY() - 1, z).getBlock().getType().isSolid()) return false;
                    break;
            }
        }
        struc.paste(mut, rotation);
        return true;
    }
}
