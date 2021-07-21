package com.dfsek.terra.addons.yaml;

import com.dfsek.tectonic.yaml.YamlConfiguration;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.events.config.ConfigurationDiscoveryEvent;
import com.dfsek.terra.api.injection.annotations.Inject;

@Addon("language-yaml")
@Version("1.0.0")
@Author("Terra")
public class YamlAddon extends TerraAddon implements EventListener {
    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager().registerListener(this, this);
    }

    public void loadYamlConfigs(ConfigurationDiscoveryEvent event) {
        event.getLoader().open("", ".yml").thenEntries(entries -> entries.forEach(entry -> {
            main.getDebugLogger().info("Discovered config " + entry.getKey());
            event.register(entry.getKey(), new YamlConfiguration(entry.getValue(), entry.getKey()));
        }));
    }
}
