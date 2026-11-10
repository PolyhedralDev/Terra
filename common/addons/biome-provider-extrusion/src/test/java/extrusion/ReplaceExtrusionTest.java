package extrusion;

import com.dfsek.seismic.type.sampler.Sampler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;
import java.util.Set;

import com.dfsek.terra.addons.biome.extrusion.api.ReplaceableBiome;
import com.dfsek.terra.addons.biome.extrusion.extrusions.ReplaceExtrusion;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.range.ConstantRange;
import com.dfsek.terra.api.util.range.Range;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.api.world.biome.tag.BiomeTagIndex;

import static org.junit.jupiter.api.Assertions.*;


class ReplaceExtrusionTest {
    private static final Sampler SAMPLER = new Sampler() {
        @Override
        public double getSample(long seed, double x, double y) {
            return 0;
        }

        @Override
        public double getSample(long seed, double x, double y, double z) {
            return 0;
        }
    };

    private static final class StubBiome implements Biome {
        private final String id;
        private final Set<String> tags;
        private final int intId;

        StubBiome(BiomeTagIndex index, String id, Set<String> tags) {
            this.id = id;
            this.tags = tags;
            this.intId = index.register(tags);
        }

        @Override
        public PlatformBiome getPlatformBiome() {
            return null;
        }

        @Override
        public int getColor() {
            return 0;
        }

        @Override
        public Set<String> getTags() {
            return tags;
        }

        @Override
        public int getIntID() {
            return intId;
        }

        @Override
        public Context getContext() {
            return null;
        }

        @Override
        public String getID() {
            return id;
        }
    }

    private final BiomeTagIndex index = new BiomeTagIndex();
    private final Map<String, Biome> biomes = Map.of(
        "tagged", new StubBiome(index, "tagged", Set.of("X")),
        "untagged", new StubBiome(index, "untagged", Set.of("Y")),
        "replacement", new StubBiome(index, "replacement", Set.of()));

    @ParameterizedTest(name = "{0}: {1} at y {2} -> {3}")
    @CsvSource({
        "X, tagged, 10, replacement",
        "X, untagged, 10, untagged",
        "X, tagged, 100, tagged",
        "TYPO, tagged, 10, tagged",
        "TYPO, untagged, 10, untagged",
        "TYPO, replacement, 10, replacement",
    })
    void replacesTaggedBiomesInRange(String tag, String biome, int y, String expected) {
        ProbabilityCollection<ReplaceableBiome> to = new ProbabilityCollection<ReplaceableBiome>()
            .add(ReplaceableBiome.of(biomes.get("replacement")), 1);
        Range range = new ConstantRange(0, 64);
        ReplaceExtrusion extrusion = new ReplaceExtrusion(SAMPLER, range, to, index.tag(tag));
        index.seal();
        assertSame(biomes.get(expected), extrusion.extrude(biomes.get(biome), 0, y, 0, 0));
    }
}
