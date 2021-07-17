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
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.util.seeded.BiomeBuilder;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;

@Addon("config-flora")
@Author("Terra")
@Version("0.1.0")
public class FloraAddon extends TerraAddon implements EventListener {
    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager().registerListener(this, this);
    }

    public void onPackLoad(ConfigPackPreLoadEvent event) throws DuplicateEntryException {
        event.getPack().registerConfigType(new FloraConfigType(event.getPack()), "FLORA", 2);
        event.getPack().getOrCreateRegistry(GenerationStageProvider.class).register("FLORA", pack -> new FloraPopulator(main));
        event.getPack().applyLoader(FloraLayer.class, FloraLayerLoader::new)
                .applyLoader(BlockLayer.class, BlockLayerTemplate::new);
    }

    public void onBiomeLoad(ConfigurationLoadEvent event) {
        if(BiomeBuilder.class.isAssignableFrom(event.getType().getTypeClass())) {
            event.getLoadedObject(BiomeBuilder.class).getContext().put(event.load(new BiomeFloraTemplate()).get());
        }
    }
}
