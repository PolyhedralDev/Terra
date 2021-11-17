package com.dfsek.terra.addons.terrascript;

import com.dfsek.tectonic.exception.LoadException;

import java.io.InputStream;
import java.util.Map;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.script.StructureScript;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.StringUtil;



public class TerraScriptAddon implements AddonInitializer {
    @Inject
    private Platform platform;
    
    @Inject
    private BaseAddon addon;
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigPackPreLoadEvent.class)
                .then(event -> {
                    CheckedRegistry<Structure> structureRegistry = event.getPack().getOrCreateRegistry(Structure.class);
                    CheckedRegistry<LootTable> lootRegistry = event.getPack().getOrCreateRegistry(LootTable.class);
                    event.getPack().getLoader().open("", ".tesf").thenEntries(entries -> {
                        for(Map.Entry<String, InputStream> entry : entries) {
                            try {
                                String id = StringUtil.fileName(entry.getKey());
                                StructureScript structureScript = new StructureScript(entry.getValue(), id, platform, structureRegistry,
                                                                                      lootRegistry,
                                                                                      event.getPack().getRegistryFactory().create());
                                structureRegistry.register(structureScript.getID(), structureScript);
                            } catch(ParseException e) {
                                throw new LoadException("Failed to load script \"" + entry.getKey() + "\"", e);
                            }
                        }
                    }).close();
                })
                .failThrough();
    }
}
