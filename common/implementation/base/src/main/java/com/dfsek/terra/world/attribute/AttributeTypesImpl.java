package com.dfsek.terra.world.attribute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.AttributeTypes;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.world.attribute.value.BlendToGray;
import com.dfsek.terra.api.world.attribute.value.Color;
import com.dfsek.terra.api.world.attribute.value.TriState;


final class AttributeTypesImpl implements AttributeTypes {
    static final AttributeTypesImpl INSTANCE = new AttributeTypesImpl();

    private final AttributeType<Boolean> booleanType = AttributeTypeImpl.builder("boolean", ValueDecoders.BOOLEAN)
        .op(Operation.AND, ValueDecoders.BOOLEAN, (s, a) -> a && s)
        .op(Operation.NAND, ValueDecoders.BOOLEAN, (s, a) -> !a || !s)
        .op(Operation.OR, ValueDecoders.BOOLEAN, (s, a) -> a || s)
        .op(Operation.NOR, ValueDecoders.BOOLEAN, (s, a) -> !a && !s)
        .op(Operation.XOR, ValueDecoders.BOOLEAN, (s, a) -> a ^ s)
        .op(Operation.XNOR, ValueDecoders.BOOLEAN, (s, a) -> a == s)
        .build();

    private final AttributeType<TriState> triState = AttributeTypeImpl.builder("tri_state", ValueDecoders.TRI_STATE).build();

    private final AttributeType<Float> floatType = floatType("float");

    private final AttributeType<Float> angleDegrees = floatType("angle_degrees");

    private final AttributeType<Color> rgbColor = AttributeTypeImpl.builder("rgb_color", ValueDecoders.RGB)
        .op(Operation.ALPHA_BLEND, ValueDecoders.ARGB, AttributeTypesImpl::rgbAlphaBlend)
        .op(Operation.ADD, ValueDecoders.RGB, (s, a) -> new Color(Math.min(s.r() + a.r(), 1f), Math.min(s.g() + a.g(), 1f),
            Math.min(s.b() + a.b(), 1f), 1f))
        .op(Operation.SUBTRACT, ValueDecoders.RGB, (s, a) -> new Color(Math.max(s.r() - a.r(), 0f), Math.max(s.g() - a.g(), 0f),
            Math.max(s.b() - a.b(), 0f), 1f))
        .op(Operation.MULTIPLY, ValueDecoders.RGB, (s, a) -> multiply(s, a, false))
        .op(Operation.BLEND_TO_GRAY, ValueDecoders.BLEND_TO_GRAY, AttributeTypesImpl::blendToGray)
        .build();

    private final AttributeType<Color> argbColor = AttributeTypeImpl.builder("argb_color", ValueDecoders.ARGB)
        .op(Operation.ALPHA_BLEND, ValueDecoders.ARGB, AttributeTypesImpl::argbAlphaBlend)
        .op(Operation.ADD, ValueDecoders.RGB, (s, a) -> new Color(Math.min(s.r() + a.r(), 1f), Math.min(s.g() + a.g(), 1f),
            Math.min(s.b() + a.b(), 1f), s.a()))
        .op(Operation.SUBTRACT, ValueDecoders.RGB, (s, a) -> new Color(Math.max(s.r() - a.r(), 0f), Math.max(s.g() - a.g(), 0f),
            Math.max(s.b() - a.b(), 0f), s.a()))
        .op(Operation.MULTIPLY, ValueDecoders.ARGB, (s, a) -> multiply(s, a, true))
        .op(Operation.BLEND_TO_GRAY, ValueDecoders.BLEND_TO_GRAY, AttributeTypesImpl::blendToGray)
        .build();

    private final AttributeType<Integer> integer = integerType("integer");

    private final AttributeType<Integer> integerOverrideOnly = AttributeTypeImpl.builder("integer_override_only", ValueDecoders.INT)
        .build();

    private final Map<String, AttributeType<RegistryKey>> identifiers = new ConcurrentHashMap<>();

    private final Map<String, AttributeType<Either<RegistryKey, TagKey>>> identifiersOrTags = new ConcurrentHashMap<>();

    private AttributeTypesImpl() { }

    @Override
    public AttributeType<Boolean> booleanType() {
        return booleanType;
    }

    @Override
    public AttributeType<TriState> triState() {
        return triState;
    }

    @Override
    public AttributeType<Float> floatType() {
        return floatType;
    }

    @Override
    public AttributeType<Float> angleDegrees() {
        return angleDegrees;
    }

    @Override
    public AttributeType<Color> rgbColor() {
        return rgbColor;
    }

    @Override
    public AttributeType<Color> argbColor() {
        return argbColor;
    }

