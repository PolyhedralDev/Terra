package com.dfsek.terra.addons.chunkgenerator;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import com.dfsek.terra.addons.chunkgenerator.config.palette.BlockLayerPaletteTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pack.LayerPalettePackConfigTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.predicate.BelowLayerPredicateTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pack.LayerPredicatePackConfigTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.predicate.RangeLayerPredicateTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.predicate.SamplerLayerPredicateTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pack.LayerResolverPackConfigTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.resolve.PaletteLayerResolverTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.resolve.PredicateLayerResolverTemplate;
import com.dfsek.terra.addons.chunkgenerator.generation.LayeredChunkGenerator;
import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.api.LayerResolver;
import com.dfsek.terra.addons.chunkgenerator.util.InstanceWrapper;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.chunk.generation.util.provider.ChunkGeneratorProvider;


public class LayeredChunkGeneratorAddon implements AddonInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(LayeredChunkGenerator.class);
    
    public static final TypeKey<Supplier<ObjectTemplate<LayerPalette>>> LAYER_PALETTE_TYPE_TOKEN = new TypeKey<>() {
    };
    
    public static final TypeKey<InstanceWrapper<LayerPalette>> LAYER_PALETTE_TOKEN = new TypeKey<>() {
    };
    
    public static final TypeKey<Supplier<ObjectTemplate<LayerPredicate>>> LAYER_PREDICATE_TYPE_TOKEN = new TypeKey<>() {
    };
    
    public static final TypeKey<InstanceWrapper<LayerPredicate>> LAYER_PREDICATE_TOKEN = new TypeKey<>() {
    };
    
    public static final TypeKey<Supplier<ObjectTemplate<LayerResolver>>> LAYER_RESOLVER_TYPE_TOKEN = new TypeKey<>() {
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
                .priority(1000)
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<LayerPalette>>> paletteTypeRegistry = event.getPack().getOrCreateRegistry(LAYER_PALETTE_TYPE_TOKEN);
                    paletteTypeRegistry.register(addon.key("BLOCK"), BlockLayerPaletteTemplate::new);
                    CheckedRegistry<InstanceWrapper<LayerPalette>> paletteRegistry = event.getPack().getOrCreateRegistry(LAYER_PALETTE_TOKEN);
                    event.loadTemplate(new LayerPalettePackConfigTemplate()).getPalettes().forEach((key, palette) -> {
                        paletteRegistry.register(addon.key(key), new InstanceWrapper<>(palette));
                    });
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<LayerPredicate>>> predicateTypeRegistry = event.getPack().getOrCreateRegistry(LAYER_PREDICATE_TYPE_TOKEN);
                    predicateTypeRegistry.register(addon.key("BELOW"), BelowLayerPredicateTemplate::new);
                    predicateTypeRegistry.register(addon.key("RANGE"), RangeLayerPredicateTemplate::new);
                    predicateTypeRegistry.register(addon.key("SAMPLER"), SamplerLayerPredicateTemplate::new);
                    CheckedRegistry<InstanceWrapper<LayerPredicate>> predicateRegistry = event.getPack().getOrCreateRegistry(LAYER_PREDICATE_TOKEN);
                    event.loadTemplate(new LayerPredicatePackConfigTemplate()).getPredicates().forEach((key, predicate) -> {
                        predicateRegistry.register(addon.key(key), new InstanceWrapper<>(predicate));
                    });
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<LayerResolver>>> resolverTypeRegistry = event.getPack().getOrCreateRegistry(LAYER_RESOLVER_TYPE_TOKEN);
                    resolverTypeRegistry.register(addon.key("TEST_PREDICATE"), PredicateLayerResolverTemplate::new);
                    resolverTypeRegistry.register(addon.key("USE_PALETTE"), PaletteLayerResolverTemplate::new);
                    LayerResolver resolver = event.loadTemplate(new LayerResolverPackConfigTemplate()).getResolver();

                    event.getPack()
                         .getOrCreateRegistry(ChunkGeneratorProvider.class)
                         .register(addon.key("LAYERED"),
                                   pack -> new LayeredChunkGenerator(platform, resolver));
                })
                .failThrough();
    }
}
