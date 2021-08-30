package com.dfsek.terra.config.preprocessor;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.preprocessor.Result;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

import com.dfsek.terra.api.config.meta.Meta;


public class MetaValuePreprocessor extends MetaPreprocessor<Meta> {
    
    public MetaValuePreprocessor(Map<String, Configuration> configs) {
        super(configs);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader configLoader, Meta annotation) {
        if(c instanceof String) { // Can we do standard metaconfig?
            String value = ((String) c).trim();
            if(value.startsWith("$")) { // it's a meta value.
                return (Result<T>) Result.overwrite(getMetaValue(value.substring(1)));
            }
        }
        return Result.noOp();
    }
}
