package com.dfsek.terra.addons.biome.pipeline.api.biome;

import java.util.HashSet;
import java.util.Set;

import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.tag.BiomeTagIndex;


final class PlaceholderPipelineBiome extends PipelineBiome {
    private final Set<String> tags;
    private final String id;

    public PlaceholderPipelineBiome(String id, BiomeTagIndex index) {
        this(id, tagsOf(id), index);
    }

    private PlaceholderPipelineBiome(String id, Set<String> tags, BiomeTagIndex index) {
        super(index.register(tags));
        this.id = id;
        this.tags = tags;
    }

    private static Set<String> tagsOf(String id) {
        Set<String> tags = new HashSet<>();
        tags.add(id);
        tags.add("ALL");
        return Set.copyOf(tags);
    }

    @Override
    public Biome getBiome() {
        throw new UnsupportedOperationException("Cannot get raw biome from placeholder pipeline biome");
    }

    @Override
    public Set<String> getTags() {
        return tags;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public boolean isPlaceholder() {
        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PlaceholderPipelineBiome that)) return false;

        return this.id.equals(that.id);
    }
}
