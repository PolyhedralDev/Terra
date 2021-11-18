package com.dfsek.terra.config.loaders;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.VersionRange;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedType;


public class VersionRangeLoader implements TypeLoader<VersionRange> {
    @Override
    public VersionRange load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        try {
            return Versions.parseVersionRange((String) c);
        } catch(ParseException e) {
            throw new LoadException("Failed to parse version range: ", e);
        }
    }
}
