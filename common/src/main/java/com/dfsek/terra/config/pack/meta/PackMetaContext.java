package com.dfsek.terra.config.pack.meta;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.api.config.meta.MetaContext;
import com.dfsek.terra.config.fileloaders.Loader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PackMetaContext implements MetaContext {
    private final Map<String, Configuration> configs = new HashMap<>(); // Lazy init configs
    private final Loader fileAccess;
    private final ConfigLoader loader;

    public PackMetaContext(Loader fileAccess, ConfigLoader loader) {
        this.fileAccess = fileAccess;
        this.loader = loader;
    }

    @Override
    public <T> T load(String meta, Class<T> clazz) throws ConfigException {
        if(meta.indexOf(":") != meta.lastIndexOf(":")) { // We just need to know if there are >1.
            throw new LoadException("Malformed metavalue string: " + meta);
        }

        String file = "/pack.yml";
        String key;
        if(meta.contains(":")) {
            file = meta.substring(0, meta.indexOf(":"));
            key = meta.substring(meta.indexOf(":") + 1);
        } else {
            key = meta;
        }
        Configuration configuration;
        try {
            configuration = new Configuration(fileAccess.get(file), file);
        } catch(IOException e) {
            throw new LoadException("Failed to load config file \"" + file + "\":", e);
        }

        return loader.loadClass(clazz, configuration.get(key));
    }
}
