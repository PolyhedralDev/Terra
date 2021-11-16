package com.dfsek.terra.addons.manifest.impl.config.loaders;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.Version;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedType;


public class VersionLoader implements TypeLoader<Version> {
    @Override
    public Version load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        try {
            return Versions.parseVersion((String) c);
        } catch(ParseException e) {
            throw new LoadException("Failed to parse version", e);
        }
    }
}
