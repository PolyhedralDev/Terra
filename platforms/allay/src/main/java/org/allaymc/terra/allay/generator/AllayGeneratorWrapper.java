package org.allaymc.terra.allay.generator;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.util.GeneratorWrapper;

import com.dfsek.terra.api.world.info.WorldProperties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.allaymc.api.world.Dimension;
import org.allaymc.api.world.biome.BiomeType;
import org.allaymc.api.world.generator.ChunkGenerateContext;
import org.allaymc.api.world.generator.WorldGenerator;
import org.allaymc.api.world.generator.WorldGeneratorType;
import org.allaymc.terra.allay.TerraAllayPlugin;
import org.allaymc.terra.allay.delegate.AllayProtoChunk;
import org.allaymc.terra.allay.delegate.AllayProtoWorld;
import org.allaymc.terra.allay.delegate.AllayServerWorld;

import java.util.Locale;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
@Slf4j
public class AllayGeneratorWrapper extends WorldGenerator implements GeneratorWrapper {
    protected static final String DEFAULT_PACK_NAME = "overworld";
    protected static final String OPTION_PACK_NAME = "pack";
    protected static final String OPTION_SEED = "pack";

    @Getter
    protected final BiomeProvider biomeProvider;
    @Getter
    protected final ConfigPack configPack;
    protected final ChunkGenerator chunkGenerator;
    protected WorldProperties worldProperties;
    @Getter
    protected long seed;

    public AllayGeneratorWrapper(String preset) {
        super(preset);
        var options = parseOptions(preset);
        var packName = options.getOrDefault(OPTION_PACK_NAME, DEFAULT_PACK_NAME);
        this.seed = Long.parseLong(options.getOrDefault(OPTION_SEED, "0"));
        this.configPack = createConfigPack(packName);
        this.chunkGenerator = createGenerator(this.configPack);
        this.biomeProvider = this.configPack.getBiomeProvider();
    }

    @Override
    public void generate(ChunkGenerateContext context) {
        var chunk = context.chunk();
        var chunkX = chunk.getX();
        var chunkZ = chunk.getZ();
        chunkGenerator.generateChunkData(
            new AllayProtoChunk(chunk),
            worldProperties, biomeProvider,
            chunkX, chunkZ
        );
        var minHeight = dimension.getDimensionInfo().minHeight();
        var maxHeight = dimension.getDimensionInfo().maxHeight();
        for (int x = 0; x < 16; x++) {
            for (int y = minHeight; y < maxHeight; y++) {
                for (int z = 0; z < 16; z++) {
                    chunk.setBiome(
                        x, y, z,
                        (BiomeType) biomeProvider.getBiome(chunkX * 16 + x, y, chunkZ * 16 + z, seed).getPlatformBiome().getHandle()
                    );
                }
            }
        }
        var tmp = new AllayProtoWorld(new AllayServerWorld(this, dimension), chunk);
        try {
            for (var generationStage : configPack.getStages()) {
                generationStage.populate(tmp);
            }
        } catch (Exception e) {
            log.error("Error while populating chunk", e);
        }
    }

    @Override
    public void setDimension(Dimension dimension) {
        super.setDimension(dimension);
        this.worldProperties = new WorldProperties() {
            @Override
            public long getSeed() {
                return seed;
            }

            @Override
            public int getMaxHeight() {
                return dimension.getDimensionInfo().maxHeight();
            }

            @Override
            public int getMinHeight() {
                return dimension.getDimensionInfo().minHeight();
            }

            @Override
            public Object getHandle() {
                // 这里留null就行，没啥用
                return null;
            }
        };
    }

    protected static ConfigPack createConfigPack(String packName) {
        var byId = TerraAllayPlugin.PLATFORM.getConfigRegistry().getByID(packName);
        return byId.orElseGet(
            () -> TerraAllayPlugin.PLATFORM.getConfigRegistry().getByID(packName.toUpperCase(Locale.ENGLISH))
                .orElseThrow(() -> new IllegalArgumentException("Cant find terra config pack named " + packName))
        );
    }

    protected static ChunkGenerator createGenerator(ConfigPack configPack) {
        return configPack.getGeneratorProvider().newInstance(configPack);
    }

    @Override
    public ChunkGenerator getHandle() {
        return chunkGenerator;
    }

    @Override
    public String getGeneratorName() {
        return "TERRA";
    }

    @Override
    public WorldGeneratorType getType() {
        return WorldGeneratorType.INFINITE;
    }
}
