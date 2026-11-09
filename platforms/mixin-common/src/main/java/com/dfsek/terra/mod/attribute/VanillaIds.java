package com.dfsek.terra.mod.attribute;

import net.minecraft.resources.Identifier;

import com.dfsek.terra.api.registry.key.RegistryKey;


public final class VanillaIds {
    private VanillaIds() { }

    public static Identifier identifier(RegistryKey key) {
        Identifier identifier = Identifier.tryBuild(key.getNamespace(), key.getID());
        if(identifier == null) {
            throw new IllegalArgumentException(
                "invalid ID \"" + key + "\": Minecraft IDs use only lower-case a-z, 0-9, _, . and -, and / in the path");
        }
        return identifier;
    }
}
