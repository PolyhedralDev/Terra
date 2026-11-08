package com.dfsek.terra.world.attribute;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.world.attribute.AttributeCodec;
import com.dfsek.terra.api.world.attribute.AttributeCodecs;
import com.dfsek.terra.api.world.attribute.AttributeCodecs.ObjectReader;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AttributeCodecsTest {
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final AttributeCodecs CODECS = HANDLE.codecs();
    private static final DecodeContext PLAIN = HANDLE.plainContext();

    private record Point(int x, float weight) { }


    private static final AttributeCodec<Point> POINT = CODECS.of((raw, context) -> {
        ObjectReader f = CODECS.reader("point", raw, context);
        int x = f.required("x", CODECS.intCodec());
        float weight = f.orDefault("weight", CODECS.unitFloat(), 1f);
        f.finish();
        return new Point(x, weight);
    }, value -> CODECS.writer()
        .put("x", CODECS.intCodec(), value.x())
        .put("weight", CODECS.unitFloat(), value.weight())
        .optional("label", CODECS.string("expected a label"), Optional.empty())
        .map());

    private enum Weather implements StringIdentifiable {
        CLEAR("sunny"),
        STORM("thunder");

        private final String id;

        Weather(String id) {
            this.id = id;
        }

        @Override
        public String getID() {
            return id;
        }
    }

    private static Map<String, Object> map(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for(int i = 0; i < kv.length; i += 2) m.put((String) kv[i], kv[i + 1]);
        return m;
    }

    @Test
    void ofRoundTrips() throws DecodeException {
        Point point = new Point(3, 0.5f);
        Object encoded = POINT.encode(point);
        assertEquals(map("x", 3, "weight", 0.5f), encoded);
        assertEquals(point, POINT.decode(encoded, PLAIN));
        assertEquals(new Point(4, 1f), POINT.decode(map("x", 4), PLAIN));

        AttributeCodec<List<Point>> points = CODECS.compactList(POINT);
        assertEquals(List.of(point), points.decode(encoded, PLAIN));
        assertEquals(List.of(encoded), points.encode(List.of(point)));
    }

    @Test
    void intFailures() {
        assertEquals("\"x\" of point: expected an integer, got 1.5",
            assertThrows(DecodeException.class, () -> POINT.decode(map("x", 1.5), PLAIN)).getMessage());
        assertEquals("expected a non-negative integer, got -1",
            assertThrows(DecodeException.class, () -> CODECS.nonNegativeInt().decode(-1, PLAIN)).getMessage());
    }

    @Test
    void readerUnknownField() {
        assertEquals("unknown key \"y\" in point",
            assertThrows(DecodeException.class, () -> POINT.decode(map("x", 1, "y", 2), PLAIN)).getMessage());
        assertEquals("point is missing required key \"x\"",
            assertThrows(DecodeException.class, () -> POINT.decode(map(), PLAIN)).getMessage());
        assertEquals("expected an object for point, got a list",
            assertThrows(DecodeException.class, () -> POINT.decode(List.of(), PLAIN)).getMessage());
    }

    @Test
    void enumUnknownId() throws DecodeException {
        AttributeCodec<Weather> weather = CODECS.enumCodec("weather", Weather.class);
        assertSame(Weather.STORM, weather.decode("thunder", PLAIN));
        assertEquals("thunder", weather.encode(Weather.STORM));
        assertEquals("unknown weather \"storm\", expected one of [sunny, thunder]",
            assertThrows(DecodeException.class, () -> weather.decode("storm", PLAIN)).getMessage());
    }

    @Test
    void listFailingElement() {
        assertEquals("element 1: expected an integer, got 2.5",
            assertThrows(DecodeException.class, () -> CODECS.list(CODECS.intCodec()).decode(List.of(1, 2.5), PLAIN)).getMessage());
    }

    @Test
    void describeValues() {
        assertEquals("\"a\"", CODECS.describe("a"));
        assertEquals("an object", CODECS.describe(Map.of()));
        assertEquals("a list", CODECS.describe(List.of()));
        assertEquals("nothing", CODECS.describe(null));
        assertEquals("3", CODECS.describe(3));
    }
}
