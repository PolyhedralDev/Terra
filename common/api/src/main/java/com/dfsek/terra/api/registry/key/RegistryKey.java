package com.dfsek.terra.api.registry.key;

import java.util.regex.Pattern;


public final class RegistryKey implements StringIdentifiable, Namespaced {
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("^[a-zA-Z0-9_.-]*$");
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_./-]*$");
    private final String namespace;
    private final String id;
    private final int hash;

    private RegistryKey(String namespace, String id) {
        if(!NAMESPACE_PATTERN.matcher(namespace).matches()) {
            throw new IllegalArgumentException(
                "Namespace must only contain alphanumeric characters, hyphens, underscores, and periods. \"" + namespace +
                "\" is not a valid namespace.");
        }

        if(!ID_PATTERN.matcher(id).matches()) {
            throw new IllegalArgumentException(
                "ID must only contain alphanumeric characters, hyphens, underscores, periods, and slashes. \"" + id +
                "\" is not a valid ID.");
        }

        this.namespace = namespace;
        this.id = id;
        this.hash = 31 * (31 + namespace.hashCode()) + id.hashCode();
    }

    public static RegistryKey parse(String key) {
        if(key.chars().filter(c -> c == ':').count() != 1) {
            throw new IllegalArgumentException("Malformed RegistryKey: " + key);
        }
        String namespace = key.substring(0, key.indexOf(":"));
        String id = key.substring(key.indexOf(":") + 1);

        return new RegistryKey(namespace, id);
    }

    /**
     * Parses {@code namespace:id}, or {@code id} in {@code defaultNamespace}.
     *
     * @throws IllegalArgumentException if a part is empty or invalid, or the key holds more than one {@code :}
     */
    public static RegistryKey parse(String key, String defaultNamespace) {
        // Shown to users in config warnings: no Java type names.
        if(key == null) throw new IllegalArgumentException("malformed ID: none given");
        int colon = key.indexOf(':');
        if(colon != key.lastIndexOf(':')) throw new IllegalArgumentException(malformed(key, "more than one ':'"));
        String namespace = colon < 0 ? defaultNamespace : key.substring(0, colon);
        String id = key.substring(colon + 1);
        if(namespace == null || namespace.isEmpty()) throw new IllegalArgumentException(malformed(key, "empty namespace"));
        if(id.isEmpty()) throw new IllegalArgumentException(malformed(key, "empty ID"));
        return new RegistryKey(namespace, id);
    }

    private static String malformed(String key, String reason) {
        return "malformed ID \"" + key + "\": " + reason;
    }

    /**
     * @throws IllegalArgumentException if the key has no namespace or is otherwise invalid
     */
    public static RegistryKey parseNamespaced(String key) {
        if(key != null && key.indexOf(':') < 0) {
            throw new IllegalArgumentException("a namespace is required, as in \"namespace:" + key + "\"");
        }
        return parse(key, "");
    }

    public static RegistryKey of(String namespace, String id) {
        return new RegistryKey(namespace, id);
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj instanceof RegistryKey that) {
            return this.hash == that.hash && this.id.equals(that.id) && this.namespace.equals(that.namespace);
        }
        return false;
    }

    @Override
    public String toString() {
        return namespace + ":" + id;
    }
}
