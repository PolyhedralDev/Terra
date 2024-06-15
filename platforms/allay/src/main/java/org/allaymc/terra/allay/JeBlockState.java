package org.allaymc.terra.allay;

import org.allaymc.api.utils.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public class JeBlockState {
    protected final String identifier;
    protected final TreeMap<String, String> properties;

    public static JeBlockState fromString(String data) {
        return new JeBlockState(data);
    }

    public static JeBlockState create(String identifier, TreeMap<String, String> properties) {
        return new JeBlockState(identifier, properties);
    }

    private JeBlockState(String data) {
        var strings = data.replace("[", ",").replace("]", ",").replace(" ", "").split(",");
        this.identifier = strings[0];
        this.properties = new TreeMap<>();
        if (strings.length > 1) {
            for (int i = 1; i < strings.length; i++) {
                final var tmp = strings[i];
                final var index = tmp.indexOf("=");
                properties.put(tmp.substring(0, index), tmp.substring(index + 1));
            }
        }
    }

    private JeBlockState(String identifier, TreeMap<String, String> properties) {
        this.identifier = identifier;
        this.properties = properties;
    }

    public String toString(boolean includeProperties) {
        if(!includeProperties) return identifier;
        StringBuilder builder = new StringBuilder(identifier).append(";");
        properties.forEach((k, v) -> builder.append(k).append("=").append(v).append(";"));
        return builder.toString();
    }

    @Override
    public String toString() {
        return toString(true);
    }
}
