package com.dfsek.terra.api.gaea.tree.fractal;

import com.dfsek.terra.api.gaea.math.ProbabilityCollection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class TreeGeometry {
    private final FractalTree tree;

    public TreeGeometry(FractalTree tree) {
        this.tree = tree;
    }

    public static Vector getPerpendicular(Vector v) {
        return v.getZ() < v.getX() ? new Vector(v.getY(), - v.getX(), 0) : new Vector(0, - v.getZ(), v.getY());
    }

    public FractalTree getTree() {
        return tree;
    }

    public void generateSphere(Location l, Material m, int radius, boolean overwrite) {
        generateSphere(l, new ProbabilityCollection<Material>().add(m, 1), radius, overwrite);
    }

    public void generateCylinder(Location l, Material m, int radius, int height, boolean overwrite) {
        generateCylinder(l, new ProbabilityCollection<Material>().add(m, 1), radius, height, overwrite);
    }

    public void generateSphere(Location l, ProbabilityCollection<Material> m, int radius, boolean overwrite) {
        for(int x = - radius; x <= radius; x++) {
            for(int y = - radius; y <= radius; y++) {
                for(int z = - radius; z <= radius; z++) {
                    Vector position = l.toVector().clone().add(new Vector(x, y, z));
                    if(l.toVector().distance(position) <= radius + 0.5 && (overwrite || tree.getMaterial(position.toLocation(l.getWorld())).isAir()))
                        tree.setBlock(position.toLocation(l.getWorld()), m.get());
                }
            }
        }
    }

    public void generateSponge(Location l, ProbabilityCollection<Material> m, int radius, boolean overwrite, int sponginess) {
        for(int x = - radius; x <= radius; x++) {
            for(int y = - radius; y <= radius; y++) {
                for(int z = - radius; z <= radius; z++) {
                    Vector position = l.toVector().clone().add(new Vector(x, y, z));
                    if(tree.getRandom().nextInt(100) < sponginess && l.toVector().distance(position) <= radius + 0.5 && (overwrite || tree.getMaterial(position.toLocation(l.getWorld())).isAir()))
                        tree.setBlock(position.toLocation(l.getWorld()), m.get());
                }
            }
        }
    }

    public void generateCylinder(Location l, ProbabilityCollection<Material> m, int radius, int height, boolean overwrite) {
        for(int x = - radius; x <= radius; x++) {
            for(int y = 0; y <= height; y++) {
                for(int z = - radius; z <= radius; z++) {
                    Vector position = l.toVector().clone().add(new Vector(x, 0, z));
                    if(l.toVector().distance(position) <= radius + 0.5 && (overwrite || tree.getMaterial(position.toLocation(l.getWorld())).isAir()))
                        tree.setBlock(position.toLocation(l.getWorld()), m.get());
                }
            }
        }
    }
}
