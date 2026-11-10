package com.dfsek.terra.minestom.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.minestom.server.adventure.MinestomAdventure;
import net.minestom.server.codec.Transcoder;
import net.minestom.server.particle.Particle;
import net.minestom.server.world.attribute.AmbientParticle;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class BiomeParticleConfigTemplate implements ObjectTemplate<AmbientParticle> {
    @Value("particle")
    @Default
    private String particle = null;

    @Value("probability")
    @Default
    private Float probability = null;

    @Override
    public AmbientParticle get() {
        if(particle == null || probability == null) {
            return null;
        }

        String[] parts = particle.split("\\{");
        String key = parts[0];
        Particle parsedParticle = Particle.fromKey(key);
        if(parts.length > 1) {
            int start = particle.indexOf("{");
            String dataString = particle.substring(start);
            parsedParticle = parseAdditionalParticleData(dataString, key);
            if (parsedParticle == null) return null;
        }

        return new AmbientParticle(
            parsedParticle,
            probability
        );
    }

    private Particle parseAdditionalParticleData(String dataString, String key) {
        CompoundBinaryTag nbt = null;
        Particle parsedParticle;
        try {
            nbt = MinestomAdventure.NBT_CODEC.decode(dataString);
            // transform minecraft:x{a:"b"} into {type:"minecraft:x", a:"b"} as described in Particle.CODEC
            nbt = nbt.putString("type", key);

            // TODO fix this
            CompoundBinaryTag blockState = nbt.getCompound("block_state", null);
            if (blockState != null) {
                String name = blockState.getString("Name", null);
                if (name != null) {
                    blockState = blockState.put("id", StringBinaryTag.stringBinaryTag(name));
                    nbt = nbt.put("block_state", blockState);
                }
            }

            parsedParticle = Particle.CODEC.decode(Transcoder.NBT, nbt).orElseThrow();
        } catch(Exception e) {
            String nbtString = "null";
            try {
                nbtString = (nbt == null ? "null" : MinestomAdventure.NBT_CODEC.encode(nbt));
            } catch(IOException _) { }
            LoggerFactory.getLogger(BiomeParticleConfigTemplate.class).warn(
                "Failed to decode particle from '" + particle + "', nbt: '" + nbtString + "'. This particle will be ignored.", e);
            return null;
        }
        return parsedParticle;
    }
}
