package com.dfsek.terra.config.preprocessor;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.preprocessor.ValuePreprocessor;

import java.lang.annotation.Annotation;
import java.util.Map;


public abstract class MetaPreprocessor<A extends Annotation> implements ValuePreprocessor<A> {
    private final Map<String, Configuration> configs;
    
    public MetaPreprocessor(Map<String, Configuration> configs) {
        this.configs = configs;
    }
    
    protected Object getMetaValue(String meta) {
        int sep = meta.indexOf(':');
        String file = meta.substring(0, sep);
        String key = meta.substring(sep + 1);
        
        if(!configs.containsKey(file)) throw new LoadException("Cannot fetch metavalue: No such config: " + file);
        
        Configuration config = configs.get(file);
        
        if(!config.contains(key)) {
            throw new LoadException("Cannot fetch metavalue: No such key " + key + " in configuration " + config.getName());
        }
        
        return config.get(key);
    }
}
