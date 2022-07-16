package com.dfsek.terra.addons.chunkgenerator.api;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public abstract class LayerPalette {
    
    private final Group group;
    
    private final boolean resetsGroup;
    
    protected LayerPalette(Group group, boolean resetsGroup) {
        this.group = group;
        this.resetsGroup = resetsGroup;
    }
    
    public abstract Palette get(long seed, Biome biome, int x, int y, int z);
    
    public final Group getGroup() {
        return group;
    }
    
    public final boolean resetsGroup() {
        return resetsGroup;
    }
    
    public static class Group {
        
        public static Group NO_GROUP = new Group();
        
        private Group() {}
        
        public static Group get(String string, ConfigPack pack) {
            if (!pack.getContext().has(Holder.class)) {
                pack.getContext().put(new Holder(new HashMap<>()));
            }
            return pack.getContext().get(Holder.class).groups.computeIfAbsent(string, s -> new Group());
        }
    
        private record Holder(Map<String, Group> groups) implements Properties {}
        
        public static class Loader implements TypeLoader<Group> {
    
            private final ConfigPack pack;
            
            public Loader(ConfigPack pack) {
                this.pack = pack;
            }
            
            @Override
            public Group load(@NotNull AnnotatedType annotatedType, @NotNull Object o, @NotNull ConfigLoader configLoader,
                              DepthTracker depthTracker) throws LoadException {
                String groupName = (String) o;
                return Group.get(groupName, pack);
            }
        }
    }
}
