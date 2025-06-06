package com.dfsek.terra.addons.chunkgenerator.layer.sampler;

import com.dfsek.tectonic.api.config.template.dynamic.DynamicTemplate;
import com.dfsek.tectonic.api.config.template.dynamic.DynamicValue;

import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.dfsek.terra.addons.chunkgenerator.LayeredChunkGeneratorAddon;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class BiomeDefinedLayerSampler implements LayerSampler {
    
    private final Sampler defaultSampler;
    
    public BiomeDefinedLayerSampler(@Nullable Sampler defaultSampler) {
        this.defaultSampler = defaultSampler;
    }
    
    @Override
    public double sample(int x, int y, int z, WorldProperties world, BiomeProvider biomeProvider) {
        return biomeProvider.getBiome(x, y, z, world.getSeed())
                            .getContext()
                            .get(BiomeLayerSamplers.class)
                            .samplers()
                            .get(this)
                            .getSample(world.getSeed(), x, y, z);
    }
    
    private Optional<Sampler> getDefaultSampler() {
        return Optional.ofNullable(defaultSampler);
    }
    
    public static Consumer<ConfigurationLoadEvent> injectLayerSamplers = event -> {
        if(event.is(Biome.class)) {
            
            Map<BiomeDefinedLayerSampler, String> samplerFields = new HashMap<>();
            DynamicTemplate.Builder templateBuilder = DynamicTemplate.builder();
            
            event.getPack().getRegistry(LayeredChunkGeneratorAddon.LAYER_SAMPLER_TOKEN).forEach((registryKey, registryEntry) -> {
                LayerSampler layerSampler = registryEntry.get();

                if (layerSampler instanceof BiomeDefinedLayerSampler biomeLayerSampler) {
                    String id = registryKey.getID();
                    String fieldName = id + "LayerSampler";
                    samplerFields.put(biomeLayerSampler, fieldName);
                    DynamicValue.Builder<Sampler> value = DynamicValue.builder("generation.samplers." + id, Sampler.class);
                    biomeLayerSampler.getDefaultSampler().ifPresent(value::setDefault);
                    templateBuilder.value(fieldName, value.build());
                }
            });
            
            DynamicTemplate layerSamplerBiomeTemplate = event.load(templateBuilder.build());
            
            Map<BiomeDefinedLayerSampler, Sampler> samplerMap = samplerFields.entrySet().stream().collect(
                    Collectors.toMap(Entry::getKey, entry -> layerSamplerBiomeTemplate.get(entry.getValue(), Sampler.class)));
            event.getLoadedObject(Biome.class).getContext().put(new BiomeLayerSamplers(samplerMap));
        }
    };
    
    public record BiomeLayerSamplers(Map<BiomeDefinedLayerSampler, Sampler> samplers) implements Properties {
    }
}
