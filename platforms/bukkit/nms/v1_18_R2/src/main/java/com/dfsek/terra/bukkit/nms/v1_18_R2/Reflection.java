package com.dfsek.terra.bukkit.nms.v1_18_R2;

import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.biome.Biome;
import xyz.jpenilla.reflectionremapper.ReflectionRemapper;
import xyz.jpenilla.reflectionremapper.proxy.ReflectionProxyFactory;
import xyz.jpenilla.reflectionremapper.proxy.annotation.FieldGetter;
import xyz.jpenilla.reflectionremapper.proxy.annotation.FieldSetter;
import xyz.jpenilla.reflectionremapper.proxy.annotation.Proxies;


public class Reflection {
    public static final MappedRegistryProxy MAPPED_REGISTRY;
    public static final BiomeProxy BIOME;
    
    static {
        ReflectionRemapper reflectionRemapper = ReflectionRemapper.forReobfMappingsInPaperJar();
        ReflectionProxyFactory reflectionProxyFactory = ReflectionProxyFactory.create(reflectionRemapper,
                                                                                      Reflection.class.getClassLoader());
        
        MAPPED_REGISTRY = reflectionProxyFactory.reflectionProxy(MappedRegistryProxy.class);
        BIOME = reflectionProxyFactory.reflectionProxy(BiomeProxy.class);
    }
    

    @Proxies(MappedRegistry.class)
    public interface MappedRegistryProxy {
        @FieldSetter("frozen")
        void setFrozen(MappedRegistry<?> instance, boolean frozen);
    }
    

    @Proxies(Biome.class)
    public interface BiomeProxy {
        @FieldGetter("biomeCategory")
        Biome.BiomeCategory getBiomeCategory(Biome instance);
    }
}
