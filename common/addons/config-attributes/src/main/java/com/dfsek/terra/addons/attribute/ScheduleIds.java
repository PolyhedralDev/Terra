package com.dfsek.terra.addons.attribute;

import java.util.Set;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;


final class ScheduleIds {
    private ScheduleIds() { }

    static RegistryKey claim(String id, Set<RegistryKey> claimed) {
        if(id.startsWith("#")) throw new IllegalArgumentException("schedule IDs must not be tags");
        RegistryKey key;
        try {
            key = RegistryKey.parseNamespaced(id);
        } catch(IllegalArgumentException e) {
            if(id.indexOf(':') < 0) throw new IllegalArgumentException("schedule IDs must include a namespace, e.g. \"mypack:seasons\"", e);
            throw e;
        }
        if(!claimed.add(key)) throw new IllegalArgumentException("duplicate schedule ID");
        return key;
    }

    static Either<RegistryKey, TagKey> reference(String value) {
        if(value.startsWith("#")) return Either.right(TagKey.parse(value));
        return Either.left(RegistryKey.parseNamespaced(value));
    }
}
