package com.dfsek.terra.addons.image;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import com.dfsek.terra.addons.image.config.BufferedImageLoader;
import com.dfsek.terra.addons.image.config.picker.SingleColorPickerTemplate;
import com.dfsek.terra.addons.image.config.picker.TileColorPickerTemplate;
import com.dfsek.terra.addons.image.picker.ColorPicker;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class ImageLibraryAddon implements AddonInitializer {
    
    public static final TypeKey<Supplier<ObjectTemplate<ColorPicker>>> COLOR_PICKER_REGISTRY_KEY = new TypeKey<>() {
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
                    event.getPack().applyLoader(BufferedImage.class, new BufferedImageLoader(event.getPack().getLoader()));
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<ColorPicker>>> colorPickerRegistry = event.getPack().getOrCreateRegistry(
                            COLOR_PICKER_REGISTRY_KEY);
                    colorPickerRegistry.register(addon.key("SINGLE"), SingleColorPickerTemplate::new);
                    colorPickerRegistry.register(addon.key("TILED"), TileColorPickerTemplate::new);
        });
    }
}
