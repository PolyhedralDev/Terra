package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import org.polydev.gaea.math.ProbabilityCollection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ProbabilityCollectionLoader implements TypeLoader<ProbabilityCollection<Object>> {
    @Override
    public ProbabilityCollection<Object> load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<Object, Integer> map = (Map<Object, Integer>) o;
        ProbabilityCollection<Object> collection = new ProbabilityCollection<>();

        if(type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type generic = pType.getActualTypeArguments()[0];
            for(Map.Entry<Object, Integer> entry : map.entrySet()) {
                collection.add(configLoader.loadType(generic, entry.getKey()), entry.getValue());
            }
        } else throw new LoadException("Unable to load config! Could not retrieve parameterized type: " + type);


        return null;
    }
}
