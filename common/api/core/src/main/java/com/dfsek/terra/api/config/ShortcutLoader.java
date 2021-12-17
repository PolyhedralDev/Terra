package com.dfsek.terra.api.config;

import com.dfsek.tectonic.api.loader.ConfigLoader;


public interface ShortcutLoader<T> {
    T load(ConfigLoader configLoader, String input);
}