    @Override
    public AttributeType<Integer> integer() {
        return integer;
    }

    @Override
    public AttributeType<Integer> integerOverrideOnly() {
        return integerOverrideOnly;
    }

    @Override
    public AttributeType<RegistryKey> identifier(String defaultNamespace) {
        return identifiers.computeIfAbsent(defaultNamespace,
            ns -> AttributeTypeImpl.builder("identifier", ValueDecoders.identifier(ns)).build());
    }

    @Override
    public AttributeType<Either<RegistryKey, TagKey>> identifierOrTag(String defaultNamespace) {
        return identifiersOrTags.computeIfAbsent(defaultNamespace,
            ns -> AttributeTypeImpl.builder("identifier_or_tag", ValueDecoders.identifierOrTag(ns)).build());
    }

    @Override
    public <E extends Enum<E> & StringIdentifiable> AttributeType<E> enumType(String id, Class<E> type) {
        return AttributeTypeImpl.builder(id, ValueDecoders.identifiableEnum(id.replace('_', ' '), type)).build();
    }

    private static AttributeType<Float> floatType(String id) {
        return AttributeTypeImpl.builder(id, ValueDecoders.FLOAT)
            .op(Operation.ALPHA_BLEND, ValueDecoders.FLOAT_WITH_ALPHA, (s, a) -> s + a.alpha() * (a.value() - s))
            .op(Operation.ADD, ValueDecoders.FLOAT, (s, a) -> s + a)
            .op(Operation.SUBTRACT, ValueDecoders.FLOAT, (s, a) -> s - a)
            .op(Operation.MULTIPLY, ValueDecoders.FLOAT, (s, a) -> s * a)
            .op(Operation.MINIMUM, ValueDecoders.FLOAT, (s, a) -> Math.min(s, a))
            .op(Operation.MAXIMUM, ValueDecoders.FLOAT, (s, a) -> Math.max(s, a))
            .composable(Operation.ADD, Float::sum)
            .<Float>composable(Operation.MULTIPLY, (a, b) -> a * b)
            .build();
    }

    private static AttributeType<Integer> integerType(String id) {
        return AttributeTypeImpl.builder(id, ValueDecoders.INT)
            .op(Operation.ADD, ValueDecoders.INT, (s, a) -> s + a)
            .op(Operation.SUBTRACT, ValueDecoders.INT, (s, a) -> s - a)
            .op(Operation.MULTIPLY, ValueDecoders.INT, (s, a) -> s * a)
            .op(Operation.MINIMUM, ValueDecoders.INT, (s, a) -> Math.min(s, a))
            .op(Operation.MAXIMUM, ValueDecoders.INT, (s, a) -> Math.max(s, a))
            .composable(Operation.ADD, Integer::sum)
            .<Integer>composable(Operation.MULTIPLY, (a, b) -> a * b)
            .build();
    }

    private static float lerp(float t, float from, float to) {
        return from + t * (to - from);
    }

    private static float clamp01(float v) {
        return Math.max(0f, Math.min(1f, v));
    }

    private static boolean white(Color c, boolean withAlpha) {
        return c.r() == 1f && c.g() == 1f && c.b() == 1f && (!withAlpha || c.a() == 1f);
    }

    private static Color multiply(Color s, Color a, boolean withAlpha) {
        if(white(s, withAlpha)) return a;
        if(white(a, withAlpha)) return s;
        return new Color(s.r() * a.r(), s.g() * a.g(), s.b() * a.b(), withAlpha ? s.a() * a.a() : 1f);
    }

    private static Color rgbAlphaBlend(Color dst, Color src) {
        if(src.a() == 0f) return dst;
        return new Color(lerp(src.a(), dst.r(), src.r()), lerp(src.a(), dst.g(), src.g()), lerp(src.a(), dst.b(), src.b()), 1f);
    }

    private static Color argbAlphaBlend(Color dst, Color src) {
        float sa = src.a();
        if(sa == 1f) return src;
        if(sa == 0f) return dst;
        float a = sa + dst.a() * (1f - sa);
        return new Color((src.r() * sa + dst.r() * (a - sa)) / a, (src.g() * sa + dst.g() * (a - sa)) / a,
            (src.b() * sa + dst.b() * (a - sa)) / a, a);
    }

    private static Color blendToGray(Color s, BlendToGray arg) {
        float g = 0.3f * s.r() + 0.59f * s.g() + 0.11f * s.b();
        float grey = clamp01(g * arg.brightness());
        return new Color(lerp(arg.factor(), s.r(), grey), lerp(arg.factor(), s.g(), grey), lerp(arg.factor(), s.b(), grey), s.a());
    }
}
