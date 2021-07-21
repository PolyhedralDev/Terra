package com.dfsek.terra.config.preprocessor;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.preprocessor.Result;
import com.dfsek.terra.api.config.meta.Meta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MetaListLikePreprocessor extends MetaPreprocessor<Meta> {
    public MetaListLikePreprocessor(Map<String, Configuration> configs) {
        super(configs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, Meta annotation) {
        if(t.getType() instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) t.getType();
            if(parameterizedType.getRawType() instanceof Class) { // Should always be true but we check anyways
                Class<?> baseClass = (Class<?>) parameterizedType.getRawType();

                if((List.class.isAssignableFrom(baseClass) || Set.class.isAssignableFrom(baseClass)) && c instanceof List) { // List or set metaconfig
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
                            throw new LoadException("MetaList/Set injection candidate must be list, is type " + metaValue.getClass().getCanonicalName());
                        }

                        List<Object> metaList = (List<Object>) metaValue;

                        newList.remove(i + offset); // Remove placeholder
                        newList.addAll(i + offset, metaList); // Add metalist values where placeholder was
                        offset += metaList.size() - 1; // add metalist size to offset, subtract one to account for placeholder.
                    }

                    return (Result<T>) Result.overwrite(newList);
                }
            }
        }

        return Result.noOp();
    }
}
