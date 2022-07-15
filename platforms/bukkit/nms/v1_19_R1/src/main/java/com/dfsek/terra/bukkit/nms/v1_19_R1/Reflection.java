package com.dfsek.terra.bukkit.nms.v1_19_R1;

import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.Biome;
import xyz.jpenilla.reflectionremapper.ReflectionRemapper;
import xyz.jpenilla.reflectionremapper.proxy.ReflectionProxyFactory;
import xyz.jpenilla.reflectionremapper.proxy.annotation.FieldGetter;
import xyz.jpenilla.reflectionremapper.proxy.annotation.FieldSetter;
import xyz.jpenilla.reflectionremapper.proxy.annotation.Proxies;
import xyz.jpenilla.reflectionremapper.proxy.annotation.Static;

import java.util.Map;


public class Reflection {
    public static final MappedRegistryProxy MAPPED_REGISTRY;
    
    public static final VillagerTypeProxy VILLAGER_TYPE;
    
    static {
        ReflectionRemapper reflectionRemapper = ReflectionRemapper.forReobfMappingsInPaperJar();
        ReflectionProxyFactory reflectionProxyFactory = ReflectionProxyFactory.create(reflectionRemapper,
                                                                                      Reflection.class.getClassLoader());
        
        MAPPED_REGISTRY = reflectionProxyFactory.reflectionProxy(MappedRegistryProxy.class);
        VILLAGER_TYPE = reflectionProxyFactory.reflectionProxy(VillagerTypeProxy.class);
    }
    
    
    @Proxies(MappedRegistry.class)
    public interface MappedRegistryProxy {
        @FieldSetter("frozen")
        void setFrozen(MappedRegistry<?> instance, boolean frozen);
    }
    
    
    @Proxies(VillagerTypeProxy.class)
    public interface VillagerTypeProxy {
        @Static
        @FieldGetter("BY_BIOME")
        Map<ResourceKey<Biome>, VillagerType> getByBiome();
    }
}
