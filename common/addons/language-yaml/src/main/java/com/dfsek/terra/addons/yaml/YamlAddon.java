package com.dfsek.terra.addons.yaml;

import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.ConfigurationDiscoveryEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;


@Addon("language-yaml")
@Version("1.0.0")
@Author("Terra")
public class YamlAddon extends TerraAddon {
    private static final Logger logger = LoggerFactory.getLogger(YamlAddon.class);
    
    @Inject
    private TerraPlugin main;
    
    @Override
    public void initialize() {
        main.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(this, ConfigurationDiscoveryEvent.class)
            .then(event -> event.getLoader().open("", ".yml").thenEntries(entries -> entries.forEach(entry -> {
                logger.info("Discovered config {}", entry.getKey());
                event.register(entry.getKey(), new YamlConfiguration(entry.getValue(), entry.getKey()));
            })))
            .failThrough();
    }
}
