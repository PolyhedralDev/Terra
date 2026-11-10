package com.dfsek.terra.api.world.attribute;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;


public interface AttributeCodecs {
    @FunctionalInterface
    interface Decoder<T> {
        T decode(Object raw, DecodeContext context) throws DecodeException;
    }


    @FunctionalInterface
    interface Encoder<T> {
        Object encode(T value);
    }


    /**
     * Not thread safe.
     */
    interface ObjectReader {
        /**
         * In config order.
         */
        List<String> keys();

        /**
         * @throws DecodeException if the key is missing or its value does not decode
         */
        <V> V required(String key, AttributeCodec<V> codec) throws DecodeException;

        <V> Optional<V> optional(String key, AttributeCodec<V> codec) throws DecodeException;

        <V> V orDefault(String key, AttributeCodec<V> codec, V fallback) throws DecodeException;

        /**
         * @throws DecodeException if the object has a key that was not read
         */
        void finish() throws DecodeException;
    }


    /**
     * Not thread safe. Keeps fields in the order written.
     */
    interface ObjectWriter {
        <V> ObjectWriter put(String key, AttributeCodec<V> codec, V value);

        <V> ObjectWriter optional(String key, AttributeCodec<V> codec, Optional<V> value);

        Map<String, Object> map();
    }

    <T> AttributeCodec<T> of(Decoder<T> decoder, Encoder<T> encoder);

    /**
     * Names {@code value} for messages, such as {@code "text"}, {@code an object} or {@code nothing}.
     */
    String describe(@Nullable Object value);

    AttributeCodec<Boolean> booleanCodec();

    AttributeCodec<Float> floatCodec();

    AttributeCodec<Double> doubleCodec();

    /**
     * Rejects fractions and values outside the {@code int} range.
     */
    AttributeCodec<Integer> intCodec();

    AttributeCodec<Integer> nonNegativeInt();

    AttributeCodec<Float> unitFloat();

    /**
     * @param expected the start of the message when the value is not a string, as in {@code expected a sound ID}
     */
    AttributeCodec<String> string(String expected);

    AttributeCodec<RawValue> raw();

    <V> AttributeCodec<List<V>> list(AttributeCodec<V> element);

    /**
     * Also reads a single element as a list of one. Always encodes a list.
     */
    <V> AttributeCodec<List<V>> compactList(AttributeCodec<V> element);

    /**
     * Reads constants by {@link StringIdentifiable#getID() ID}.
     *
     * @param name names the enum in messages, such as {@code moon phase}
     *
     * @throws IllegalArgumentException if two constants share an ID
     */
    <E extends Enum<E> & StringIdentifiable> AttributeCodec<E> enumCodec(String name, Class<E> type);

    /**
     * A bare ID takes {@code defaultNamespace}. Tags are rejected.
     */
    AttributeCodec<RegistryKey> identifier(String defaultNamespace);

    /**
     * A bare ID takes {@code defaultNamespace}.
     */
    AttributeCodec<Either<RegistryKey, TagKey>> identifierOrTag(String defaultNamespace);

    /**
     * @param what names the object in messages, such as {@code music}
     *
     * @throws DecodeException if {@code raw} is not an object, or has a key that is not a string
     */
    ObjectReader reader(String what, Object raw, DecodeContext context) throws DecodeException;

    ObjectWriter writer();
}
