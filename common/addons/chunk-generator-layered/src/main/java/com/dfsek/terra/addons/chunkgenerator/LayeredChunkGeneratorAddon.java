package com.dfsek.terra.addons.chunkgenerator;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.config.sampler.ElevationLayerSamplerTemplate;
import com.dfsek.terra.addons.chunkgenerator.layer.sampler.ElevationLayerSampler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.api.LayerResolver;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.config.pack.LayerPalettePackConfigTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pack.LayerPredicatePackConfigTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pack.LayerResolverPackConfigTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pack.LayerSamplerPackConfigTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.palette.BiomeDefinedLayerPaletteTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.palette.DotProductLayerPaletteTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.palette.PlatformAirLayerPaletteTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.palette.SimpleLayerPaletteTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pointset.generative.AdjacentPointSetTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pointset.generative.SimplePointSetTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pointset.generative.geometric.CubePointSetTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pointset.generative.geometric.CuboidPointSetTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pointset.generative.geometric.SphericalPointSetTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pointset.operative.DifferencePointSetTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pointset.operative.ExpressionFilterPointSetTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pointset.operative.IntersectionPointSetTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.pointset.operative.UnionPointSetTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.predicate.BelowLayerPredicateTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.predicate.RangeLayerPredicateTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.predicate.SamplerLayerPredicateTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.predicate.SamplerListLayerPredicateTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.resolve.PaletteLayerResolverTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.resolve.PredicateLayerResolverTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.sampler.BiomeDefinedLayerSamplerTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.sampler.DensityLayerSamplerTemplate;
import com.dfsek.terra.addons.chunkgenerator.generation.LayeredChunkGenerator;
import com.dfsek.terra.addons.chunkgenerator.layer.palette.BiomeDefinedLayerPalette;
import com.dfsek.terra.addons.chunkgenerator.layer.sampler.BiomeDefinedLayerSampler;
import com.dfsek.terra.addons.chunkgenerator.math.RelationalOperator;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.addons.chunkgenerator.util.InstanceWrapper;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.chunk.generation.util.provider.ChunkGeneratorProvider;


