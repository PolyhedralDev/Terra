package com.dfsek.terra.addons.terrascript;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.script.StructureScript;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.terra.api.structure.Structure;

import java.io.InputStream;
import java.util.Map;

@Addon("structure-terrascript-loader")
@Author("Terra")
@Version("1.0.0")
public class TerraScriptAddon extends TerraAddon {
    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigPackPreLoadEvent.class)
                .then(event -> {
                    CheckedRegistry<Structure> structureRegistry = event.getPack().getOrCreateRegistry(Structure.class);
                    CheckedRegistry<LootTable> lootRegistry = event.getPack().getOrCreateRegistry(LootTable.class);
                    event.getPack().getLoader().open("", ".tesf").thenEntries(entries -> {
                        for(Map.Entry<String, InputStream> entry : entries) {
                            try {
                                StructureScript structureScript = new StructureScript(entry.getValue(), main, structureRegistry, lootRegistry, event.getPack().getRegistryFactory().create());
                                structureRegistry.register(structureScript.getID(), structureScript);
                            } catch(ParseException e) {
                                throw new LoadException("Failed to load script: ", e);
                            }
                        }
                    }).close();
                })
                .failThrough();
    }
}
