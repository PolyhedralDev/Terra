package com.dfsek.terra.generation.entities;

import com.dfsek.terra.carving.UserDefinedCarver;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.math.ProbabilityCollection;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Carver implements GenerationEntity {
    private final UserDefinedCarver carver;
    private final String id;
    private final Set<Material> update;
    private final Map<Material, Set<Material>> shift;
    private final Map<Integer, ProbabilityCollection<BlockData>> inner;
    private final Map<Integer, ProbabilityCollection<BlockData>> outer;
    private final Map<Integer, ProbabilityCollection<BlockData>> top;
    private final Map<Integer, ProbabilityCollection<BlockData>> bottom;
    private final boolean updateOcean;
    private final boolean replaceIsBlacklistInner;
    private final boolean replaceIsBlacklistOuter;
    private final boolean replaceIsBlacklistTop;
    private final boolean replaceIsBlacklistBottom;
    private final Set<Material> replaceableInner;
    private final Set<Material> replaceableOuter;
    private final Set<Material> replaceableTop;
    private final Set<Material> replaceableBottom;

    public Carver(UserDefinedCarver carver, String id, Set<Material> update, Map<Material, Set<Material>> shift,
                  Map<Integer, ProbabilityCollection<BlockData>> inner, Map<Integer, ProbabilityCollection<BlockData>> outer,
                  Map<Integer, ProbabilityCollection<BlockData>> top, Map<Integer, ProbabilityCollection<BlockData>> bottom,
                  boolean updateOcean, ReplaceableCarverConfig config) {
        this.carver = carver;
        this.id = id;
        this.update = update;
        this.shift = shift;
        this.inner = inner;
        this.outer = outer;
        this.top = top;
        this.bottom = bottom;
        this.updateOcean = updateOcean;
        this.replaceIsBlacklistInner = config.replaceIsBlacklistInner;
        this.replaceIsBlacklistOuter = config.replaceIsBlacklistOuter;
        this.replaceIsBlacklistTop = config.replaceIsBlacklistTop;
        this.replaceIsBlacklistBottom = config.replaceIsBlacklistBottom;
        this.replaceableInner = config.replaceableInner;
        this.replaceableOuter = config.replaceableOuter;
        this.replaceableTop = config.replaceableTop;
        this.replaceableBottom = config.replaceableBottom;
    }

    @Override
    public void generate(Location location, Random random, JavaPlugin plugin) {
        //TODO
    }

    @Override
    public boolean isValidLocation(Location location, JavaPlugin plugin) {
        return false; //TODO
    }

    public static class ReplaceableCarverConfig {
        private final boolean replaceIsBlacklistInner;
        private final boolean replaceIsBlacklistOuter;
        private final boolean replaceIsBlacklistTop;
        private final boolean replaceIsBlacklistBottom;
        private final Set<Material> replaceableInner;
        private final Set<Material> replaceableOuter;
        private final Set<Material> replaceableTop;
        private final Set<Material> replaceableBottom;

        public ReplaceableCarverConfig(boolean replaceIsBlacklistInner, boolean replaceIsBlacklistOuter, boolean replaceIsBlacklistTop, boolean replaceIsBlacklistBottom, Set<Material> replaceableInner, Set<Material> replaceableOuter, Set<Material> replaceableTop, Set<Material> replaceableBottom) {
            this.replaceIsBlacklistInner = replaceIsBlacklistInner;
            this.replaceIsBlacklistOuter = replaceIsBlacklistOuter;
            this.replaceIsBlacklistTop = replaceIsBlacklistTop;
            this.replaceIsBlacklistBottom = replaceIsBlacklistBottom;
            this.replaceableInner = replaceableInner;
            this.replaceableOuter = replaceableOuter;
            this.replaceableTop = replaceableTop;
            this.replaceableBottom = replaceableBottom;
        }
    }
}
