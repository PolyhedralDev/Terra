package com.dfsek.terra.addons.attribute;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeMap;
import com.dfsek.terra.api.world.attribute.AttributeScope;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;


public final class AttributeReader {
    private final RegistryAttributeLookup lookup;
    private final AttributeHandle handle;

    public AttributeReader(@Nullable Registry<Attribute<?>> registry, AttributeHandle handle) {
        this.lookup = new RegistryAttributeLookup(registry);
        this.handle = handle;
    }

    public enum Scope { BIOME, PACK }


    public record Result(AttributeMap map, List<Problem> problems) { }


    public record Problem(String key, String reason) { }


    private record Value(Object raw, DecodeContext context) { }

    public Result read(Map<String, Object> raw, Scope scope) {
        DecodeContext plain = handle.plainContext();
        Map<String, Value> values = new LinkedHashMap<>();
        raw.forEach((key, value) -> values.put(key, new Value(value, plain)));
        return readValues(values, scope);
    }

    Result readConfig(Map<String, LazyConfigValue> config, Scope scope) {
        DecodeContext plain = handle.plainContext();
        Map<String, Value> values = new LinkedHashMap<>();
        config.forEach((key, value) -> values.put(key, value == null
                                                       ? new Value(null, plain)
                                                       : new Value(value.raw(), value.context(plain))));
        return readValues(values, scope);
    }

    private Result readValues(Map<String, Value> raw, Scope scope) {
        AttributeMap.Builder builder = AttributeMap.builder();
        List<Problem> problems = new ArrayList<>();
        for(Map.Entry<String, Value> entry : raw.entrySet()) {
            String keyString = entry.getKey();
            Attribute<?> attribute;
            try {
                attribute = lookup.find(keyString);
            } catch(DecodeException e) {
                problems.add(new Problem(keyString, e.getMessage()));
                continue;
            }
            if(scope == Scope.BIOME && attribute.scope() == AttributeScope.PACK_ONLY) {
                problems.add(new Problem(keyString, "not allowed in a biome"));
                continue;
            }
            // Explicit null un-sets an inherited value.
            if(entry.getValue().raw() == null) continue;
            try {
                put(builder, attribute, entry.getValue().raw(), entry.getValue().context());
            } catch(DecodeException | RuntimeException e) {
                problems.add(new Problem(keyString, e.getMessage() == null ? "unexpected error" : e.getMessage()));
            }
        }
        return new Result(builder.build(), problems);
    }

    private <T> void put(AttributeMap.Builder builder, Attribute<T> attribute, Object value, DecodeContext context)
    throws DecodeException {
        builder.put(attribute.key(), handle.decodeEntry(attribute, value, context));
    }
}
