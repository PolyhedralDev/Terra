package com.dfsek.terra.addons.attribute;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeLookup;
import com.dfsek.terra.api.world.attribute.DecodeException;


/**
 * Keys must be fully namespaced: inheritance merges config maps by raw key text.
 */
public final class RegistryAttributeLookup implements AttributeLookup {
    private static final String NAMESPACE_REQUIRED = "attribute keys must include a namespace, e.g. \"minecraft:visual/fog_color\"";
    private static final String UNKNOWN = "unknown attribute";
    private static final Map<String, String> NAMESPACE_ADDONS = Map.of(
        "minecraft", "minecraft-attributes",
        "terra", "minecraft-attributes");

    private final @Nullable Registry<Attribute<?>> registry;

    public RegistryAttributeLookup(@Nullable Registry<Attribute<?>> registry) {
        this.registry = registry;
    }

    @Override
    public Attribute<?> find(String key) throws DecodeException {
        AttributeKey<?> parsed;
        try {
            parsed = AttributeKey.parse(key);
        } catch(IllegalArgumentException e) {
            if(key.indexOf(':') < 0) throw new DecodeException(NAMESPACE_REQUIRED, e);
            throw new DecodeException(invalidKey(e), e);
        }
        Attribute<?> attribute = registry == null ? null : registry.get(parsed.getRegistryKey()).orElse(null);
        if(attribute == null) throw new DecodeException(unknownAttribute(parsed.getRegistryKey()));
        return attribute;
    }

    private static String invalidKey(IllegalArgumentException e) {
        Throwable cause = e.getCause() == null ? e : e.getCause();
        String message = cause.getMessage();
        return message == null ? "invalid attribute key" : "invalid attribute key: " + message;
    }

    private String unknownAttribute(RegistryKey key) {
        String namespace = key.getNamespace();
        String addon = NAMESPACE_ADDONS.get(namespace);
        if(addon == null) return UNKNOWN;
        if(registry != null && registry.keys().stream().anyMatch(registered -> registered.getNamespace().equals(namespace))) {
            return UNKNOWN;
        }
        return UNKNOWN + ": no \"" + namespace + "\" attributes are registered; add the \"" + addon + "\" addon to the pack's addons";
    }
}
