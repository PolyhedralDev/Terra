package com.dfsek.terra.config.preprocessor;

import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.preprocessor.Result;
import com.dfsek.tectonic.util.ReflectionUtil;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.reflection.TypeKey;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.Map;

public class MetaNumberPreprocessor extends MetaPreprocessor<Meta> {
    public static final TypeKey<String> META_STRING_KEY = new TypeKey<@Meta String>() {};

    public MetaNumberPreprocessor(Map<String, Configuration> configs) {
        super(configs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, Meta annotation) {
        if(t.getType() instanceof Class && isNumber((Class<?>) t.getType()) && c instanceof String) {
            String expression = (String) loader.loadType(META_STRING_KEY.getAnnotatedType(), c);
            try {
                return (Result<T>) Result.overwrite(new Parser().parse(expression).evaluate());
            } catch(ParseException e) {
                throw new LoadException("Invalid expression: ", e);
            }
        }
        return Result.noOp();
    }

    private static boolean isNumber(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz)
                || byte.class.equals(clazz)
                || int.class.equals(clazz)
                || long.class.equals(clazz)
                || float.class.equals(clazz)
                || double.class.equals(clazz);
    }
}
