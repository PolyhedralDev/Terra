package com.dfsek.terra.addons.chunkgenerator;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.config.LayeredChunkGeneratorPackConfigTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.palette.BlockLayerPaletteTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.predicate.BelowLayerPredicateTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.predicate.RangeLayerPredicateTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.predicate.SamplerLayerPredicateTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.resolve.PaletteLayerResolverTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.resolve.PredicateLayerResolverTemplate;
import com.dfsek.terra.addons.chunkgenerator.generation.LayeredChunkGenerator;
import com.dfsek.terra.addons.chunkgenerator.layer.palette.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.layer.predicate.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.layer.resolve.LayerResolver;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;

import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.chunk.generation.util.provider.ChunkGeneratorProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;


public class LayeredChunkGeneratorAddon implements AddonInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(LayeredChunkGenerator.class);
    
    public static final TypeKey<Supplier<ObjectTemplate<LayerPalette>>> LAYER_PALETTE_TOKEN = new TypeKey<>() {
    };
    
    public static final TypeKey<Supplier<ObjectTemplate<LayerResolver>>> LAYER_RESOLVER_TOKEN = new TypeKey<>() {
    };
    
    public static final TypeKey<Supplier<ObjectTemplate<LayerPredicate>>> LAYER_PREDICATE_TOKEN = new TypeKey<>() {
    };
    
    @Inject
    private Platform platform;
    
    @Inject
    private BaseAddon addon;
    
    @Override
    public void initialize() {
        
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigPackPreLoadEvent.class)
                .priority(50)
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<LayerPalette>>> paletteRegistry = event.getPack().getOrCreateRegistry(LAYER_PALETTE_TOKEN);
                    paletteRegistry.register(addon.key("BLOCK"), BlockLayerPaletteTemplate::new);
                })
                .then(event -> {
                    logger.info("Registering LayerResolvers..");
                    LayeredChunkGeneratorPackConfigTemplate config = event.loadTemplate(new LayeredChunkGeneratorPackConfigTemplate());
                    CheckedRegistry<Supplier<ObjectTemplate<LayerResolver>>> resolverRegistry = event.getPack().getOrCreateRegistry(LAYER_RESOLVER_TOKEN);
                    resolverRegistry.register(addon.key("PREDICATE"), PredicateLayerResolverTemplate::new);
                    resolverRegistry.register(addon.key("PALETTE"), () -> new PaletteLayerResolverTemplate(config.getPalettes()));
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<LayerPredicate>>> predicateRegistry = event.getPack().getOrCreateRegistry(LAYER_PREDICATE_TOKEN);
                    predicateRegistry.register(addon.key("BELOW"), BelowLayerPredicateTemplate::new);
                    predicateRegistry.register(addon.key("RANGE"), RangeLayerPredicateTemplate::new);
                    predicateRegistry.register(addon.key("SAMPLER"), SamplerLayerPredicateTemplate::new);
                })
                .then(event -> {
                    LayeredChunkGeneratorPackConfigTemplate config = event.loadTemplate(new LayeredChunkGeneratorPackConfigTemplate());
                    event.getPack()
                         .getOrCreateRegistry(ChunkGeneratorProvider.class)
                         .register(addon.key("LAYERED"),
                                   pack -> new LayeredChunkGenerator(platform, config.getResolver()));
                })
                .failThrough();
    }
}
