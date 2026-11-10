package com.dfsek.terra.addons.attribute;

import com.dfsek.paralithic.eval.parser.Parser.ParseOptions;
import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.preprocessor.Result;
import com.dfsek.tectonic.api.preprocessor.ValuePreprocessor;
import com.dfsek.tectonic.impl.MapConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AnnotatedType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.config.preprocessor.MetaListLikePreprocessor;
import com.dfsek.terra.config.preprocessor.MetaMapPreprocessor;
import com.dfsek.terra.config.preprocessor.MetaNumberPreprocessor;
import com.dfsek.terra.config.preprocessor.MetaStringPreprocessor;
import com.dfsek.terra.config.preprocessor.MetaValuePreprocessor;
import com.dfsek.terra.world.attribute.AttributeHandleImpl;

import static org.junit.jupiter.api.Assertions.*;


class ConfigDecodeContextTest {
    private static final Map<String, Object> META = map(
        "n", 3,
        "s", "two",
        "chain", "$meta.yml:s",
        "obj", map("x", 1, "y", "$meta.yml:s"),
        "list", List.of("a", "b"));

    private static Map<String, Object> map(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for(int i = 0; i < kv.length; i += 2) m.put((String) kv[i], kv[i + 1]);
        return m;
    }

    private static DecodeContext context() {
        return context(Map.of("meta.yml", new MapConfiguration(META, "meta.yml")));
    }

    private static DecodeContext context(Map<String, Configuration> configs) {
        ConfigLoader loader = new ConfigLoader();
        loader.registerPreprocessor(Meta.class, new MetaStringPreprocessor(configs));
        loader.registerPreprocessor(Meta.class, new MetaListLikePreprocessor(configs));
        loader.registerPreprocessor(Meta.class, new MetaMapPreprocessor(configs));
        loader.registerPreprocessor(Meta.class, new MetaValuePreprocessor(configs));
        loader.registerPreprocessor(Meta.class, new MetaNumberPreprocessor(configs, new ParseOptions()));
        return context(loader);
    }

    private static DecodeContext context(ConfigLoader loader) {
        for(Class<?> type : ConfigDecodeContext.CAPTURED_TYPES) loader.registerLoader(type, LazyConfigValue.LOADER);
        DepthTracker tracker = new DepthTracker(List.of(), new MapConfiguration(Map.of(), "biome.yml"));
        return new LazyConfigValue(null, loader, tracker).context(new AttributeHandleImpl().plainContext());
    }

    @Test
    void numberExpressions() throws DecodeException {
        DecodeContext context = context();
        assertEquals("2 * 6000", context.resolve("2 * 6000"));
        assertEquals(12000, context.<Integer>load(Integer.class, "2 * 6000"));
        assertEquals(1.5f, context.<Float>load(Float.class, "${meta.yml:n} / 2"));
        assertEquals(3.0, context.<Double>load(Double.class, "$meta.yml:n"));
        assertEquals(0.5f, context.<Float>load(Float.class, 0.5));
    }

    @Test
    void plainConversion() {
        DecodeContext context = context();
        assertTrue(assertThrows(DecodeException.class, () -> context.load(Integer.class, "5 / 2")).getMessage()
            .startsWith("expected an integer"));
        assertEquals("expected a string, got 3",
            assertThrows(DecodeException.class, () -> context.load(String.class, "$meta.yml:n")).getMessage());
        assertEquals("expected a boolean (true or false), got \"two\"",
            assertThrows(DecodeException.class, () -> context.load(Boolean.class, "$meta.yml:s")).getMessage());
    }

