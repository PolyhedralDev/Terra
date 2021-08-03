package com.dfsek.terra.config.preprocessor;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.preprocessor.Result;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.reflection.TypeKey;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaMapPreprocessor extends MetaPreprocessor<Meta> {
    private static final TypeKey<List<String>> STRING_LIST = new TypeKey<>() {
    };

    public MetaMapPreprocessor(Map<String, Configuration> configs) {
        super(configs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, Meta annotation) {
        if(t.getType() instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) t.getType();
            if(parameterizedType.getRawType() instanceof Class) { // Should always be true but we check anyways
                Class<?> baseClass = (Class<?>) parameterizedType.getRawType();

                if(Map.class.isAssignableFrom(baseClass) && c instanceof Map) { // Map metaconfig
                    Map<Object, Object> map = (Map<Object, Object>) c;

                    Map<Object, Object> newMap = new HashMap<>(map);

                    if(map.containsKey("<<")) {
                        newMap.putAll(map);
                        newMap.remove("<<"); // Remove placeholder

                        List<String> keys = (List<String>) loader.loadType(STRING_LIST.getAnnotatedType(), map.get("<<"));
                        keys.forEach(key -> {
                            Object meta = getMetaValue(key);
                            if(!(meta instanceof Map)) {
                                throw new LoadException("MetaMap injection candidate must be list, is type " + meta.getClass().getCanonicalName());
                            }
                            newMap.putAll((Map<?, ?>) meta);
                        });
                        return (Result<T>) Result.overwrite(newMap);
                    }


                }
            }
        }

        return Result.noOp();
    }
}
