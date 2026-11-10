package com.dfsek.terra.minecraft.generation;

import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;

import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;
import com.dfsek.terra.api.world.info.WorldProperties;
import com.dfsek.terra.minecraft.attribute.MinecraftTestSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


class TerraTerrainTest {
    static {
        MinecraftTestSupport.REGISTRIES.hashCode();
    }

    private static final com.dfsek.terra.api.block.state.BlockState STONE = terra("stone");
    private static final com.dfsek.terra.api.block.state.BlockState AIR = terra("air");
    private static final com.dfsek.terra.api.block.state.BlockState DIRT = terra("dirt");
    private static final Map<com.dfsek.terra.api.block.state.BlockState, BlockState> VANILLA = Map.of(
        STONE, Blocks.STONE.defaultBlockState(), AIR, Blocks.AIR.defaultBlockState(), DIRT, Blocks.DIRT.defaultBlockState());
    private static final Function<com.dfsek.terra.api.block.state.BlockState, BlockState> TO_VANILLA = VANILLA::get;
    private static final LevelHeightAccessor HEIGHT = LevelHeightAccessor.create(-4, 8);

    @Test
    void baseColumnCoversTheWholeHeightBottomUp() {
        NoiseColumn column = TerraTerrain.baseColumn(generator(y -> y < 0 ? STONE : AIR, null), null, null, HEIGHT, 0, 0, TO_VANILLA);

        for(int y = -4; y < 4; y++) {
            assertSame(y < 0 ? Blocks.STONE.defaultBlockState() : Blocks.AIR.defaultBlockState(), column.getBlock(y), "y " + y);
        }
    }

    @Test
    void baseHeightIsAboveTheTopOpaqueBlock() {
        int height = TerraTerrain.baseHeight(generator(y -> y <= 1 ? STONE : AIR, null), null, null, HEIGHT, 0, 0,
            state -> !state.isAir(), TO_VANILLA);

        assertEquals(2, height);
    }

    @Test
    void baseHeightCountsTheTopBlock() {
        int height = TerraTerrain.baseHeight(generator(y -> y == 3 ? STONE : AIR, null), null, null, HEIGHT, 0, 0,
            state -> !state.isAir(), TO_VANILLA);

        assertEquals(4, height);
    }

    @Test
    void baseHeightOfAnEmptyColumnIsTheBottom() {
        assertEquals(-4, TerraTerrain.baseHeight(generator(y -> AIR, null), null, null, HEIGHT, 0, 0, state -> !state.isAir(), TO_VANILLA));
    }

    @Test
    void beardFillsDenseBlocksWithPaletteLayersAndClearsSparseOnes() {
        List<Integer> layers = new ArrayList<>();
        Map<Integer, BlockState> placed = new HashMap<>();
        // Every column gets the same density: dense at 2, 1 and -2, neutral at 0, sparse at -1.
        TerraTerrain.beard(32, 48, -2, 2, (x, y, z) -> switch(y) {
                case 2, 1, -2 -> 1;
                case -1 -> -1;
                default -> 0;
            }, generator(y -> AIR, layer -> {
                layers.add(layer);
                return layer == 0 ? DIRT : STONE;
            }), null, properties(), 0.5, -0.5, TO_VANILLA,
            (x, y, z, state) -> {
                if(x == 0 && z == 0) placed.put(y, state);
            });

        assertEquals(Map.of(2, Blocks.DIRT.defaultBlockState(), 1, Blocks.STONE.defaultBlockState(), -1, Blocks.AIR.defaultBlockState(),
            -2, Blocks.DIRT.defaultBlockState()), placed);
        assertEquals(List.of(0, 1, 0), layers.subList(0, 3));
    }

    private static ChunkGenerator generator(IntFunction<com.dfsek.terra.api.block.state.BlockState> byY,
                                            IntFunction<com.dfsek.terra.api.block.state.BlockState> byLayer) {
        Palette palette = (layer, x, y, z, seed) -> byLayer.apply(layer);
        return (ChunkGenerator) Proxy.newProxyInstance(ChunkGenerator.class.getClassLoader(), new Class<?>[]{ ChunkGenerator.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "getBlock" -> byY.apply((int) args[2]);
                case "getPalette" -> palette;
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    private static WorldProperties properties() {
        return (WorldProperties) Proxy.newProxyInstance(WorldProperties.class.getClassLoader(), new Class<?>[]{ WorldProperties.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "getSeed" -> 0L;
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    private static com.dfsek.terra.api.block.state.BlockState terra(String name) {
        return (com.dfsek.terra.api.block.state.BlockState) Proxy.newProxyInstance(
            com.dfsek.terra.api.block.state.BlockState.class.getClassLoader(), new Class<?>[]{ com.dfsek.terra.api.block.state.BlockState.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                case "toString" -> name;
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }
}
