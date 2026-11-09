package com.dfsek.terra.mod.attribute;

import net.minecraft.SharedConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.Bootstrap;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.attribute.EnvironmentAttribute;

import java.util.List;

import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributeTable;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeLookup;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.world.attribute.AttributeHandleImpl;


final class MinecraftTestSupport {
    static {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    static final HolderLookup.Provider REGISTRIES = VanillaRegistries.createWorldLookup();
    static final VanillaContext CONTEXT = VanillaContext.of(REGISTRIES, key -> null);
    static final AttributeHandle HANDLE = new AttributeHandleImpl();
    static final List<Attribute<?>> ATTRIBUTES = MinecraftAttributeTable.create(HANDLE);

    private static final Registry<Attribute<?>> TABLE = registry();

    static final AttributeLookup LOOKUP = key -> {
        RegistryKey id;
        try {
            id = RegistryKey.parseNamespaced(key);
        } catch(IllegalArgumentException e) {
            throw new DecodeException("unknown attribute", e);
        }
        return TABLE.get(id).orElseThrow(() -> new DecodeException("unknown attribute"));
    };

    private MinecraftTestSupport() { }

    static List<HolderLookup.RegistryLookup<?>> loadContext(net.minecraft.core.Registry.PendingTags<?>... pending) {
        return TagLoader.buildUpdatedLookups(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), List.of(pending));
    }

    static Registry<Attribute<?>> registry(Attribute<?>... extra) {
        OpenRegistryImpl<Attribute<?>> registry = new OpenRegistryImpl<>(Attribute.REGISTRY_TYPE);
        ATTRIBUTES.forEach(attribute -> registry.register(attribute.getRegistryKey(), attribute));
        for(Attribute<?> attribute : extra) registry.register(attribute.getRegistryKey(), attribute);
        return registry;
    }

    @SuppressWarnings("unchecked")
    static <T> Attribute<T> attribute(String id) {
        AttributeKey<T> key = AttributeKey.parse(id);
        return (Attribute<T>) TABLE.get(key.getRegistryKey()).orElseThrow(() -> new IllegalArgumentException("no attribute " + id));
    }

    static EnvironmentAttribute<?> vanilla(String id) {
        return BuiltInRegistries.ENVIRONMENT_ATTRIBUTE.getOptional(Identifier.parse(id))
            .orElseThrow(() -> new IllegalArgumentException("no vanilla attribute " + id));
    }
}
