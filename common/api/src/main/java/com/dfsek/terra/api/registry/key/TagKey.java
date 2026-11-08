package com.dfsek.terra.api.registry.key;

public final class TagKey {
    private final RegistryKey key;

    private TagKey(RegistryKey key) {
        this.key = key;
    }

    public static TagKey of(RegistryKey key) {
        return new TagKey(key);
    }

    /**
     * @throws IllegalArgumentException if the string does not start with {@code #}, has no namespace, or is not a valid
     *                                  {@link RegistryKey}
     */
    public static TagKey parse(String tag) {
        String body = body(tag);
        try {
            return new TagKey(RegistryKey.parseNamespaced(body));
        } catch(IllegalArgumentException e) {
            if(body.indexOf(':') < 0) {
                throw new IllegalArgumentException(
                    "invalid tag \"" + tag + "\": a namespace is required, as in \"#namespace:" + body + "\"", e);
            }
            throw e;
        }
    }

    /**
     * Parses {@code #namespace:id}, or {@code #id} in {@code defaultNamespace}.
     *
     * @throws IllegalArgumentException if the string does not start with {@code #} or is not a valid {@link RegistryKey}
     */
    public static TagKey parse(String tag, String defaultNamespace) {
        return new TagKey(RegistryKey.parse(body(tag), defaultNamespace));
    }

    private static String body(String tag) {
        if(tag == null || !tag.startsWith("#")) throw new IllegalArgumentException("invalid tag (expected #namespace:id): \"" + tag + "\"");
        return tag.substring(1);
    }

    public RegistryKey key() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TagKey that && key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return "#" + key;
    }
}
