package com.dfsek.terra.minecraft.attribute;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.minecraft.attribute.BiomeTags.PlatformTagSource;
import com.dfsek.terra.world.attribute.platform.AttributeReport;

import static com.dfsek.terra.minecraft.attribute.VanillaContextTest.copy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BiomeTagsTest {
    private static final Identifier TERRA_ID = Identifier.parse("terra:test/biome");
    private static final TagKey<Biome> HAS_SNOW = tag("mypack:has_snow");
    private static final TagKey<Biome> IS_FOREST = net.minecraft.tags.BiomeTags.IS_FOREST;

    private final List<String> warnings = new ArrayList<>();
    private final AttributeReport report = new AttributeReport("test", warnings::add, m -> { });
    private final MappedRegistry<Biome> registry = copy(Registries.BIOME);
    private final Holder<Biome> terra = registry.get(Biomes.DESERT).orElseThrow();
    private final Map<TagKey<Biome>, List<Holder<Biome>>> collect = new HashMap<>();

    private static TagKey<Biome> tag(String id) {
        return TagKey.create(Registries.BIOME, Identifier.parse(id));
    }

    private Optional<Holder<Biome>> lookup(Identifier id) {
        return id.equals(TERRA_ID) ? Optional.of(terra) : registry.get(ResourceKey.create(Registries.BIOME, id))
            .map(h -> h);
    }

    private void add(Map<Identifier, List<Identifier>> byParent, String... tags) {
        Set<RegistryKey> keys = new LinkedHashSet<>();
        for(String t : tags) keys.add(RegistryKey.parse(t));
        BiomeTags.addTerraBiomes(collect, this::lookup, byParent, Map.of(TERRA_ID, new PlatformTagSource(report, "BIOME", keys)));
    }

    private static void bindTags(Holder<Biome> holder, TagKey<Biome> tag) throws ReflectiveOperationException {
        Method bind = Holder.Reference.class.getDeclaredMethod("bindTags", Collection.class);
        bind.setAccessible(true);
        bind.invoke(holder, List.of(tag));
    }

    @Test
    void platformTagsAreAdded() {
        Holder<Biome> forest = registry.get(Biomes.FOREST).orElseThrow();
        collect.put(IS_FOREST, new ArrayList<>(List.of(forest)));

        add(Map.of(), "mypack:has_snow", "minecraft:is_forest");

        assertEquals(List.of(terra), collect.get(HAS_SNOW));
        assertEquals(List.of(forest, terra), collect.get(IS_FOREST));
        assertEquals(List.of(), warnings);
    }

    @Test
    void inheritedTagsStillApply() throws ReflectiveOperationException {
        Holder<Biome> forest = registry.get(Biomes.FOREST).orElseThrow();
        bindTags(forest, IS_FOREST);
        Identifier forestId = Biomes.FOREST.identifier();

        add(Map.of(forestId, List.of(TERRA_ID)));

        assertEquals(List.of(terra), collect.get(IS_FOREST));
        assertEquals(List.of(), warnings);
    }

    @Test
    void upperCaseTagIsIgnored() {
        add(Map.of(), "mypack:Has_Snow");

        assertTrue(collect.isEmpty(), collect.toString());
        assertEquals(1, warnings.size(), warnings.toString());
        assertTrue(warnings.getFirst().startsWith("Pack \"test\", config \"BIOME\": \"mypack:Has_Snow\" ignored: invalid ID "),
                   warnings.getFirst());
    }

    @Test
    void missingMinecraftTagWarnsAndIsCreated() {
        add(Map.of(), "minecraft:is_forrest");

        assertEquals(List.of("Pack \"test\", config \"BIOME\": tag \"minecraft:is_forrest\" created: no such tag in the minecraft namespace"),
                     warnings);
        assertEquals(List.of(terra), collect.get(tag("minecraft:is_forrest")));
    }
}
