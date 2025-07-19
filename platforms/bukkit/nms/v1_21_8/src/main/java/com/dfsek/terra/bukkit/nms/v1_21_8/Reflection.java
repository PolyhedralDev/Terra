package com.dfsek.terra.bukkit.nms.v1_21_8;

import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderSet;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import xyz.jpenilla.reflectionremapper.ReflectionRemapper;
import xyz.jpenilla.reflectionremapper.proxy.ReflectionProxyFactory;
import xyz.jpenilla.reflectionremapper.proxy.annotation.FieldGetter;
import xyz.jpenilla.reflectionremapper.proxy.annotation.FieldSetter;
import xyz.jpenilla.reflectionremapper.proxy.annotation.MethodName;
import xyz.jpenilla.reflectionremapper.proxy.annotation.Proxies;
import xyz.jpenilla.reflectionremapper.proxy.annotation.Static;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class Reflection {
    public static final MappedRegistryProxy MAPPED_REGISTRY;
    public static final MappedRegistryTagSetProxy MAPPED_REGISTRY_TAG_SET;
    public static final StructureManagerProxy STRUCTURE_MANAGER;

    public static final ReferenceProxy REFERENCE;


    public static final ChunkMapProxy CHUNKMAP;
    public static final HolderReferenceProxy HOLDER_REFERENCE;
    public static final HolderSetNamedProxy HOLDER_SET;
    public static final BiomeProxy BIOME;
    public static final VillagerTypeProxy VILLAGER_TYPE;

    static {
        ReflectionRemapper reflectionRemapper = ReflectionRemapper.forReobfMappingsInPaperJar();
        ReflectionProxyFactory reflectionProxyFactory = ReflectionProxyFactory.create(reflectionRemapper,
            Reflection.class.getClassLoader());

        MAPPED_REGISTRY = reflectionProxyFactory.reflectionProxy(MappedRegistryProxy.class);
        MAPPED_REGISTRY_TAG_SET = reflectionProxyFactory.reflectionProxy(MappedRegistryTagSetProxy.class);
        STRUCTURE_MANAGER = reflectionProxyFactory.reflectionProxy(StructureManagerProxy.class);
        REFERENCE = reflectionProxyFactory.reflectionProxy(ReferenceProxy.class);
        CHUNKMAP = reflectionProxyFactory.reflectionProxy(ChunkMapProxy.class);
        HOLDER_REFERENCE = reflectionProxyFactory.reflectionProxy(HolderReferenceProxy.class);
        HOLDER_SET = reflectionProxyFactory.reflectionProxy(HolderSetNamedProxy.class);
        BIOME = reflectionProxyFactory.reflectionProxy(BiomeProxy.class);
        VILLAGER_TYPE = reflectionProxyFactory.reflectionProxy(VillagerTypeProxy.class);
    }


    @Proxies(MappedRegistry.class)
    public interface MappedRegistryProxy {
        @FieldGetter("byKey")
        <T> Map<ResourceKey<T>, Reference<T>> getByKey(MappedRegistry<T> instance);

        @FieldSetter("allTags")
        <T> void setAllTags(MappedRegistry<T> instance, Object obj);

        @FieldSetter("frozen")
        void setFrozen(MappedRegistry<?> instance, boolean frozen);

        @MethodName("createTag")
        <T> HolderSet.Named<T> invokeCreateTag(MappedRegistry<T> instance, TagKey<T> tag);
    }

    @Proxies(className = "net.minecraft.core.MappedRegistry$TagSet")
    public interface MappedRegistryTagSetProxy {
        @MethodName("fromMap")
        @Static
        <T> Object invokeFromMap(Map<TagKey<T>, HolderSet.Named<T>> map);
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

        @MethodName("bindTags")
        <T> void invokeBindTags(Reference<T> instance, Collection<TagKey<T>> tags);
    }

    @Proxies(ChunkMap.class)
    public interface ChunkMapProxy {
        @FieldGetter("worldGenContext")
        WorldGenContext getWorldGenContext(ChunkMap instance);

        @FieldSetter("worldGenContext")
        void setWorldGenContext(ChunkMap instance, WorldGenContext worldGenContext);
    }

    @Proxies(Holder.Reference.class)
    public interface HolderReferenceProxy {
        @MethodName("bindTags")
        <T> void invokeBindTags(Holder.Reference<T> instance, Collection<TagKey<T>> tags);
    }

    @Proxies(HolderSet.Named.class)
    public interface HolderSetNamedProxy {
        @MethodName("bind")
        <T> void invokeBind(HolderSet.Named<T> instance, List<Holder<T>> entries);

        @MethodName("contents")
        <T> List<Holder<T>> invokeContents(HolderSet.Named<T> instance);
    }

    @Proxies(Biome.class)
    public interface BiomeProxy {
        @MethodName("getGrassColorFromTexture")
        int invokeGrassColorFromTexture(Biome instance);
    }

    @Proxies(VillagerType.class)
    public interface VillagerTypeProxy {
        @Static
        @FieldGetter("BY_BIOME")
        Map<ResourceKey<Biome>, ResourceKey<VillagerType>> getByBiome();
    }
}
