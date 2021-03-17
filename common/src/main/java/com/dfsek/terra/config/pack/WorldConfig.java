package com.dfsek.terra.config.pack;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.structures.loot.LootTable;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.flora.Flora;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.registry.OpenRegistry;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.generation.math.SamplerCache;
import com.dfsek.terra.world.population.items.TerraStructure;
import com.dfsek.terra.world.population.items.ores.Ore;

import java.util.Set;

public class WorldConfig {
    private final LockedRegistry<StructureScript> scriptRegistry;
    private final LockedRegistry<TerraBiome> biomeRegistry;
    private final SamplerCache samplerCache;
    private final LockedRegistry<UserDefinedCarver> carverRegistry;
    private final LockedRegistry<Tree> treeRegistry;
    private final LockedRegistry<Flora> floraRegistry;
    private final LockedRegistry<LootTable> lootRegistry;
    private final LockedRegistry<Ore> oreRegistry;
    private final LockedRegistry<Palette<BlockData>> paletteRegistry;
    private final LockedRegistry<TerraStructure> structureRegistry;

    private final BiomeProvider provider;

    private final TerraWorld world;
    private final ConfigPack pack;

    public WorldConfig(TerraWorld world, ConfigPack pack, TerraPlugin main) {
        this.world = world;
        this.pack = pack;
        this.samplerCache = new SamplerCache(main, world);
        this.scriptRegistry = new LockedRegistry<>(pack.getScriptRegistry());

        OpenRegistry<TerraBiome> biomeOpenRegistry = new OpenRegistry<>();
        pack.getBiomeRegistry().forEach((id, biome) -> biomeOpenRegistry.add(id, biome.apply(world.getWorld().getSeed())));

        this.biomeRegistry = new LockedRegistry<>(biomeOpenRegistry);
        this.carverRegistry = new LockedRegistry<>(pack.getCarverRegistry());
        this.treeRegistry = new LockedRegistry<>(pack.getTreeRegistry());
        this.floraRegistry = new LockedRegistry<>(pack.getFloraRegistry());
        this.lootRegistry = new LockedRegistry<>(pack.getLootRegistry());
        this.oreRegistry = new LockedRegistry<>(pack.getOreRegistry());
        this.paletteRegistry = new LockedRegistry<>(pack.getPaletteRegistry());
        this.structureRegistry = new LockedRegistry<>(pack.getStructureRegistry());

        this.provider = pack.getBiomeProviderBuilder().build(world.getWorld().getSeed());
    }

    public TerraWorld getWorld() {
        return world;
    }

    public SamplerCache getSamplerCache() {
        return samplerCache;
    }

    public Set<UserDefinedCarver> getCarvers() {
        return carverRegistry.entries();
    }

    public LockedRegistry<StructureScript> getScriptRegistry() {
        return scriptRegistry;
    }

    public LockedRegistry<TerraBiome> getBiomeRegistry() {
        return biomeRegistry;
    }

    public LockedRegistry<Tree> getTreeRegistry() {
        return treeRegistry;
    }

    public LockedRegistry<UserDefinedCarver> getCarverRegistry() {
        return carverRegistry;
    }

    public LockedRegistry<Flora> getFloraRegistry() {
        return floraRegistry;
    }

    public LockedRegistry<LootTable> getLootRegistry() {
        return lootRegistry;
    }

    public LockedRegistry<Ore> getOreRegistry() {
        return oreRegistry;
    }

    public LockedRegistry<Palette<BlockData>> getPaletteRegistry() {
        return paletteRegistry;
    }

    public LockedRegistry<TerraStructure> getStructureRegistry() {
        return structureRegistry;
    }

    public BiomeProvider getProvider() {
        return provider;
    }

    public Set<TerraStructure> getStructures() {
        return structureRegistry.entries();
    }

    public ConfigPackTemplate getTemplate() {
        return pack.getTemplate();
    }
}
