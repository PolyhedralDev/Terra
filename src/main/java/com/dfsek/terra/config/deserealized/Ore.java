package com.dfsek.terra.config.deserealized;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.Set;

/**
 * This selects which value based on whether the key {@code type} is
 * {@code multi}, {@code single}, or {@code vanilla}.
 */
@SuppressWarnings("DefaultAnnotationParam")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MultiChunkOre.class, name = "multi"),
        @JsonSubTypes.Type(value = SingleChunkOre.class, name = "single"),
        @JsonSubTypes.Type(value = VanillaOre.class, name = "vanilla")
})
public abstract class Ore implements Generateable {
    protected BlockData material;
    protected double deform = 0.75;
    @JsonProperty("deform-frequency")
    protected double deformFrequency = 0.1;
    protected String id;
    protected boolean update = false;
    @JsonProperty("chunk-edge-offset")
    protected int chunkEdgeOffset = 0;
    @JsonProperty("replace")
    protected Set<Material> replaceable;

    public int getChunkEdgeOffset() {
        return chunkEdgeOffset;
    }

    /**
     * Generate an ore.
     *
     * @param location Location to generate the ore at.
     * @param random   Random used for generation.
     * @param plugin   Plugin reference.
     */
    @Override
    public abstract void generate(Location location, Random random, JavaPlugin plugin);

    public boolean isValidLocation(Location location, JavaPlugin plugin) {
        Block block = location.getBlock();
        return (replaceable.contains(block.getType()) && (block.getLocation().getY() >= 0));
    }

    protected int randomInRange(Random r, int min, int max) {
        return r.nextInt(max - min + 1) + min;
    }
}
