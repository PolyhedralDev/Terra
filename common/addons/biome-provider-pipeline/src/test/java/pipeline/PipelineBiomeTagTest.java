package pipeline;

import com.dfsek.seismic.type.sampler.Sampler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.dfsek.terra.addons.biome.pipeline.api.Source;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.config.PipelineBiomeLoader;
import com.dfsek.terra.addons.biome.pipeline.pipeline.PipelineImpl;
import com.dfsek.terra.addons.biome.pipeline.stage.mutators.BorderListStage;
import com.dfsek.terra.addons.biome.pipeline.stage.mutators.ReplaceListStage;
import com.dfsek.terra.addons.biome.pipeline.stage.mutators.ReplaceStage;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.cache.SeededVector2Key;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.api.world.biome.tag.BiomeTagIndex;

import static org.junit.jupiter.api.Assertions.*;


class PipelineBiomeTagTest {
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

    @SuppressWarnings("unchecked")
    private static Registry<Biome> emptyRegistry() {
        return (Registry<Biome>) Proxy.newProxyInstance(Registry.class.getClassLoader(), new Class<?>[]{ Registry.class },
            (proxy, method, args) -> {
                if(method.getName().equals("getByID")) return Optional.empty();
                throw new UnsupportedOperationException(method.getName());
            });
    }

    private final BiomeTagIndex index = new BiomeTagIndex();
    private final Map<String, PipelineBiome> biomes = new LinkedHashMap<>();

    {
        biome("border", "border");
        biome("c", "R", "D");
        biome("a", "R");
        biome("b", "R");
        biome("x");
        biome("y");
        biome("e");
        biome("f", "R", "D");
    }

    private void biome(String id, String... tags) {
        biomes.put(id, PipelineBiome.from(new StubBiome(index, id, Set.of(tags))));
    }

    private static ProbabilityCollection<PipelineBiome> only(PipelineBiome biome) {
        return new ProbabilityCollection<PipelineBiome>().add(biome, 1);
    }

    private Map<PipelineBiome, ProbabilityCollection<PipelineBiome>> replaceTable() {
        Map<PipelineBiome, ProbabilityCollection<PipelineBiome>> replace = new HashMap<>();
        replace.put(biomes.get("a"), only(biomes.get("x")));
        replace.put(biomes.get("b"), only(PipelineBiome.self()));
        return replace;
    }

    private String run(Stage stage, String centre) {
        index.seal();
        PipelineBiome centreBiome = biomes.get(centre);
        PipelineBiome border = biomes.get("border");
        Source source = new Source() {
            @Override
            public PipelineBiome get(long seed, int x, int z) {
                return x == 0 && z == 0 ? centreBiome : border;
            }

            @Override
            public Iterable<PipelineBiome> getBiomes() {
                return List.of(centreBiome, border);
            }
        };
        return new PipelineImpl(source, List.of(stage), 1, 3).generateChunk(new SeededVector2Key(0, 0, 0)).get(0, 0).getID();
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource({ "a, x", "b, b", "c, y", "e, e", "f, y" })
    void borderListStage(String centre, String expected) {
        assertEquals(expected, run(new BorderListStage(replaceTable(), index.tag("border"), index.tag("R"), SAMPLER,
            only(biomes.get("y"))), centre));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource({ "a, x", "b, b", "c, y", "e, e", "f, y" })
    void replaceListStage(String centre, String expected) {
        assertEquals(expected, run(new ReplaceListStage(replaceTable(), index.tag("D"), only(biomes.get("y")), SAMPLER), centre));
    }

    @ParameterizedTest(name = "{0}: {1} -> {2}")
    @CsvSource({ "D, c, y", "D, e, e", "TYPO, c, c", "TYPO, e, e" })
    void replaceStage(String tag, String centre, String expected) {
        assertEquals(expected, run(new ReplaceStage(index.tag(tag), only(biomes.get("y")), SAMPLER), centre));
    }

    @Test
    void placeholderRegistration() {
        PipelineBiomeLoader loader = new PipelineBiomeLoader(emptyRegistry(), index);
        int before = index.size();
        PipelineBiome first = load(loader, "OCEAN");
        assertSame(first, load(loader, "OCEAN"));
        assertEquals(before + 1, index.size());
        assertDoesNotThrow(() -> load(loader, "ALL"));
        index.seal();
        assertTrue(index.tag("OCEAN").has(first.getIntID()));
        assertTrue(index.tag("ALL").has(first.getIntID()));
        assertFalse(index.tag("OTHER").has(first.getIntID()));
    }

    @Test
    void selfHasNoTags() {
        assertEquals(0, PipelineBiome.self().getIntID());
        index.seal();
        assertFalse(index.tag("ALL").has(PipelineBiome.self().getIntID()));
    }

    private static PipelineBiome load(PipelineBiomeLoader loader, String id) {
        return loader.load((AnnotatedType) null, id, null, null);
    }
}
