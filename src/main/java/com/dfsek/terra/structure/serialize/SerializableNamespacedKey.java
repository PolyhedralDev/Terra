package com.dfsek.terra.structure.serialize;

import org.bukkit.NamespacedKey;

import java.io.Serializable;

public class SerializableNamespacedKey implements Serializable {
    public static final long serialVersionUID = 5298928608478640007L;
    private final String namespace;
    private final String key;
    public SerializableNamespacedKey(NamespacedKey key) {
        this.namespace = key.getNamespace();
        this.key = key.getKey();
    }
    public NamespacedKey getNamespacedKey() {
        return new NamespacedKey(namespace, key);
    }
}
