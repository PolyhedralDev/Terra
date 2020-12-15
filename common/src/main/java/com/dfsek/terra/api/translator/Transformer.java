package com.dfsek.terra.api.translator;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to translate types from one style/platform to another.
 *
 * @param <F> Data type to transform from.
 * @param <T> Data type to transform to.
 */
public class Transformer<F, T> {
    private final List<Transform<F, T>> transformer;

    private Transformer(List<Transform<F, T>> transformer) {
        this.transformer = transformer;
    }

    /**
     * Translate data from {@code from} type to {@code to} type.
     *
     * @param from Data to translate
     * @return Result
     */
    public T translate(F from) {
        List<Exception> exceptions = new ArrayList<>();
        for(Transform<F, T> transform : transformer) {
            try {
                return transform.transform(from);
            } catch(Exception exception) {
                exceptions.add(exception);
            }
        }
        StringBuilder exBuilder = new StringBuilder("Could not transform input; all attempts failed: ").append(from.toString()).append("\n");
        for(Exception exception : exceptions) exBuilder.append(exception.getMessage()).append("\n");
        throw new AttemptsFailedException(exBuilder.toString());
    }

    /**
     * Builder pattern for building Transformers
     *
     * @param <T>
     * @param <F>
     */
    public static class Builder<T, F> {
        private final List<Transform<T, F>> transforms = new ArrayList<>();

        public Builder<T, F> addTransform(Transform<T, F> transform) {
            transforms.add(transform);
            return this;
        }

        public Transformer<T, F> build() {
            return new Transformer<>(transforms);
        }
    }
}
