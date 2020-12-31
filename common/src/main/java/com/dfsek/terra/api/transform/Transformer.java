package com.dfsek.terra.api.transform;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to translate types from one style/platform to another.
 *
 * @param <F> Data type to transform from.
 * @param <T> Data type to transform to.
 */
public class Transformer<F, T> {
    private final LinkedHashMap<Transform<F, T>, List<Validator<T>>> transformers;

    private Transformer(LinkedHashMap<Transform<F, T>, List<Validator<T>>> transformer) {
        this.transformers = transformer;
    }

    /**
     * Translate data from {@code from} type to {@code to} type.
     *
     * @param from Data to translate
     * @return Result
     */
    public T translate(F from) {
        List<Exception> exceptions = new ArrayList<>();
        for(Map.Entry<Transform<F, T>, List<Validator<T>>> transform : transformers.entrySet()) {
            try {
                T result = transform.getKey().transform(from);
                for(Validator<T> validator : transform.getValue()) {
                    if(!validator.validate(result)) {
                        throw new TransformException("Failed to validate result: " + result.toString());
                    }
                }
                return result;
            } catch(Exception exception) {
                exceptions.add(exception);
            }
        }
        StringBuilder exBuilder = new StringBuilder("Could not transform input; all attempts failed: ").append(from.toString()).append("\n");
        for(Exception exception : exceptions) {
            StringWriter writer = new StringWriter();
            exception.printStackTrace(new PrintWriter(writer));
            exBuilder.append("\n").append(writer.toString());
        }
        throw new AttemptsFailedException(exBuilder.toString());
    }

    /**
     * Builder pattern for building Transformers
     *
     * @param <T>
     * @param <F>
     */
    public static final class Builder<F, T> {
        private final LinkedHashMap<Transform<F, T>, List<Validator<T>>> transforms = new LinkedHashMap<>();

        @SafeVarargs
        public final Builder<F, T> addTransform(Transform<F, T> transform, Validator<T>... validators) {
            transforms.put(transform, Arrays.asList(validators));
            return this;
        }

        public final Transformer<F, T> build() {
            return new Transformer<>(transforms);
        }
    }
}
