/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript;

import com.dfsek.tectonic.exception.LoadException;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.StructureScript;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.StringUtil;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;


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
                                                                                      event.getPack().getOrCreateRegistry(
                                                                                              (Type) FunctionBuilder.class));
                                structureRegistry.register(structureScript.getID(), structureScript);
                            } catch(ParseException e) {
                                throw new LoadException("Failed to load script \"" + entry.getKey() + "\"", e);
                            }
                        }
                    }).close();
                })
                .priority(2)
                .failThrough();
    }
}
