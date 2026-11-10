package com.dfsek.terra.world.attribute;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.AttributeCodec;
import com.dfsek.terra.api.world.attribute.AttributeCodecs;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.RawValue;


final class AttributeCodecsImpl implements AttributeCodecs {
    static final AttributeCodecsImpl INSTANCE = new AttributeCodecsImpl();

    private AttributeCodecsImpl() { }

    @Override
    public <T> AttributeCodec<T> of(Decoder<T> decoder, Encoder<T> encoder) {
        return ValueDecoders.codec(decoder, encoder);
    }

    @Override
    public String describe(@Nullable Object value) {
        return ValueDecoders.got(value);
    }

    @Override
    public AttributeCodec<Boolean> booleanCodec() {
        return ValueDecoders.BOOLEAN;
    }

    @Override
    public AttributeCodec<Float> floatCodec() {
        return ValueDecoders.FLOAT;
    }

    @Override
    public AttributeCodec<Double> doubleCodec() {
        return ValueDecoders.DOUBLE;
    }

    @Override
    public AttributeCodec<Integer> intCodec() {
        return ValueDecoders.INT;
    }

    @Override
    public AttributeCodec<Integer> nonNegativeInt() {
        return ValueDecoders.NON_NEGATIVE_INT;
    }

    @Override
    public AttributeCodec<Float> unitFloat() {
        return ValueDecoders.UNIT_FLOAT;
    }

    @Override
    public AttributeCodec<String> string(String expected) {
        return ValueDecoders.string(expected);
    }

    @Override
    public AttributeCodec<RawValue> raw() {
        return ValueDecoders.RAW;
    }

    @Override
    public <V> AttributeCodec<List<V>> list(AttributeCodec<V> element) {
        return ValueDecoders.list(element);
    }

    @Override
    public <V> AttributeCodec<List<V>> compactList(AttributeCodec<V> element) {
        return ValueDecoders.compactList(element);
    }

    @Override
    public <E extends Enum<E> & StringIdentifiable> AttributeCodec<E> enumCodec(String name, Class<E> type) {
        return ValueDecoders.identifiableEnum(name, type);
    }

    @Override
    public AttributeCodec<RegistryKey> identifier(String defaultNamespace) {
        return ValueDecoders.identifier(defaultNamespace);
    }

    @Override
    public AttributeCodec<Either<RegistryKey, TagKey>> identifierOrTag(String defaultNamespace) {
        return ValueDecoders.identifierOrTag(defaultNamespace);
    }

    @Override
    public ObjectReader reader(String what, Object raw, DecodeContext context) throws DecodeException {
        return new ValueDecoders.Fields(what, raw, context);
    }

    @Override
    public ObjectWriter writer() {
        return new ValueDecoders.Encoded();
    }
}
