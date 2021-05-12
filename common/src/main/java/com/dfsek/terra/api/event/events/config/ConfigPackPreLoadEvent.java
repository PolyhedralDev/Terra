package com.dfsek.terra.api.event.events.config;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.config.pack.ConfigPack;

import java.util.ArrayList;
import java.util.List;

/**
 * Called before a config pack's registries are filled. At this point, the pack manifest has been loaded, and all registries are empty.
 */
public class ConfigPackPreLoadEvent extends ConfigPackLoadEvent {
    public ConfigPackPreLoadEvent(ConfigPack pack) {
        super(pack);
    }

    private final List<ConfigTemplate> templates = new ArrayList<>();

    /**
     * Add an additional config template to load using the pack manifest.
     *
     * @param template Template to register.
     */
    public void addTemplate(ConfigTemplate template) {
        templates.add(template);
    }

    public List<ConfigTemplate> getTemplates() {
        return new ArrayList<>(templates);
    }
}
