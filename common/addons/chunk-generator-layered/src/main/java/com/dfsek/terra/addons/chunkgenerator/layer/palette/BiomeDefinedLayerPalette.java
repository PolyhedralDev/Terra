package com.dfsek.terra.addons.chunkgenerator.layer.palette;

import com.dfsek.tectonic.api.config.template.dynamic.DynamicTemplate;
import com.dfsek.tectonic.api.config.template.dynamic.DynamicValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.dfsek.terra.addons.chunkgenerator.LayeredChunkGeneratorAddon;
import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class BiomeDefinedLayerPalette extends LayerPalette {
    
    private final Palette defaultPalette;
    
    public BiomeDefinedLayerPalette(Group group, boolean resetsGroup, Palette defaultPalette) {
        super(group, resetsGroup);
        this.defaultPalette = defaultPalette;
    }
    
    @Override
    public Palette get(long seed, Biome biome, int x, int y, int z) {
        return biome.getContext().get(BiomeLayerPalettes.class).palettes().get(this);
    }
    
    public Optional<Palette> getDefaultPalette() {
        return Optional.ofNullable(defaultPalette);
    }
    
    public static Consumer<ConfigurationLoadEvent> injectLayerPalettes = event -> {
        if(event.is(Biome.class)) {
            
            Map<BiomeDefinedLayerPalette, String> paletteFields = new HashMap<>();
            DynamicTemplate.Builder templateBuilder = DynamicTemplate.builder();
            
            event.getPack().getRegistry(LayeredChunkGeneratorAddon.LAYER_PALETTE_TOKEN).forEach((registryKey, registryEntry) -> {
                LayerPalette layerPalette = registryEntry.get();
                // Add template value for each BiomeDefinedLayerPalette
                if (layerPalette instanceof BiomeDefinedLayerPalette biomeLayerPalette) {
                    String id = registryKey.getID();
                    String fieldName = id + "LayerPalette";
                    paletteFields.put(biomeLayerPalette, fieldName);
                    DynamicValue.Builder<Palette> value = DynamicValue.builder("generation.layers." + id, Palette.class);
                    biomeLayerPalette.getDefaultPalette().ifPresent(value::setDefault);
                    templateBuilder.value(fieldName, value.build());
                }
            });
            
            DynamicTemplate layerPaletteBiomeTemplate = event.load(templateBuilder.build());
            
            Map<BiomeDefinedLayerPalette, Palette> paletteMap = paletteFields.entrySet().stream().collect(
                    Collectors.toMap(Entry::getKey, entry -> layerPaletteBiomeTemplate.get(entry.getValue(), Palette.class)));
            event.getLoadedObject(Biome.class).getContext().put(new BiomeLayerPalettes(paletteMap));
        }
    };
    
    public record BiomeLayerPalettes(Map<BiomeDefinedLayerPalette, Palette> palettes) implements Properties {
    }
}
