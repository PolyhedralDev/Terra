package biome;

import com.dfsek.tectonic.api.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import com.dfsek.terra.addons.biome.BiomeTemplate;
import com.dfsek.terra.addons.biome.UserDefinedBiome;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.biome.tag.BiomeTagIndex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PlatformTagsTest {
    private static BiomeTemplate template(String... tags) throws ReflectiveOperationException {
        BiomeTagIndex index = new BiomeTagIndex();
        ConfigPack pack = (ConfigPack) Proxy.newProxyInstance(ConfigPack.class.getClassLoader(), new Class<?>[]{ ConfigPack.class },
            (proxy, method, args) -> {
                if(method.getName().equals("getBiomeTagIndex")) return index;
                throw new UnsupportedOperationException(method.getName());
            });
        BiomeTemplate template = new BiomeTemplate(pack, null);
        set(template, "id", "TEST");
        set(template, "tags", new HashSet<>(Set.of(tags)));
        return template;
    }

    private static void set(BiomeTemplate template, String field, Object value) throws ReflectiveOperationException {
        Field f = BiomeTemplate.class.getDeclaredField(field);
        f.setAccessible(true);
        f.set(template, value);
    }

    @Test
    void platformTags() throws Exception {
        BiomeTemplate template = template("USE_RIVER", "minecraft:is_forest", "c:hot");
        template.validate();
        UserDefinedBiome biome = new UserDefinedBiome(null, template);

        assertEquals(Set.of(RegistryKey.parse("minecraft:is_forest"), RegistryKey.parse("c:hot")), biome.getPlatformTags());
        assertEquals(Set.of("USE_RIVER", "minecraft:is_forest", "c:hot", "BIOME:TEST", "ALL"), biome.getTags());
        assertThrows(UnsupportedOperationException.class, () -> biome.getTags().add("X"));
        assertFalse(template.getTags().contains("BIOME:TEST"));
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource(delimiter = '|', value = {
        "minecraft:Bad Tag | minecraft:Bad Tag",
        ":is_forest        | empty",
        "minecraft:        | empty",
    })
    void invalidNamespacedTag(String tag, String reasonPart) throws Exception {
        ValidationException e = assertThrows(ValidationException.class, template(tag)::validate);
        assertTrue(e.getMessage().contains("\"" + tag + "\""), e.getMessage());
        assertTrue(e.getMessage().contains(reasonPart), e.getMessage());
    }
}
