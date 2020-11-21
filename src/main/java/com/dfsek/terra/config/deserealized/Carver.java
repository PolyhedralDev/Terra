package com.dfsek.terra.config.deserealized;

import com.dfsek.terra.carving.UserDefinedCarver;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.math.ProbabilityCollection;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Carver implements Generateable {
    private UserDefinedCarver carver;
    private String id;
    private Set<Material> update;
    private Map<Material, Set<Material>> shift;
    private Map<Integer, ProbabilityCollection<BlockData>> inner;
    private Map<Integer, ProbabilityCollection<BlockData>> outer;
    private Map<Integer, ProbabilityCollection<BlockData>> top;
    private Map<Integer, ProbabilityCollection<BlockData>> bottom;
    private boolean updateOcean;
    private boolean replaceIsBlacklistInner;
    private boolean replaceIsBlacklistOuter;
    private boolean replaceIsBlacklistTop;
    private boolean replaceIsBlacklistBottom;
    private Set<Material> replaceableInner;
    private Set<Material> replaceableOuter;
    private Set<Material> replaceableTop;
    private Set<Material> replaceableBottom;

    @Override
    public void generate(Location location, Random random, JavaPlugin plugin) {
        //TODO
    }

    @Override
    public boolean isValidLocation(Location location, JavaPlugin plugin) {
        return false;
        //TODO
    }
}
