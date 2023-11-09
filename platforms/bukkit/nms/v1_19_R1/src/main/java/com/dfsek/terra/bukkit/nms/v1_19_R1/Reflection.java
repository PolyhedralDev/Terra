package com.dfsek.terra.bukkit.nms.v1_19_R1;

import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import xyz.jpenilla.reflectionremapper.ReflectionRemapper;
import xyz.jpenilla.reflectionremapper.proxy.ReflectionProxyFactory;
import xyz.jpenilla.reflectionremapper.proxy.annotation.FieldGetter;
import xyz.jpenilla.reflectionremapper.proxy.annotation.FieldSetter;
import xyz.jpenilla.reflectionremapper.proxy.annotation.Proxies;


public class Reflection {
    public static final MappedRegistryProxy MAPPED_REGISTRY;
    public static final StructureManagerProxy STRUCTURE_MANAGER;

    static {
        ReflectionRemapper reflectionRemapper = ReflectionRemapper.forReobfMappingsInPaperJar();
        ReflectionProxyFactory reflectionProxyFactory = ReflectionProxyFactory.create(reflectionRemapper,
            Reflection.class.getClassLoader());

        MAPPED_REGISTRY = reflectionProxyFactory.reflectionProxy(MappedRegistryProxy.class);
        STRUCTURE_MANAGER = reflectionProxyFactory.reflectionProxy(StructureManagerProxy.class);
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
}
