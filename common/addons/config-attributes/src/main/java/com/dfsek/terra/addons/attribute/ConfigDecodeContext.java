package com.dfsek.terra.addons.attribute;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;


/**
 * Tectonic picks preprocessors by declared type.
 * <p>
 * Not thread safe: include preprocessors mutate the {@link DepthTracker}.
 */
final class ConfigDecodeContext implements DecodeContext {
    static final List<Class<?>> CAPTURED_TYPES = List.of(LazyConfigValue.class, MapShape.class, ListShape.class);

    private static final AnnotatedType WHOLE_VALUE = new TypeKey<@Meta LazyConfigValue>() { }.getAnnotatedType();
    private static final AnnotatedType MAP = new TypeKey<@Meta MapShape<String, LazyConfigValue>>() { }.getAnnotatedType();
    private static final AnnotatedType LIST = new TypeKey<@Meta ListShape<LazyConfigValue>>() { }.getAnnotatedType();
    private static final AnnotatedType STRING = new TypeKey<@Meta String>() { }.getAnnotatedType();
    private static final AnnotatedType NUMBER = new TypeKey<@Meta Double>() { }.getAnnotatedType();

    private final ConfigLoader loader;
    private final DepthTracker tracker;
    private final DecodeContext plain;
    private final Set<Object> resolved = Collections.newSetFromMap(new IdentityHashMap<>());

    ConfigDecodeContext(ConfigLoader loader, DepthTracker tracker, DecodeContext plain) {
        this.loader = loader;
        this.tracker = tracker;
        this.plain = plain;
    }

    private static boolean numeric(Type type) {
        return type instanceof Class<?> c && Number.class.isAssignableFrom(c);
    }

    private static DecodeException failure(RuntimeException e) {
        String message = e instanceof ConfigException && e.getMessage() != null ? e.getMessage().strip() : "config preprocessing failed";
        // Tectonic leaves some details only in the cause.
        Throwable cause = e.getCause();
        boolean readable = cause instanceof ConfigException
                           || cause != null && !(cause instanceof RuntimeException || cause instanceof Error);
        if(readable && cause.getMessage() != null && !message.contains(cause.getMessage().strip())) {
            message += "\n\tCaused by: " + cause.getMessage().strip();
        }
        return new DecodeException(message, e);
    }

    @Override
    public <V> V load(Type type, Object raw) throws DecodeException {
        Object value = resolve(raw);
        if(value instanceof String expression && numeric(type)) {
            try {
                value = loadType(NUMBER, expression, tracker);
            } catch(RuntimeException e) {
                throw failure(e);
            }
        }
        return plain.load(type, value);
    }

    @Override
    public Object resolve(Object raw) throws DecodeException {
        try {
            return resolve(raw, tracker);
        } catch(RuntimeException e) {
            throw failure(e);
        }
    }

    private Object resolve(Object raw, DepthTracker at) {
        if(raw == null || raw instanceof Number || raw instanceof Boolean || resolved.contains(raw)) return raw;
        LazyConfigValue whole = capture(WHOLE_VALUE, raw, at);
        Object value = whole.raw();
        Object out;
        if(value instanceof String) {
            out = loadType(STRING, raw, at);
        } else if(value instanceof Map<?, ?>) {
            LazyConfigValue included = capture(MAP, value, whole.tracker());
            Map<Object, Object> map = new LinkedHashMap<>();
            for(Map.Entry<?, ?> entry : ((Map<?, ?>) included.raw()).entrySet()) {
                map.put(entry.getKey(), resolve(entry.getValue(), included.tracker().entry(String.valueOf(entry.getKey()))));
            }
            out = Collections.unmodifiableMap(map);
        } else if(value instanceof List<?>) {
            LazyConfigValue included = capture(LIST, value, whole.tracker());
            List<?> elements = (List<?>) included.raw();
            List<Object> list = new ArrayList<>(elements.size());
            for(int i = 0; i < elements.size(); i++) list.add(resolve(elements.get(i), included.tracker().index(i)));
            out = Collections.unmodifiableList(list);
        } else {
            out = value;
        }
        if(out != null) resolved.add(out);
        return out;
    }

    private LazyConfigValue capture(AnnotatedType type, Object raw, DepthTracker at) {
        return (LazyConfigValue) loadType(type, raw, at);
    }

    private Object loadType(AnnotatedType type, Object raw, DepthTracker at) {
        try {
            return loader.loadType(type, raw, at);
        } catch(ConfigException e) {
            throw e;
        } catch(RuntimeException e) {
            throw new LoadException("config preprocessing failed", e, at);
        }
    }


    private abstract static class MapShape<K, V> extends AbstractMap<K, V> { }


    private abstract static class ListShape<E> extends AbstractList<E> { }
}