public class LayeredChunkGeneratorAddon implements AddonInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger( LayeredChunkGeneratorAddon.class);
    
    public static final TypeKey<Supplier<ObjectTemplate<PointSet>>> POINT_SET_TYPE_TOKEN = new TypeKey<>() {
    };
    
    public static final TypeKey<Supplier<ObjectTemplate<LayerSampler>>> LAYER_SAMPLER_TYPE_TOKEN = new TypeKey<>() {
    };
    
    public static final TypeKey<InstanceWrapper<LayerSampler>> LAYER_SAMPLER_TOKEN = new TypeKey<>() {
    };
    
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
                    event.getPack().applyLoader(RelationalOperator.class,
                                                (type, o, loader, depthTracker) -> RelationalOperator.valueOf((String) o));
    
                    CheckedRegistry<Supplier<ObjectTemplate<PointSet>>> pointSetTypeRegistry = event.getPack().getOrCreateRegistry(
                            POINT_SET_TYPE_TOKEN);
                    pointSetTypeRegistry.register(addon.key("LIST"), SimplePointSetTemplate::new);
                    pointSetTypeRegistry.register(addon.key("ADJACENT"), AdjacentPointSetTemplate::new);
                    
                    pointSetTypeRegistry.register(addon.key("SPHERE"), SphericalPointSetTemplate::new);
                    pointSetTypeRegistry.register(addon.key("CUBOID"), CuboidPointSetTemplate::new);
                    pointSetTypeRegistry.register(addon.key("CUBE"), CubePointSetTemplate::new);
                    
                    pointSetTypeRegistry.register(addon.key("UNION"), UnionPointSetTemplate::new);
                    pointSetTypeRegistry.register(addon.key("INTERSECTION"), IntersectionPointSetTemplate::new);
                    pointSetTypeRegistry.register(addon.key("DIFFERENCE"), DifferencePointSetTemplate::new);
                    
                    pointSetTypeRegistry.register(addon.key("EXPRESSION"), ExpressionFilterPointSetTemplate::new);
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<LayerSampler>>> samplerTypeRegistry = event.getPack().getOrCreateRegistry(LAYER_SAMPLER_TYPE_TOKEN);
                    CheckedRegistry<InstanceWrapper<LayerSampler>> samplerRegistry = event.getPack().getOrCreateRegistry(LAYER_SAMPLER_TOKEN);
                    samplerTypeRegistry.register(addon.key("DENSITY"), DensityLayerSamplerTemplate::new);
                    samplerTypeRegistry.register(addon.key("ELEVATION"), ElevationLayerSamplerTemplate::new);
                    samplerTypeRegistry.register(addon.key("BIOME_DEFINED"), BiomeDefinedLayerSamplerTemplate::new);
                    
                    event.loadTemplate(new LayerSamplerPackConfigTemplate()).getSamplers().forEach((key, sampler) -> {
                        samplerRegistry.register(addon.key(key), new InstanceWrapper<>(sampler));
                    });
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<LayerPredicate>>> predicateTypeRegistry = event.getPack().getOrCreateRegistry(LAYER_PREDICATE_TYPE_TOKEN);
                    predicateTypeRegistry.register(addon.key("BELOW"), BelowLayerPredicateTemplate::new);
                    predicateTypeRegistry.register(addon.key("RANGE"), RangeLayerPredicateTemplate::new);
                    predicateTypeRegistry.register(addon.key("SAMPLER"), SamplerLayerPredicateTemplate::new);
                    predicateTypeRegistry.register(addon.key("SAMPLER_POINTS"), SamplerListLayerPredicateTemplate::new);
                    
                    CheckedRegistry<InstanceWrapper<LayerPredicate>> predicateRegistry = event.getPack().getOrCreateRegistry(LAYER_PREDICATE_TOKEN);
                    event.loadTemplate(new LayerPredicatePackConfigTemplate()).getPredicates().forEach((key, predicate) -> {
                        predicateRegistry.register(addon.key(key), new InstanceWrapper<>(predicate));
                    });
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<LayerPalette>>> paletteTypeRegistry = event.getPack().getOrCreateRegistry(LAYER_PALETTE_TYPE_TOKEN);
                    paletteTypeRegistry.register(addon.key("PALETTE"), SimpleLayerPaletteTemplate::new);
                    paletteTypeRegistry.register(addon.key("BIOME_DEFINED"), BiomeDefinedLayerPaletteTemplate::new);
                    paletteTypeRegistry.register(addon.key("AIR"), () -> new PlatformAirLayerPaletteTemplate(platform));
                    paletteTypeRegistry.register(addon.key("SURFACE_NORMAL"), DotProductLayerPaletteTemplate::new);
                    
                    event.getPack().applyLoader(LayerPalette.Group.class, new LayerPalette.Group.Loader(event.getPack()));
                    
                    CheckedRegistry<InstanceWrapper<LayerPalette>> paletteRegistry = event.getPack().getOrCreateRegistry(LAYER_PALETTE_TOKEN);
                    event.loadTemplate(new LayerPalettePackConfigTemplate()).getPalettes().forEach((key, palette) -> {
                        paletteRegistry.register(addon.key(key), new InstanceWrapper<>(palette));
                    });
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<LayerResolver>>> resolverTypeRegistry = event.getPack().getOrCreateRegistry(LAYER_RESOLVER_TYPE_TOKEN);
                    resolverTypeRegistry.register(addon.key("TEST"), PredicateLayerResolverTemplate::new);
                    resolverTypeRegistry.register(addon.key("LAYER"), PaletteLayerResolverTemplate::new);
                    LayerResolver resolver = event.loadTemplate(new LayerResolverPackConfigTemplate()).getResolver();

                    event.getPack()
                         .getOrCreateRegistry(ChunkGeneratorProvider.class)
                         .register(addon.key("LAYERED"),
                                   pack -> new LayeredChunkGenerator(platform, resolver));
                })
                .failThrough();
        
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigurationLoadEvent.class)
                .priority(1000)
                .then(BiomeDefinedLayerPalette.injectLayerPalettes)
                .then(BiomeDefinedLayerSampler.injectLayerSamplers);
    }
}
