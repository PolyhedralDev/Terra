package com.dfsek.terra.addons.attribute.minecraft;

import java.util.Optional;


public record BackgroundMusic(Optional<Music> defaultMusic, Optional<Music> creative, Optional<Music> underwater) {
    public static final BackgroundMusic EMPTY = new BackgroundMusic(Optional.empty(), Optional.empty(), Optional.empty());
}
