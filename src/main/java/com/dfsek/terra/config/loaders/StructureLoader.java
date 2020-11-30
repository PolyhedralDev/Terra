package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.config.files.Loader;
import com.dfsek.terra.structure.Structure;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public class StructureLoader implements TypeLoader<Structure> {
    private final Loader loader;

    public StructureLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
    public Structure load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        try(InputStream stream = loader.get("structures/data/" + o + ".tstructure")) {
            return Structure.fromStream(stream);
        } catch(IOException | ClassNotFoundException e) {
            throw new LoadException("Unable to load structure", e);
        }
    }
}
