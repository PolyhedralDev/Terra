package com.dfsek.terra.bukkit.util;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.wyck.environment.particle.AmbientParticle;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;


public final class VanillaParticles {
    private static final Logger LOGGER = LoggerFactory.getLogger(VanillaParticles.class);
    private static final SimpleCommandExceptionType TRAILING_DATA = new SimpleCommandExceptionType(
        new LiteralMessage("Unexpected text after the particle"));

    private VanillaParticles() {
    }

    public static @Nullable AmbientParticle parse(String particle, float probability) throws CommandSyntaxException {
        Handles handles = Holder.HANDLES;
        if(handles == null) return null;

        StringReader reader = new StringReader(particle.strip());
        Object options;
        Object ambient;
        try {
            options = handles.readParticle.invokeExact(reader, handles.registries);
            ambient = handles.ambientParticle.invokeExact(options, probability);
        } catch(CommandSyntaxException | RuntimeException | Error e) {
            throw e;
        } catch(Throwable e) {
            throw new IllegalStateException(e);
        }
        if(reader.canRead()) throw TRAILING_DATA.createWithContext(reader);
        return AmbientParticle.decode(ambient);
    }

    private static @Nullable Handles resolve() {
        String step = "net.minecraft.server.MinecraftServer";
        try {
            ClassLoader loader = Bukkit.getServer().getClass().getClassLoader();
            MethodHandles.Lookup lookup = MethodHandles.publicLookup();

            Class<?> server = Class.forName(step, false, loader);
            step = "MinecraftServer.getServer()";
            Object instance = lookup.findStatic(server, "getServer", MethodType.methodType(server)).invoke();
            if(instance == null) throw new IllegalStateException("the server has not started");
            step = "MinecraftServer.registryAccess()";
            Object registries = lookup.findVirtual(server, "registryAccess",
                MethodType.methodType(Class.forName("net.minecraft.core.RegistryAccess$Frozen", false, loader))).invoke(instance);

            step = "ParticleArgument.readParticle(StringReader, HolderLookup.Provider)";
            Class<?> provider = Class.forName("net.minecraft.core.HolderLookup$Provider", false, loader);
            Class<?> options = Class.forName("net.minecraft.core.particles.ParticleOptions", false, loader);
            MethodHandle readParticle = lookup.findStatic(Class.forName("net.minecraft.commands.arguments.ParticleArgument", false, loader),
                    "readParticle", MethodType.methodType(options, StringReader.class, provider))
                .asType(MethodType.methodType(Object.class, StringReader.class, Object.class));

            step = "AmbientParticle(ParticleOptions, float)";
            MethodHandle ambientParticle = lookup.findConstructor(Class.forName("net.minecraft.world.attribute.AmbientParticle", false, loader),
                    MethodType.methodType(void.class, options, float.class))
                .asType(MethodType.methodType(Object.class, Object.class, float.class));

            return new Handles(readParticle, registries, ambientParticle);
        } catch(Throwable e) {
            LOGGER.warn("Biome particles are disabled: could not find vanilla's {} ({}). " +
                        "This Minecraft version is probably not supported yet.", step, e.toString());
            return null;
        }
    }

    private record Handles(MethodHandle readParticle, Object registries, MethodHandle ambientParticle) {
    }


    private static final class Holder {
        private static final @Nullable Handles HANDLES = resolve();
    }
}
