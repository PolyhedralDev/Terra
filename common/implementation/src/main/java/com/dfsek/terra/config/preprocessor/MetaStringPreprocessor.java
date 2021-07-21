package com.dfsek.terra.config.preprocessor;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.preprocessor.Result;
import com.dfsek.terra.api.config.meta.Meta;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

public class MetaStringPreprocessor extends MetaPreprocessor<Meta> {
    public MetaStringPreprocessor(Map<String, Configuration> configs) {
        super(configs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, Meta annotation) {
        if(String.class.equals(t.getType()) && c instanceof String) { // String is final so we use #equals
            String candidate = (String) c;
            StringSubstitutor substitutor = new StringSubstitutor(key -> {
                Object meta = getMetaValue(key);
                if(!(meta instanceof String) && !(meta instanceof Number) && !(meta instanceof Character) && !(meta instanceof Boolean)) {
                    throw new LoadException("MetaString template injection candidate must be string or primitive, is type " + meta.getClass().getCanonicalName());
                }
                return meta.toString();
            });
            return (Result<T>) Result.overwrite(substitutor.replace(candidate));
        }
        return Result.noOp();
    }


}
