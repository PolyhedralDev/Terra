package com.dfsek.terra.addons.tree;

import com.dfsek.terra.addons.tree.tree.TreeLayer;
import com.dfsek.terra.addons.tree.tree.TreeLayerTemplate;
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
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Addon("config-tree")
@Author("Terra")
@Version("1.0.0")
public class TreeAddon extends TerraAddon implements EventListener {
    @Inject
    private TerraPlugin main;

    private final Map<String, List<TreeLayer>> trees = new HashMap<>();

    @Override
    public void initialize() {
        main.getEventManager().registerListener(this, this);
    }

    public void onPackLoad(ConfigPackPreLoadEvent event) throws DuplicateEntryException {
        event.getPack().registerConfigType(new TreeConfigType(event.getPack()), "TREE", 2);
        event.getPack().getOrCreateRegistry(GenerationStageProvider.class).register("TREE", pack -> new TreePopulator(main, this));
        event.getPack().applyLoader(TreeLayer.class, TreeLayerTemplate::new);
    }

    public void onBiomeLoad(ConfigurationLoadEvent event) {
        if(BiomeBuilder.class.isAssignableFrom(event.getType().getTypeClass())) {
            trees.put(event.getConfiguration().getID(), event.load(new BiomeTreeTemplate()).getTrees());
        }
    }

    public List<TreeLayer> getTrees(TerraBiome biome) {
        return trees.getOrDefault(biome.getID(), Collections.emptyList());
    }
}
