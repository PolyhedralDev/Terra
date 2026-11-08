package com.dfsek.terra.addons.attribute.minecraft;

import java.util.List;
import java.util.Optional;


public record AmbientSounds(Optional<SoundRef> loop, Optional<Mood> mood, List<Additions> additions) {
    public static final AmbientSounds EMPTY = new AmbientSounds(Optional.empty(), Optional.empty(), List.of());

    public AmbientSounds {
        additions = List.copyOf(additions);
    }

    public record Mood(SoundRef sound, int tickDelay, int blockSearchExtent, double offset) { }


    public record Additions(SoundRef sound, double tickChance) { }
}
