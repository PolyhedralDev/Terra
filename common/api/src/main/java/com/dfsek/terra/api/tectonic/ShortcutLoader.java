package com.dfsek.terra.api.tectonic;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.loader.ConfigLoader;


public interface ShortcutLoader<T> {
    T load(ConfigLoader configLoader, String input, DepthTracker tracker);
}
