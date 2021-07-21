package com.dfsek.terra.config.preprocessor;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.preprocessor.Result;
import com.dfsek.tectonic.preprocessor.ValuePreprocessor;
import com.dfsek.terra.api.config.meta.Meta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

public class MetaValuePreprocessor implements ValuePreprocessor<Meta> {
    private final Map<String, Configuration> configs;

    public MetaValuePreprocessor(Map<String, Configuration> configs) {
        this.configs = configs;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader configLoader, Meta annotation) {
        if(c instanceof String) {
            String value = ((String) c).trim();
            if(value.startsWith("$")) { // it's a meta value.
                String raw = value.substring(1);
                int sep = raw.indexOf(':');
                String file = raw.substring(0, sep);
                String key = raw.substring(sep + 1);

                if(!configs.containsKey(file)) throw new LoadException("Cannot fetch metavalue: No such config: " + file);

                Configuration config = configs.get(file);

                if(!config.contains(key))
                    throw new LoadException("Cannot fetch metavalue: No such key " + key + " in configuration " + config.getName());

                return (Result<T>) Result.overwrite(config.get(key));
            }
        }
        return Result.noOp();
    }
}
