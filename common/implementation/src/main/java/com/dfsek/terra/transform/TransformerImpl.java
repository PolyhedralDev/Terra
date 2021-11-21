/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.transform.Transform;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.api.transform.Validator;
import com.dfsek.terra.api.transform.exception.AttemptsFailedException;
import com.dfsek.terra.api.transform.exception.TransformException;


/**
 * Class to translate types from one style/platform to another.
 *
 * @param <F> Data type to transform from.
 * @param <T> Data type to transform to.
 */
public class TransformerImpl<F, T> implements Transformer<F, T> {
    private final LinkedHashMap<Transform<F, T>, List<Validator<T>>> transformers;
    
    private TransformerImpl(LinkedHashMap<Transform<F, T>, List<Validator<T>>> transformer) {
        this.transformers = transformer;
    }
    
    @Override
    public T translate(F from) {
        List<Throwable> exceptions = new ArrayList<>();
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
        throw new AttemptsFailedException("Could not transform input; all attempts failed: " + from.toString() + "\n", exceptions);
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
        @SuppressWarnings("varargs")
        public final Builder<F, T> addTransform(Transform<F, T> transform, Validator<T>... validators) {
            transforms.put(transform, Arrays.asList(validators));
            return this;
        }
        
        public TransformerImpl<F, T> build() {
            return new TransformerImpl<>(transforms);
        }
    }
}
