package com.dfsek.terra.config.preprocessor;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.preprocessor.Result;
import com.dfsek.tectonic.preprocessor.ValuePreprocessor;
import com.dfsek.terra.api.config.meta.Meta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetaValuePreprocessor implements ValuePreprocessor<Meta> {
    private final Map<String, Configuration> configs;

    public MetaValuePreprocessor(Map<String, Configuration> configs) {
        this.configs = configs;
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

        if(t.getType() instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) t.getType();
            if(parameterizedType.getRawType() instanceof Class) { // Should always be true but we check anyways
                Class<?> baseClass = (Class<?>) parameterizedType.getRawType();

                if(List.class.isAssignableFrom(baseClass) && c instanceof List) { // List metaconfig
                    List<Object> list = (List<Object>) c;

                    int offset = 0;
                    List<Object> newList = new ArrayList<>((List<Object>) c);

                    for(int i = 0; i < list.size(); i++) {
                        Object o = list.get(i);
                        if(!(o instanceof String)) continue;
                        String s = ((String) o).trim();
                        if(!s.startsWith("<< ")) continue;
                        String meta = s.substring(3);

                        Object metaValue = getMetaValue(meta);

                        if(!(metaValue instanceof List)) {
                            throw new LoadException("MetaList injection candidate must be list, is type " + metaValue.getClass().getCanonicalName());
                        }

                        List<Object> metaList = (List<Object>) metaValue;

                        newList.addAll(i + offset, metaList);
                        offset += metaList.size();
                    }

                    return (Result<T>) Result.overwrite(newList);
                }
            }
        }

        return Result.noOp();
    }

    private Object getMetaValue(String meta) {
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
