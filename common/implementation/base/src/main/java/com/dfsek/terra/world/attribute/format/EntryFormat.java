package com.dfsek.terra.world.attribute.format;

import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;


public final class EntryFormat {
    private static final String MODIFIER = "modifier";
    private static final String ARGUMENT = "argument";
    private static final Set<String> MODIFIER_KEYS = Set.of(MODIFIER, ARGUMENT);

    private EntryFormat() { }

    public static <T> AttributeEntry<T> decode(Attribute<T> attribute, @Nullable Object raw, DecodeContext context)
    throws DecodeException {
        if(raw == null) throw new DecodeException("missing value");
        Object value = context.resolve(raw);
        if(value == null) throw new DecodeException("missing value");
        if(value instanceof Map<?, ?> map && map.containsKey(MODIFIER)) {
            return decodeModifier(attribute, map, context);
        }
        return AttributeEntry.override(attribute.validate(attribute.type().decode(value, context)));
    }

    @SuppressWarnings("unchecked")
    public static <T> Object encode(Attribute<T> attribute, AttributeEntry<T> entry) {
        if(entry.isOverride()) {
            if(entry.argument() == null) {
                throw new IllegalArgumentException("a null override has no pack-format form");
            }
            return attribute.type().encode((T) entry.argument());
        }
        Map<String, Object> encoded = new LinkedHashMap<>();
        encoded.put(MODIFIER, entry.operation().getID());
        encoded.put(ARGUMENT, attribute.type().encodeArgument(entry.operation(), entry.argument()));
        return encoded;
    }

    private static <T> AttributeEntry<T> decodeModifier(Attribute<T> attribute, Map<?, ?> map, DecodeContext context)
    throws DecodeException {
        if(!map.keySet().equals(MODIFIER_KEYS)) {
            throw new DecodeException("modifier entry must have exactly the keys \"modifier\" and \"argument\"");
        }
        Operation operation;
        try {
            operation = Operation.parse(context.load(String.class, map.get(MODIFIER)));
        } catch(IllegalArgumentException e) {
            throw new DecodeException(e.getMessage(), e);
        } catch(DecodeException e) {
            throw new DecodeException("\"modifier\": " + e.getMessage(), e);
        }
        if(!attribute.type().operations().contains(operation)) {
            throw new DecodeException("modifier " + operation.getID() + " is not valid for " + attribute.key());
        }
        Object argument = attribute.type().decodeArgument(operation, map.get(ARGUMENT), context);
        if(argument == null && operation != Operation.OVERRIDE) throw new DecodeException("\"argument\": missing value");
        if(operation == Operation.OVERRIDE) {
            @SuppressWarnings("unchecked")
            T value = (T) argument;
            attribute.validate(value);
        }
        return AttributeEntry.of(operation, argument);
    }
}
