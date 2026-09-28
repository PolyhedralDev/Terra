package com.dfsek.terra.bukkit.nms.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.AmbientParticle;

import java.util.stream.Stream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BiomeParticleConfigTemplate implements ObjectTemplate<AmbientParticle> {
    private static final Pattern LEGACY_BLOCK_PARTICLE = Pattern.compile(
        "^((?:[a-z0-9_.-]+:)?[a-z0-9_./-]+)\\{block_state:\\{Name:((?:[a-z0-9_.-]+:)?[a-z0-9_./-]+)\\}\\}$"
    );

    @Value("particle")
    @Default
    private String particle = null;

    @Value("probability")
    @Default
    private Float probability = 0.1f;

    @Override
    public AmbientParticle get() {
        if(particle == null) {
            return null;
        }

        try {
            return new AmbientParticle(parseParticle(particle), probability);
        } catch(CommandSyntaxException e) {
            String normalized = normalizeLegacyBlockParticle(particle);
            if(normalized == null) {
                throw new RuntimeException(e);
            }

            try {
                return new AmbientParticle(parseParticle(normalized), probability);
            } catch(CommandSyntaxException fallbackException) {
                fallbackException.addSuppressed(e);
                throw new RuntimeException(fallbackException);
            }
        }
    }

    private static net.minecraft.core.particles.ParticleOptions parseParticle(String value) throws CommandSyntaxException {
        return ParticleArgument.readParticle(new StringReader(value),
            HolderLookup.Provider.create(Stream.of(BuiltInRegistries.PARTICLE_TYPE)));
    }

    private static String normalizeLegacyBlockParticle(String value) {
        Matcher matcher = LEGACY_BLOCK_PARTICLE.matcher(value);
        if(!matcher.matches()) {
            return null;
        }

        String block = matcher.group(2);
        if(!block.contains(":")) {
            block = "minecraft:" + block;
        }
        return matcher.group(1) + "{block_state:\"" + block + "\"}";
    }
}
