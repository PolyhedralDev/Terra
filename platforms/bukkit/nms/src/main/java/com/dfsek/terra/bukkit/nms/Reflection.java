package com.dfsek.terra.bukkit.nms;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import xyz.jpenilla.reflectionremapper.ReflectionRemapper;
import xyz.jpenilla.reflectionremapper.proxy.ReflectionProxyFactory;
import xyz.jpenilla.reflectionremapper.proxy.annotation.FieldGetter;
import xyz.jpenilla.reflectionremapper.proxy.annotation.FieldSetter;
import xyz.jpenilla.reflectionremapper.proxy.annotation.Proxies;


public class Reflection {
    public static final StructureManagerProxy STRUCTURE_MANAGER;

    public static final ChunkMapProxy CHUNKMAP;

    static {
        ReflectionRemapper reflectionRemapper = ReflectionRemapper.forReobfMappingsInPaperJar();
        ReflectionProxyFactory reflectionProxyFactory = ReflectionProxyFactory.create(reflectionRemapper,
            Reflection.class.getClassLoader());

        STRUCTURE_MANAGER = reflectionProxyFactory.reflectionProxy(StructureManagerProxy.class);
        CHUNKMAP = reflectionProxyFactory.reflectionProxy(ChunkMapProxy.class);
    }


    @Proxies(StructureManager.class)
    public interface StructureManagerProxy {
        @FieldGetter("level")
        LevelAccessor getLevel(StructureManager instance);
    }


    @Proxies(ChunkMap.class)
    public interface ChunkMapProxy {
        @FieldGetter("worldGenContext")
        WorldGenContext getWorldGenContext(ChunkMap instance);

        @FieldSetter("worldGenContext")
        void setWorldGenContext(ChunkMap instance, WorldGenContext worldGenContext);
    }
}
