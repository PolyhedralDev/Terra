package com.dfsek.terra.bukkit.generator;

import org.bukkit.World.Environment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Proxy;
import java.util.Map;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.MetaPack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


class MetaPackWorldsTest {
    private static final ConfigPack OVERWORLD = pack("OVERWORLD");
    private static final ConfigPack NETHER = pack("NETHER");

    @ParameterizedTest
    @CsvSource({
        "minecraft:overworld, NORMAL",
        "overworld,           NORMAL",
        "minecraft:the_nether, NETHER",
        "the_nether,          NETHER"
    })
    void picksThePackForTheWorldsStem(String key, Environment environment) {
        assertSame(OVERWORLD, MetaPackWorlds.packFor(metaPack(Map.of(key, OVERWORLD)), environment, "world"));
    }

    @Test
    void namespacedKeyWins() {
        MetaPack metaPack = metaPack(Map.of("overworld", NETHER, "minecraft:overworld", OVERWORLD));

        assertSame(OVERWORLD, MetaPackWorlds.packFor(metaPack, Environment.NORMAL, "world"));
    }

    @Test
    void missingStemNamesTheMetapackStemAndWorld() {
        MetaPack metaPack = metaPack(Map.of("minecraft:overworld", OVERWORLD));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> MetaPackWorlds.packFor(metaPack, Environment.THE_END, "world_the_end"));

        assertEquals("Metapack \"DEFAULT\" has no pack for minecraft:the_end (world \"world_the_end\")", e.getMessage());
    }

    @Test
    void customEnvironmentHasNoStem() {
        MetaPack metaPack = metaPack(Map.of("minecraft:overworld", OVERWORLD));

        assertThrows(IllegalArgumentException.class, () -> MetaPackWorlds.packFor(metaPack, Environment.CUSTOM, "custom"));
    }

    @Test
    void unboundWrapperRefusesGetPack() {
        BukkitChunkGeneratorWrapper wrapper = new BukkitChunkGeneratorWrapper(metaPack(Map.of()), null);

        assertThrows(IllegalStateException.class, wrapper::getPack);
    }

    private static MetaPack metaPack(Map<String, ConfigPack> packs) {
        return (MetaPack) Proxy.newProxyInstance(MetaPack.class.getClassLoader(), new Class<?>[]{ MetaPack.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "packs" -> packs;
                case "getID" -> "DEFAULT";
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    private static ConfigPack pack(String id) {
        return (ConfigPack) Proxy.newProxyInstance(ConfigPack.class.getClassLoader(), new Class<?>[]{ ConfigPack.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "getID", "toString" -> id;
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }
}
