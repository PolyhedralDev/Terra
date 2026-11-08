package com.dfsek.terra.addons.attribute;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;

import com.dfsek.terra.api.world.attribute.DecodeContext;


/**
 * Use only on the thread that loaded it: the {@link DepthTracker} is mutable.
 */
record LazyConfigValue(Object raw, ConfigLoader loader, DepthTracker tracker) {
    static final TypeLoader<LazyConfigValue> LOADER = (type, o, loader, depthTracker) -> new LazyConfigValue(o, loader, depthTracker);

    DecodeContext context(DecodeContext plain) {
        return new ConfigDecodeContext(loader, tracker, plain);
    }
}
