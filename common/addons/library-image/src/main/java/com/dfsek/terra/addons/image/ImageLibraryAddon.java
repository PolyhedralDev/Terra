package com.dfsek.terra.addons.image;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.function.Supplier;

import com.dfsek.terra.addons.image.config.image.DistanceTransformNoiseSamplerTemplate;
import com.dfsek.terra.addons.image.config.image.ImageTemplate;
import com.dfsek.terra.addons.image.config.image.StitchedImageTemplate;
import com.dfsek.terra.addons.image.config.colorsampler.ConstantColorSamplerTemplate;
import com.dfsek.terra.addons.image.config.colorsampler.image.SingleImageColorSamplerTemplate;
import com.dfsek.terra.addons.image.config.colorsampler.image.TileImageColorSamplerTemplate;
import com.dfsek.terra.addons.image.config.colorsampler.mutate.RotateColorSamplerTemplate;
import com.dfsek.terra.addons.image.config.colorsampler.mutate.TranslateColorSamplerTemplate;
import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.operator.DistanceTransform;
import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class ImageLibraryAddon implements AddonInitializer {
    
    public static final TypeKey<Supplier<ObjectTemplate<Image>>> IMAGE_REGISTRY_KEY = new TypeKey<>() {
    };
    
    public static final TypeKey<Supplier<ObjectTemplate<ColorSampler>>> COLOR_PICKER_REGISTRY_KEY = new TypeKey<>() {
    };
    
    public static final TypeKey<Supplier<ObjectTemplate<NoiseSampler>>> NOISE_SAMPLER_TOKEN = new TypeKey<>() {
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
                .priority(10)
                .then(event -> {
                    ConfigPack pack = event.getPack();
                    CheckedRegistry<Supplier<ObjectTemplate<Image>>> imageRegistry = pack.getOrCreateRegistry(IMAGE_REGISTRY_KEY);
                    imageRegistry.register(addon.key("BITMAP"), () -> new ImageTemplate(pack.getLoader(), pack));
                    imageRegistry.register(addon.key("STITCHED_BITMAP"), () -> new StitchedImageTemplate(pack.getLoader(), pack));
//                    imageRegistry.register(addon.key("DISTANCE_TRANSFORM"), DistanceTransformTemplate::new);
                })
                .then(event -> {
                    event.getPack()
                         .applyLoader(DistanceTransform.CostFunction.class,
                                      (type, o, loader, depthTracker) -> DistanceTransform.CostFunction.valueOf((String) o))
                         .applyLoader(DistanceTransform.Normalization.class,
                                      (type, o, loader, depthTracker) -> DistanceTransform.Normalization.valueOf((String) o));
                    
                    CheckedRegistry<Supplier<ObjectTemplate<NoiseSampler>>> noiseRegistry = event.getPack().getOrCreateRegistry(
                            NOISE_SAMPLER_TOKEN);
                    noiseRegistry.register(addon.key("DISTANCE_TRANSFORM"), DistanceTransformNoiseSamplerTemplate::new);
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<ColorSampler>>> colorSamplerRegistry = event.getPack().getOrCreateRegistry(
                            COLOR_PICKER_REGISTRY_KEY);
                    colorSamplerRegistry.register(addon.key("SINGLE_IMAGE"), SingleImageColorSamplerTemplate::new);
                    colorSamplerRegistry.register(addon.key("TILED_IMAGE"), TileImageColorSamplerTemplate::new);
                    colorSamplerRegistry.register(addon.key("COLOR"), ConstantColorSamplerTemplate::new);
                    colorSamplerRegistry.register(addon.key("ROTATE"), RotateColorSamplerTemplate::new);
                    colorSamplerRegistry.register(addon.key("TRANSLATE"), TranslateColorSamplerTemplate::new);
        });
    }
}
