package org.allaymc.terra.allay;

import org.allaymc.api.utils.Identifier;

import java.util.Map;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public class JeBlockState {
    protected final Identifier identifier;
    protected final Map<String, String> properties;

    public static JeBlockState fromString(String data) {
        // TODO
        return null;
    }

    public JeBlockState(Identifier identifier, Map<String, String> properties) {
        this.identifier = identifier;
        this.properties = properties;
    }

    public String toString(boolean includeProperties) {
        if(!includeProperties) return identifier.toString();
        StringBuilder builder = new StringBuilder(identifier.toString()).append(";");
        properties.forEach((k, v) -> builder.append(k).append("=").append(v).append(";"));
        return builder.toString();
    }

    public boolean hasProperty(String name) {
        return properties.containsKey(name);
    }

    @Override
    public String toString() {
        return toString(true);
    }
}
