package com.dfsek.terra.addons.flora;

import com.dfsek.terra.addons.flora.config.BlockLayerTemplate;
import com.dfsek.terra.addons.flora.flora.FloraLayer;
import com.dfsek.terra.addons.flora.flora.gen.BlockLayer;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.events.config.ConfigLoadEvent;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.util.seeded.BiomeBuilder;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Addon("config-flora")
@Author("Terra")
@Version("0.1.0")
public class FloraAddon extends TerraAddon implements EventListener {
    @Inject
    private TerraPlugin main;

    private final Map<String, List<FloraLayer>> flora = new HashMap<>(); // store Flora layers per biome by biome ID

    @Override
    public void initialize() {
        main.getEventManager().registerListener(this, this);
    }

    public void onPackLoad(ConfigPackPreLoadEvent event) throws DuplicateEntryException {
        event.getPack().registerConfigType(new FloraConfigType(event.getPack()), "FLORA", 2);
        event.getPack().getOrCreateRegistry(GenerationStageProvider.class).register("FLORA", pack -> new FloraPopulator(main, this));
        event.getPack().applyLoader(FloraLayer.class, FloraLayerLoader::new)
                .applyLoader(BlockLayer.class, BlockLayerTemplate::new);
    }

    public void onBiomeLoad(ConfigLoadEvent event) {
        if(BiomeBuilder.class.isAssignableFrom(event.getType().getTypeClass())) {
            flora.put(event.getConfiguration().getID(), event.load(new BiomeFloraTemplate()).getFlora());
        }
    }

    public List<FloraLayer> getFlora(TerraBiome biome) {
        return flora.getOrDefault(biome.getID(), Collections.emptyList());
    }
}
