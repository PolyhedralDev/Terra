package com.dfsek.terra.api.gaea.tree.fractal;

import com.dfsek.terra.api.gaea.util.GlueList;
import com.dfsek.terra.api.generic.Entity;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.api.generic.world.vector.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;


public abstract class FractalTree {
    private final Map<Location, BlockData> treeAssembler = new HashMap<>();
    private final List<EntitySpawnHolder> entities = new GlueList<>();
    private final Location origin;
    private final Random random;
    private final TerraPlugin main;

    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public FractalTree(Location origin, Random random, TerraPlugin plugin) {
        this.origin = origin.add(0, 1, 0);
        this.random = random;
        this.main = plugin;
    }

    /**
     * Gets the raw tree map.
     *
     * @return HashMap&lt;Location, BlockData&gt; - The raw dictionary representation of the tree.
     */
    public Map<Location, BlockData> getTree() {
        return treeAssembler;
    }


    /**
     * Fetches the Random object used to generate the tree.
     *
     * @return Random - The Random object.
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Fetches the origin location.
     *
     * @return Location - The origin location specified upon instantiation.
     */
    public Location getOrigin() {
        return origin;
    }

    /**
     * Sets a block in the tree's storage map to a material.
     *
     * @param l - The location to set.
     * @param m - The material to which it will be set.
     */
    public void setBlock(Location l, BlockData m) {
        treeAssembler.put(l, m);
    }

    public TerraPlugin getMain() {
        return main;
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    public abstract void grow();

    /**
     * Pastes the tree in the world. Must be invoked from main thread.
     */
    @SuppressWarnings("unchecked")
    public void plant() {
        for(Map.Entry<Location, BlockData> entry : treeAssembler.entrySet()) {
            entry.getKey().getBlock().setBlockData(entry.getValue(), false);
        }
        for(EntitySpawnHolder e : entities) {
            Objects.requireNonNull(e.getLocation().getWorld()).spawn(e.getLocation(), e.getEntity(), e.getConsumer());
        }
    }

    @SuppressWarnings("rawtypes, unchecked")
    public void spawnEntity(Location spawn, Class clazz, Consumer<Entity> consumer) {
        entities.add(new EntitySpawnHolder(spawn, clazz, consumer));
    }

    /**
     * Gets the material at the specified block.
     * Returns air if no material has been set.
     *
     * @param l - The location at which to check.
     * @return Material - The material at the specified block.
     */
    public MaterialData getMaterial(Location l) {
        return treeAssembler.getOrDefault(l, main.getWorldHandle().createBlockData("minecraft:air")).getMaterial();
    }


}
