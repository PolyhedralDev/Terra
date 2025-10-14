package com.dfsek.terra.allay.generator;

import com.dfsek.terra.allay.Mapping;
import com.dfsek.terra.allay.delegate.AllayWorldProperties;

import com.google.common.base.Preconditions;
import org.allaymc.api.utils.AllayStringUtils;
import org.allaymc.api.world.biome.BiomeType;
import org.allaymc.api.world.chunk.UnsafeChunk;
import org.allaymc.api.world.data.DimensionInfo;
import org.allaymc.api.world.generator.WorldGenerator;
import org.allaymc.api.world.generator.context.NoiseContext;
import org.allaymc.api.world.generator.context.PopulateContext;
import org.allaymc.api.world.generator.function.Noiser;
import org.allaymc.api.world.generator.function.Populator;

import com.dfsek.terra.allay.TerraAllayPlugin;
import com.dfsek.terra.allay.delegate.AllayProtoChunk;
import com.dfsek.terra.allay.delegate.AllayProtoWorld;
import com.dfsek.terra.allay.delegate.AllayServerWorld;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.api.world.chunk.generation.util.GeneratorWrapper;
import com.dfsek.terra.api.world.info.WorldProperties;


/**
 * @author daoge_cmd
 */
public class AllayGeneratorWrapper implements GeneratorWrapper {

    protected static final String OPTION_META_PACK_NAME = "meta-pack";
    protected static final String OPTION_PACK_NAME = "pack";
    protected static final String OPTION_SEED = "seed";

    protected final long seed;
    protected final WorldGenerator allayWorldGenerator;

    protected ConfigPack configPack;
    protected ChunkGenerator chunkGenerator;
    protected BiomeProvider biomeProvider;

    protected WorldProperties worldProperties;
    protected AllayServerWorld allayServerWorld;

    public AllayGeneratorWrapper(String preset) {
        var options = AllayStringUtils.parseOptions(preset);
        this.seed = parseSeed(options.get(OPTION_SEED));
        this.allayWorldGenerator = WorldGenerator
            .builder()
            .name("TERRA")
            .preset(preset)
            .noisers(new AllayNoiser())
            .populators(new AllayPopulator())
            .onDimensionSet(dimension -> {
                this.allayServerWorld = new AllayServerWorld(this, dimension);
                this.worldProperties = new AllayWorldProperties(this.seed, dimension.getDimensionInfo());

                var metaPackName = options.get(OPTION_META_PACK_NAME);
                if (metaPackName != null) {
                    setConfigPack(getConfigPackByMeta(metaPackName, dimension.getDimensionInfo()));
                    return;
                }

                var packName = options.get(OPTION_PACK_NAME);
                if(packName != null) {
                    setConfigPack(getConfigPackById(packName));
                    return;
                }

                throw new IllegalArgumentException("Either 'pack' or 'meta-pack' option should be specified in the generator preset!");
            })
            .build();
    }

    protected static long parseSeed(String str) {
        if(str == null) {
            return 0;
        }

        try {
            return Long.parseLong(str);
        } catch(NumberFormatException e) {
            // Return the hashcode of the string if it cannot be parsed to a long value directly
            return str.hashCode();
        }
    }

    protected static ConfigPack getConfigPackById(String packId) {
        return TerraAllayPlugin.platform
            .getConfigRegistry()
            .getByID(packId)
            .orElseThrow(() -> new IllegalArgumentException("Cant find terra config pack named " + packId));
    }

    protected static ConfigPack getConfigPackByMeta(String metaPackId, DimensionInfo dimensionInfo) {
        return TerraAllayPlugin.platform
            .getMetaConfigRegistry()
            .getByID(metaPackId)
            .orElseThrow(() -> new IllegalArgumentException("Cant find terra meta pack named " + metaPackId))
            .packs()
            .get(Mapping.dimensionIdBeToJe(dimensionInfo.toString()));
    }

    protected static ChunkGenerator createGenerator(ConfigPack configPack) {
        return configPack.getGeneratorProvider().newInstance(configPack);
    }

    @Override
    public ChunkGenerator getHandle() {
        return this.chunkGenerator;
    }

    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }

    public ConfigPack getConfigPack() {
        return this.configPack;
    }

    public void setConfigPack(ConfigPack configPack) {
        Preconditions.checkNotNull(configPack, "Config pack cannot be null!");
        this.configPack = configPack;
        this.chunkGenerator = createGenerator(this.configPack);
        this.biomeProvider = this.configPack.getBiomeProvider();
    }

    public long getSeed() {
        return this.seed;
    }

    public WorldGenerator getAllayWorldGenerator() {
        return this.allayWorldGenerator;
    }


    protected class AllayNoiser implements Noiser {

        @Override
        public boolean apply(NoiseContext context) {
            UnsafeChunk chunk = context.getCurrentChunk();
            int chunkX = chunk.getX();
            int chunkZ = chunk.getZ();
            chunkGenerator.generateChunkData(
                new AllayProtoChunk(chunk),
                worldProperties, biomeProvider,
                chunkX, chunkZ
            );
            int minHeight = context.getDimensionInfo().minHeight();
            int maxHeight = context.getDimensionInfo().maxHeight();
            for(int x = 0; x < 16; x++) {
                for(int y = minHeight; y < maxHeight; y++) {
                    for(int z = 0; z < 16; z++) {
                        chunk.setBiome(
                            x, y, z,
                            (BiomeType) biomeProvider.getBiome(chunkX * 16 + x, y, chunkZ * 16 + z, seed).getPlatformBiome().getHandle()
                        );
                    }
                }
            }
            return true;
        }
    }


    protected class AllayPopulator implements Populator {

        @Override
        public boolean apply(PopulateContext context) {
            AllayProtoWorld tmp = new AllayProtoWorld(allayServerWorld, context);
            try {
                for(GenerationStage generationStage : configPack.getStages()) {
                    generationStage.populate(tmp);
                }
            } catch(Exception e) {
                TerraAllayPlugin.instance.getPluginLogger().error("Error while populating chunk", e);
            }
            return true;
        }
    }
}
