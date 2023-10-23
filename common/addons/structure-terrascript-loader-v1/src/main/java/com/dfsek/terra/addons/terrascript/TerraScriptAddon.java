/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript;

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
                    event.getPack().getLoader().open("", ".tesf").thenEntries(
                                 entries ->
                                         entries.stream()
                                                .parallel()
                                                .map(entry -> {
                                                    try {
                                                        String id = StringUtil.fileName(entry.getKey());
                                                        return new StructureScript(entry.getValue(),
                                                                                   addon.key(id),
                                                                                   platform,
                                                                                   structureRegistry,
                                                                                   lootRegistry,
                                                                                   event.getPack().getOrCreateRegistry(FunctionBuilder.class));
                                                    } catch(ParseException e) {
                                                        throw new RuntimeException("Failed to load script \"" + entry.getKey() + "\"", e);
                                                    }
                                                })
                                                .toList()
                                                .forEach(structureRegistry::register))
                         .close();
                })
                .priority(100)
                .failThrough();
    }
}
