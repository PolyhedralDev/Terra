package com.dfsek.terra.allay;

import org.allaymc.api.utils.hash.HashUtils;

import java.util.Map;
import java.util.TreeMap;


/**
 * @author daoge_cmd
 */
public class JeBlockState {

    protected final String identifier;
    protected final TreeMap<String, String> properties;

    protected int hash = Integer.MAX_VALUE;

    private JeBlockState(String data) {
        // TODO: support block state with nbt (identifier[properties]{nbt}), for now we just ignore it
        int braceIndex = data.indexOf('{');
        if (braceIndex != -1) {
            data = data.substring(0, braceIndex);
        }

        String[] strings = data
            .replace("[", ",")
            .replace("]", ",")
            .replace(" ", "")
            .split(",");

        this.identifier = strings[0];
        this.properties = new TreeMap<>();
        if(strings.length > 1) {
            for(int i = 1; i < strings.length; i++) {
                final String tmp = strings[i];
                final int index = tmp.indexOf("=");
                properties.put(tmp.substring(0, index), tmp.substring(index + 1));
            }
        }
        completeMissingProperties();
    }

    private JeBlockState(String identifier, TreeMap<String, String> properties) {
        this.identifier = identifier;
        this.properties = properties;
    }

    public static JeBlockState fromString(String data) {
        return new JeBlockState(data);
    }

    public static JeBlockState create(String identifier, TreeMap<String, String> properties) {
        return new JeBlockState(identifier, properties);
    }

    public String getPropertyValue(String key) {
        return properties.get(key);
    }

    private void completeMissingProperties() {
        Map<String, String> defaultProperties = Mapping.getJeBlockDefaultProperties(identifier);
        if(properties.size() == defaultProperties.size()) {
            return;
        }
        defaultProperties.entrySet().stream().filter(entry -> !properties.containsKey(entry.getKey())).forEach(
            entry -> properties.put(entry.getKey(), entry.getValue()));
    }

    public String toString(boolean includeProperties) {
        if(!includeProperties) return identifier;
        StringBuilder builder = new StringBuilder(identifier).append(";");
        properties.forEach((k, v) -> builder.append(k).append("=").append(v).append(";"));
        String str = builder.toString();
        if(hash == Integer.MAX_VALUE) {
            hash = HashUtils.fnv1a_32(str.getBytes());
        }
        return str;
    }

    public int getHash() {
        if(hash == Integer.MAX_VALUE) {
            hash = HashUtils.fnv1a_32(toString(true).getBytes());
        }
        return hash;
    }

    @Override
    public String toString() {
        return toString(true);
    }
}
