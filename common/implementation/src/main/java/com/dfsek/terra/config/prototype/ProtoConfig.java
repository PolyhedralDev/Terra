package com.dfsek.terra.config.prototype;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;

public class ProtoConfig implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("type")
    private ConfigType<?, ?> type;


    public String getId() {
        return id;
    }

    public ConfigType<?, ?> getType() {
        return type;
    }
}