    @Test
    void nestedResolve() throws DecodeException {
        Map<String, Object> raw = map(
            "null", null,
            "ref", "$meta.yml:obj",
            "list", List.of("<< meta.yml:list", "c", "${meta.yml:s}"),
            "include", map("<<", List.of("meta.yml:obj"), "x", 2),
            "string", "one-${meta.yml:s}",
            "deep", map("a", List.of(map("b", "$meta.yml:n"))));
        Map<?, ?> resolved = (Map<?, ?>) context().resolve(raw);
        assertEquals(map(
            "null", null,
            "ref", map("x", 1, "y", "two"),
            "list", List.of("a", "b", "c", "two"),
            "include", map("x", 2, "y", "two"),
            "string", "one-two",
            "deep", map("a", List.of(map("b", 3)))), resolved);
        assertEquals(List.copyOf(raw.keySet()), List.copyOf(resolved.keySet()));
    }

    @Test
    void resolveTwice() throws DecodeException {
        DecodeContext context = context();
        Object once = context.resolve(map("a", "$meta.yml:chain"));
        assertEquals(map("a", "$meta.yml:s"), once);
        assertSame(once, context.resolve(once));
        Object chained = ((Map<?, ?>) once).get("a");
        assertSame(chained, context.resolve(chained));
        assertEquals("$meta.yml:s", context.load(String.class, chained));
    }

    @Test
    void preprocessingErrors() {
        DecodeContext context = context();
        String missing = assertThrows(DecodeException.class, () -> context.resolve(map("a", "${meta.yml:missing}"))).getMessage();
        assertTrue(missing.contains("No such key missing"), missing);
        assertTrue(missing.contains("Path: a"), missing);

        String malformed = assertThrows(DecodeException.class, () -> context.resolve(List.of("${2 * 6000}"))).getMessage();
        assertTrue(malformed.contains("meta reference \"2 * 6000\" must have the form file:key"), malformed);
        assertTrue(malformed.contains("Path: [0]"), malformed);
        assertReadable(malformed);

        String escape = assertThrows(DecodeException.class, () -> context.resolve(map("a", "$meta.yml:bad\\q"))).getMessage();
        assertTrue(escape.contains("meta reference \"meta.yml:bad\\q\" has an invalid key path"), escape);
        assertReadable(escape);

        String expression = assertThrows(DecodeException.class, () -> context.load(Float.class, "1 +")).getMessage();
        assertTrue(expression.contains("Invalid expression"), expression);

        String include = assertThrows(DecodeException.class, () -> context.resolve(map("<<", List.of("meta.yml:list")))).getMessage();
        assertTrue(include.contains("MetaMap injection candidate must be map, is type java.util."), include);
    }

    @Test
    void nullMetaTargets() {
        Configuration nulls = new Configuration() {
            @Override
            public Object get(@NotNull String key) {
                return null;
            }

            @Override
            public boolean contains(@NotNull String key) {
                return true;
            }

            @Override
            public String getName() {
                return "null.yml";
            }
        };
        DecodeContext context = context(Map.of("null.yml", nulls));
        String string = assertThrows(DecodeException.class, () -> context.resolve("${null.yml:x}")).getMessage();
        assertTrue(string.contains("must be string or primitive, is null"), string);
        String list = assertThrows(DecodeException.class, () -> context.resolve(List.of("<< null.yml:x"))).getMessage();
        assertTrue(list.contains("'null.yml:x' points to null"), list);
        String include = assertThrows(DecodeException.class, () -> context.resolve(map("<<", List.of("null.yml:x")))).getMessage();
        assertTrue(include.contains("must be map, is null"), include);
    }

    @Test
    void preprocessorRuntimeExceptions() {
        ConfigLoader loader = new ConfigLoader();
        loader.registerPreprocessor(Meta.class, new ValuePreprocessor<Meta>() {
            @Override
            public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader l, Meta annotation, DepthTracker depthTracker) {
                throw new IllegalStateException("java.lang.Boom");
            }
        });
        DecodeContext context = context(loader);
        String message = assertThrows(DecodeException.class, () -> context.resolve("x")).getMessage();
        assertTrue(message.contains("config preprocessing failed"), message);
        assertReadable(message);
    }

    private static void assertReadable(String message) {
        assertFalse(message.contains("java."), message);
        assertFalse(message.contains("Exception"), message);
    }
}
