package com.dfsek.terra.addons.commands.locate;

import com.dfsek.seismic.type.vector.Vector2Int;
import com.dfsek.seismic.type.vector.Vector3Int;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BiomeLocator {

    /**
     * Locates the nearest biome matching the given predicate using a parallelized square spiral search.
     *
     * @param provider   The BiomeProvider to search in.
     * @param properties The world properties (needed for seed and height bounds).
     * @param originX    Starting X coordinate.
     * @param originZ    Starting Z coordinate.
     * @param radius     The maximum radius (in blocks) to search.
     * @param step       The search step/increment. Higher values are faster but less accurate.
     * @param filter     The condition to match the biome.
     * @param search3D   If true, searches the entire vertical column at each step. If false, only checks originY.
     * @return An Optional containing the location of the found biome, or empty if not found.
     */
    public static Optional<Either<Vector3Int, Vector2Int>> search(
        @NotNull BiomeProvider provider,
        @NotNull WorldProperties properties,
        int originX,
        int originZ,
        int radius,
        int step,
        @NotNull Predicate<Biome> filter,
        boolean search3D
    ) {
        long seed = properties.getSeed();
        int minHeight = properties.getMinHeight();
        int maxHeight = properties.getMaxHeight();

        // 1. Check the exact center first
        Optional<Either<Vector3Int, Vector2Int>> centerResult = check(provider, seed, originX, originZ, step, filter, search3D, minHeight, maxHeight);
        if (centerResult.isPresent()) {
            return centerResult;
        }

        // 2. Begin Parallel Square Spiral Search
        // We iterate rings sequentially to guarantee finding the *nearest* result.
        // However, we process all points within a specific ring in parallel.
        for (int r = step; r <= radius; r += step) {
            final int currentRadius = r;
            final int minX = -currentRadius;
            final int maxX = currentRadius;
            final int minZ = -currentRadius;
            final int maxZ = currentRadius;

            Stream<int[]> northSide = IntStream.iterate(minX, n -> n < maxX, n -> n + step)
                .mapToObj(x -> new int[]{x, minZ}); // Fixed Z (min), varying X

            Stream<int[]> eastSide = IntStream.iterate(minZ, n -> n < maxZ, n -> n + step)
                .mapToObj(z -> new int[]{maxX, z}); // Fixed X (max), varying Z

            Stream<int[]> southSide = IntStream.iterate(maxX, n -> n > minX, n -> n - step)
                .mapToObj(x -> new int[]{x, maxZ}); // Fixed Z (max), varying X

            Stream<int[]> westSide = IntStream.iterate(maxZ, n -> n > minZ, n -> n - step)
                .mapToObj(z -> new int[]{minX, z}); // Fixed X (min), varying Z

            Optional<Either<Vector3Int, Vector2Int>> ringResult = Stream.of(northSide, eastSide, southSide, westSide)
                .flatMap(Function.identity())
                .parallel()
                .map(coords -> check(
                    provider,
                    seed,
                    originX + coords[0],
                    originZ + coords[1],
                    step,
                    filter,
                    search3D,
                    minHeight,
                    maxHeight
                ))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst(); // findFirst() respects encounter order (North -> East -> South -> West)

            if (ringResult.isPresent()) {
                return ringResult;
            }
        }

        return Optional.empty();
    }

    /**
     * Helper to check a specific coordinate column or point.
     * This logic is executed inside the worker threads.
     */
    private static Optional<Either<Vector3Int, Vector2Int>> check(
        BiomeProvider provider,
        long seed,
        int x,
        int z,
        int step,
        Predicate<Biome> filter,
        boolean search3D,
        int minHeight,
        int maxHeight
    ) {
        if (search3D) {
            // Iterate from bottom to top of the world using the step
            for (int y = minHeight; y < maxHeight; y += step) {
                if (filter.test(provider.getBiome(x, y, z, seed))) {
                    return Optional.of(Either.left(Vector3Int.of(x, y, z)));
                }
            }
            return Optional.empty();
        } else {
            // 2D Mode: Check only the base biome
            // We use a flatMap approach here to be safe with Optionals inside the stream
            return provider.getBaseBiome(x, z, seed)
                .filter(filter)
                .map(b -> Either.right(Vector2Int.of(x, z)));
        }
    }
}