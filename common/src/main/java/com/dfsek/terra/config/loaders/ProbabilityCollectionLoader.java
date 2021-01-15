package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("unchecked")
public class ProbabilityCollectionLoader implements TypeLoader<ProbabilityCollection<Object>> {
    @Override
    public ProbabilityCollection<Object> load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        ProbabilityCollection<Object> collection = new ProbabilityCollection<>();

        if(type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type generic = pType.getActualTypeArguments()[0];
            if(o instanceof Map) {
                Map<Object, Integer> map = (Map<Object, Integer>) o;
                for(Map.Entry<Object, Integer> entry : map.entrySet()) {
                    collection.add(configLoader.loadType(generic, entry.getKey()), entry.getValue());
                }
            } else if(o instanceof List) {
                List<Map<Object, Integer>> map = (List<Map<Object, Integer>>) o;
                for(Map<Object, Integer> l : map) {
                    for(Map.Entry<Object, Integer> entry : l.entrySet()) {
                        collection.add(configLoader.loadType(generic, entry.getKey()), entry.getValue());
                    }
                }
            } else if(o instanceof String) {
                return new Singleton<>(configLoader.loadType(generic, o));
            } else {
                throw new LoadException("Malformed Probability Collection: " + o);
            }
        } else throw new LoadException("Unable to load config! Could not retrieve parameterized type: " + type);


        return collection;
    }

    private static final class Singleton<T> extends ProbabilityCollection<T> {
        private final T single;

        private Singleton(T single) {
            this.single = single;
        }

        @Override
        public ProbabilityCollection<T> add(T item, int probability) {
            throw new UnsupportedOperationException();
        }

        @Override
        public T get(Random r) {
            return single;
        }

        @Override
        public T get(NoiseSampler n, double x, double y, double z) {
            return single;
        }

        @Override
        public T get(NoiseSampler n, double x, double z) {
            return single;
        }

        @Override
        public int getTotalProbability() {
            return 1;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public Set<T> getContents() {
            return Collections.singleton(single);
        }
    }
}
