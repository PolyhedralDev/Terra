package com.dfsek.terra.generation.items.tree;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.world.tree.Tree;
import com.dfsek.terra.util.MaterialSet;

import java.util.Random;

public class TerraTree implements Tree {
    private final MaterialSet spawnable;
    private final int yOffset;
    private final ProbabilityCollection<Void> structure;

    public TerraTree(MaterialSet spawnable, int yOffset, ProbabilityCollection<Void> structure) {
        this.spawnable = spawnable;
        this.yOffset = yOffset;
        this.structure = structure;
    }

    @Override
    public boolean plant(Location location, Random random) {
        /*
        Location mut = location.clone().subtract(0, yOffset, 0);
        if(!spawnable.contains(location.getBlock().getType())) return false;
        Structure struc = structure.get(random);
        Rotation rotation = Rotation.fromDegrees(random.nextInt(4) * 90);
        if(!struc.checkSpawns(mut, rotation, null)) return false;
        struc.paste(mut, rotation, null);*/
        return true;


    }

    @Override
    public MaterialSet getSpawnable() {
        return spawnable;
    }

    public boolean plantBlockCheck(Location location, Random random, TerraPlugin main) {
        /*
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
        struc.paste(mut, rotation, main);

         */
        return true;
    }
}
