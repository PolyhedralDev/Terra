package com.dfsek.terra.bukkit.nms.v1_20_R1;

import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import xyz.jpenilla.reflectionremapper.ReflectionRemapper;
import xyz.jpenilla.reflectionremapper.proxy.ReflectionProxyFactory;
import xyz.jpenilla.reflectionremapper.proxy.annotation.FieldGetter;
import xyz.jpenilla.reflectionremapper.proxy.annotation.FieldSetter;
import xyz.jpenilla.reflectionremapper.proxy.annotation.MethodName;
import xyz.jpenilla.reflectionremapper.proxy.annotation.Proxies;


public class Reflection {
    public static final MappedRegistryProxy MAPPED_REGISTRY;
    public static final StructureManagerProxy STRUCTURE_MANAGER;
    
    public static final ReferenceProxy REFERENCE;
    
    static {
        ReflectionRemapper reflectionRemapper = ReflectionRemapper.forReobfMappingsInPaperJar();
        ReflectionProxyFactory reflectionProxyFactory = ReflectionProxyFactory.create(reflectionRemapper,
                                                                                      Reflection.class.getClassLoader());
        
        MAPPED_REGISTRY = reflectionProxyFactory.reflectionProxy(MappedRegistryProxy.class);
        STRUCTURE_MANAGER = reflectionProxyFactory.reflectionProxy(StructureManagerProxy.class);
        REFERENCE = reflectionProxyFactory.reflectionProxy(ReferenceProxy.class);
    }
    
    
    @Proxies(MappedRegistry.class)
    public interface MappedRegistryProxy {
        @FieldSetter("frozen")
        void setFrozen(MappedRegistry<?> instance, boolean frozen);
    }
    
    
    @Proxies(StructureManager.class)
    public interface StructureManagerProxy {
        @FieldGetter("level")
        LevelAccessor getLevel(StructureManager instance);
    }
    
    
    @Proxies(Holder.Reference.class)
    public interface ReferenceProxy {
        @MethodName("bindValue")
        <T> void invokeBindValue(Reference<T> instance, T value);
    }
}
