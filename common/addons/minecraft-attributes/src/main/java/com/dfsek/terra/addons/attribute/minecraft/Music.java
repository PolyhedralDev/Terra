package com.dfsek.terra.addons.attribute.minecraft;

public record Music(SoundRef sound, int minDelay, int maxDelay, boolean replaceCurrentMusic) { }
