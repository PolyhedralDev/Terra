package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SelfProbabilityCollectionLoader<T> implements TypeLoader<ProbabilityCollection<T>> {

    @Override
    public ProbabilityCollection<T> load(Type type, Object o, ConfigLoader loader) throws LoadException {
        ProbabilityCollection<T> collection = new ProbabilityCollection<>();

        if(type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type generic = pType.getActualTypeArguments()[0];
            if(o instanceof Map) {
                Map<Object, Integer> map = (Map<Object, Integer>) o;
                addItems(loader, collection, generic, map);
            } else if(o instanceof List) {
                List<Map<Object, Integer>> map = (List<Map<Object, Integer>>) o;
                for(Map<Object, Integer> l : map) {
                    addItems(loader, collection, generic, l);
                }
            } else if(o instanceof String) {
                return new ProbabilityCollection.Singleton<>((T) loader.loadType(generic, o));
            } else {
                throw new LoadException("Malformed Probability Collection: " + o);
            }
        } else throw new LoadException("Unable to load config! Could not retrieve parameterized type: " + type);


        return collection;
    }

    private void addItems(ConfigLoader loader, ProbabilityCollection<T> collection, Type generic, Map<Object, Integer> l) throws LoadException {
        for(Map.Entry<Object, Integer> entry : l.entrySet()) {
            if(entry.getKey().toString().equals("SELF")) {
                collection.add(null, entry.getValue()); // hmm maybe replace this with something better later
                continue;
            }
            collection.add((T) loader.loadType(generic, entry.getKey()), entry.getValue());
        }
    }
}
