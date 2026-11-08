package com.dfsek.terra.cli;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.structure.feature.Feature;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.config.pack.ConfigPackImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FeatureStagePackTest {
    private static ConfigPack pack;

    @BeforeAll
    static void setUp() throws Exception {
        CLIPlatform platform = new CLIPlatform();
        platform.getEventManager().callEvent(new PlatformInitializationEvent());
        Path fixture = Path.of(FeatureStagePackTest.class.getResource("/packs/feature-stage-test/pack.yml").toURI()).getParent();
        pack = new ConfigPackImpl(fixture, platform);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map<String, List<String>> featuresByStage(Biome biome) throws ReflectiveOperationException {
        GenerationStage stage = pack.getStages().getFirst();
        Class<?> biomeFeatures = stage.getClass().getClassLoader()
            .loadClass("com.dfsek.terra.addons.generation.feature.config.BiomeFeatures");
        PropertyKey<?> key = Context.create((Class) biomeFeatures.asSubclass(Properties.class));
        Object properties = biome.getContext().get(key);
        Map<Object, List<Feature>> features = (Map<Object, List<Feature>>) biomeFeatures.getMethod("getFeatures").invoke(properties);
        Map<String, List<String>> byId = new HashMap<>();
        features.forEach((s, list) -> byId.put(((StringIdentifiable) s).getID(), list.stream().map(Feature::getID).toList()));
        return byId;
    }

    @Test
    void dottedStageIdReadsNestedPath() throws ReflectiveOperationException {
        Biome plain = pack.getRegistry(Biome.class).getByID("PLAIN").orElseThrow();
        Map<String, List<String>> features = featuresByStage(plain);
        assertEquals(List.of("ROCK"), features.get("nested.stage"));
        assertEquals(List.of("ROCK"), features.get("flat"));
    }
}
