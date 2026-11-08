package com.dfsek.terra.world.attribute;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;

import static com.dfsek.terra.world.attribute.ValueDecoders.got;


final class PlainDecodeContext implements DecodeContext {
    static final PlainDecodeContext INSTANCE = new PlainDecodeContext();

    private PlainDecodeContext() { }

    private static Class<?> rawClass(Type type) {
        if(type instanceof Class<?> c) return c;
        if(type instanceof ParameterizedType p && p.getRawType() instanceof Class<?> c) return c;
        return null;
    }

    @Override
    public Object resolve(Object raw) {
        return raw;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V load(Type type, Object raw) throws DecodeException {
        Class<?> c = rawClass(type);
        Object value;
        if(c == Object.class) value = raw;
        else if(c == Float.class) value = number(raw).floatValue();
        else if(c == Double.class) value = number(raw).doubleValue();
        else if(c == Integer.class) {
            if(!(raw instanceof Number n)) throw new DecodeException("expected an integer, got " + got(raw));
            value = ValueDecoders.integer(n, raw);
        } else if(c == Boolean.class) {
            if(!(raw instanceof Boolean)) throw new DecodeException("expected a boolean (true or false), got " + got(raw));
            value = raw;
        } else if(c == String.class) {
            if(!(raw instanceof String)) throw new DecodeException("expected a string, got " + got(raw));
            value = raw;
        } else if(c == List.class) {
            if(!(raw instanceof List<?>)) throw new DecodeException("expected a list, got " + got(raw));
            value = raw;
        } else if(c == Map.class) {
            if(!(raw instanceof Map<?, ?>)) throw new DecodeException("expected an object, got " + got(raw));
            value = raw;
        } else {
            throw new DecodeException("cannot load this kind of value");
        }
        return (V) value;
    }

    private static Number number(Object raw) throws DecodeException {
        if(raw instanceof Number n) return n;
        throw new DecodeException("expected a number, got " + got(raw));
    }
}
