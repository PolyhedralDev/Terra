package com.dfsek.terra.biome.grid.master;

import com.dfsek.terra.api.gaea.biome.BiomeGrid;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.biome.BiomeZone;
import com.dfsek.terra.biome.grid.UserDefinedGrid;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigPackTemplate;
import com.dfsek.terra.config.builder.biomegrid.BiomeGridBuilder;
import com.dfsek.terra.debug.Debug;

public abstract class TerraBiomeGrid extends BiomeGrid {
    protected final BiomeZone zone;

    public TerraBiomeGrid(long seed, double freq1, double freq2, int sizeX, int sizeZ, BiomeZone zone) {
        super(seed, freq1, freq2, sizeX, sizeZ);
        this.zone = zone;
    }

    public BiomeZone getZone() {
        return zone;
    }

    public abstract UserDefinedGrid getGrid(int x, int z);

    public enum Type {
        RADIAL, STANDARD
    }

    public static final class TerraBiomeGridBuilder {
        private final long seed;
        private final ConfigPack config;
        private final TerraPlugin main;

        public TerraBiomeGridBuilder(long seed, ConfigPack config, TerraPlugin main) {
            this.seed = seed;
            this.config = config;
            this.main = main;
        }

        public TerraBiomeGrid build() {
            ConfigPackTemplate template = config.getTemplate();

            int zoneSize = template.getGrids().size();

            BiomeGrid[] definedGrids = new BiomeGrid[zoneSize];
            for(int i = 0; i < zoneSize; i++) {
                String partName = template.getGrids().get(i);
                try {
                    BiomeGridBuilder g = config.getBiomeGrid(partName);
                    BiomeGrid b = g.build(seed, config);
                    definedGrids[i] = b;
                } catch(NullPointerException e) {
                    Debug.stack(e);
                    main.getLogger().severe("No such BiomeGrid " + partName);
                    main.getLogger().severe("Please check configuration files for errors. Configuration errors will have been reported during initialization.");
                    main.getLogger().severe("ONLY report this to Terra if you are SURE your config is error-free.");
                    main.getLogger().severe("Terrain will NOT generate properly at this point. Correct your config before using your server!");
                }
            }
            BiomeZone zone = new BiomeZone(seed, config, definedGrids);

            if(template.getGridType().equals(TerraBiomeGrid.Type.RADIAL)) {
                BiomeGrid internal = config.getBiomeGrid(template.getRadialInternalGrid()).build(seed, config);
                return new TerraRadialBiomeGrid(seed, template.getGridFreqX(), template.getGridFreqZ(), zone, config, template.getRadialGridRadius(), internal);
            } else return new TerraStandardBiomeGrid(seed, template.getGridFreqX(), template.getGridFreqZ(), zone, config);
        }
    }
}
