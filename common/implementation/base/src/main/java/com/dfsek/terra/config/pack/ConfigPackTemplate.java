/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.config.pack;

import ca.solostudios.strata.version.Version;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.Collections;
import java.util.List;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class ConfigPackTemplate implements ConfigTemplate {
    @Value("id")
    private String id;
    
    @Value("author")
    @Default
    private String author = "Anon Y. Mous";
    
    @Value("stages")
    @Default
    private @Meta List<@Meta GenerationStage> stages = Collections.emptyList();
    
    @Value("version")
    private Version version;
    
    @Value("generator")
    private @Meta ChunkGeneratorProvider generatorProvider;
    
    @Value("cache.biome.enable")
    @Default
    private boolean biomeCache = false;
    
    public ChunkGeneratorProvider getGeneratorProvider() {
        return generatorProvider;
    }
    
    public List<GenerationStage> getStages() {
        return stages;
    }
    
    public Version getVersion() {
        return version;
    }
    
    public String getID() {
        return id;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public boolean getBiomeCache() {
        return biomeCache;
    }
}
