package com.dfsek.terra.generation.items.tree;

import com.dfsek.terra.generation.items.PlaceableLayer;
import com.dfsek.terra.procgen.math.Vector2;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.polydev.gaea.GaeaPlugin;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;

import java.util.Random;

public class TreeLayer extends PlaceableLayer<Tree> {
    private final GaeaPlugin main;

    public TreeLayer(double density, Range level, ProbabilityCollection<Tree> layer, FastNoiseLite noise, GaeaPlugin main) {
        super(density, level, layer, noise);
        this.main = main;
    }

    @Override
    public void place(Chunk chunk, Vector2 coords, Random random) {
        Tree item = layer.get(random);
        Block current = chunk.getBlock((int) coords.getX(), level.getMax(), (int) coords.getZ());
        for(int ignored : level) {
            current = current.getRelative(BlockFace.DOWN);
            if(item.getSpawnable().contains(current.getType())) {
                if(item instanceof TreeType && current.getRelative(BlockFace.UP).isEmpty())
                    item.plant(current.getLocation().add(0, 1, 0), random, main);
                else if(!(item instanceof TreeType)) item.plant(current.getLocation(), random, main);
            }
        }
    }
}
