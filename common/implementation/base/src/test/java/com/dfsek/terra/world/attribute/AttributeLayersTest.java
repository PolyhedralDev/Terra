package com.dfsek.terra.world.attribute;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeParents;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.AttributeTypes;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.api.world.attribute.value.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SuppressWarnings("unchecked")
class AttributeLayersTest {
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final AttributeTypes TYPES = HANDLE.types();

    private static final Attribute<Float> FOG_END = attribute("test:fog_end", TYPES.floatType(), 1024f)
        .range(0, Double.POSITIVE_INFINITY).build();
    private static final Attribute<Float> WATER_FOG_END = attribute("test:water_fog_end", TYPES.floatType(), 96f)
        .range(0, Double.POSITIVE_INFINITY).build();
    private static final Attribute<Color> FOG_COLOR = attribute("test:fog_color", TYPES.rgbColor(), Color.rgb(0)).build();
    private static final Attribute<Color> FOLIAGE_COLOR = attribute("test:foliage_color", TYPES.rgbColor(), null).build();
    private static final Attribute<Float> STAR_BRIGHTNESS = attribute("test:star_brightness", TYPES.floatType(), 0f).range(0, 1).build();

    private static <T> Attribute.Builder<T> attribute(String key, AttributeType<T> type, T defaultValue) {
        return HANDLE.attributeBuilder(AttributeKey.<T>parse(key), type, defaultValue);
    }

    @Test
    void layerOrder() {
        assertEquals(1024f, HANDLE.resolve(FOG_END), "default alone");
        assertEquals(5f, HANDLE.resolve(FOG_END, null, AttributeEntry.override(5f), null), "null layers are skipped");
        assertEquals(1f, HANDLE.resolve(STAR_BRIGHTNESS, AttributeEntry.override(2f)), "the range clamps the result");
        assertEquals(81.6f, HANDLE.resolve(WATER_FOG_END, AttributeEntry.override(96f), AttributeEntry.of(Operation.MULTIPLY, 0.85f)),
            1e-3f, "pack then biome multiply");

        AttributeEntry<Color> pack = AttributeEntry.override(Color.rgb(0x808080));
        AttributeEntry<Color> vanilla = AttributeEntry.override(Color.rgb(0xc0d8ff));
        AttributeEntry<Color> terra = AttributeEntry.of(Operation.ADD, Color.rgb(0x101010));
        assertEquals(0xc0d8ff, HANDLE.resolve(FOG_COLOR, pack, vanilla).toRgbInt(), "vanilla parent wins over the pack override");
        assertEquals(0xd0e8ff, HANDLE.resolve(FOG_COLOR, pack, vanilla, terra).toRgbInt(), "Terra modifier on the vanilla parent");
    }

    @Test
    void modifiersOnNull() {
        AttributeEntry<Color> add = AttributeEntry.of(Operation.ADD, Color.rgb(0x101010));
        assertNull(HANDLE.resolve(FOLIAGE_COLOR));
        assertNull(HANDLE.resolve(FOLIAGE_COLOR, add));
        assertEquals(0x305070, HANDLE.resolve(FOLIAGE_COLOR, add, AttributeEntry.override(Color.rgb(0x204060)), add).toRgbInt());
    }

    @Test
    void parentsNoneIsEmpty() {
        assertNull(AttributeParents.NONE.biomeOrNull(null, FOG_END));
        assertNull(AttributeParents.NONE.packOrNull(null, FOG_END));
    }

    @Test
    void packAttributesFromContext() {
        Context context = new Context();
        ConfigPack pack = (ConfigPack) Proxy.newProxyInstance(ConfigPack.class.getClassLoader(), new Class<?>[]{ ConfigPack.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "getContext" -> context;
                default -> throw new UnsupportedOperationException(method.getName());
            });
        assertTrue(HANDLE.pack(pack).isEmpty());
        PackAttributes attributes = (PackAttributes) Proxy.newProxyInstance(PackAttributes.class.getClassLoader(),
            new Class<?>[]{ PackAttributes.class }, (proxy, method, args) -> {
                throw new UnsupportedOperationException(method.getName());
            });
        context.put(Context.create(PackAttributes.class), attributes);
        assertSame(attributes, HANDLE.pack(pack).orElseThrow());
    }
}
