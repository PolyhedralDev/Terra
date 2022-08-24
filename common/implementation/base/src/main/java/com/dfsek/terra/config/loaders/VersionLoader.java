package com.dfsek.terra.config.loaders;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.Version;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.AnnotatedType;


public class VersionLoader implements TypeLoader<Version> {
    @Override
    public Version load(@NonNull AnnotatedType t, @NonNull Object c, @NonNull ConfigLoader loader, DepthTracker depthTracker)
    throws LoadException {
        try {
            return Versions.parseVersion((String) c);
        } catch(ParseException e) {
            throw new LoadException("Failed to parse version", e, depthTracker);
        }
    }
}
