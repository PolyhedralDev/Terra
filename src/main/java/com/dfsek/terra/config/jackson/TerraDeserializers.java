package com.dfsek.terra.config.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.MapLikeType;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.ProbabilityCollection;


public class TerraDeserializers extends Deserializers.Base {
    public TerraDeserializers() {
    }
    
    @Override
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config,
                                                    BeanDescription beanDesc) throws JsonMappingException {
        if (type.isAssignableFrom(Material.class))
            return new MaterialDeserializer();
        return null;
    }
    
    @Override
    public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config,
                                                    BeanDescription beanDesc) throws JsonMappingException {
        if (type.hasRawClass(BlockData.class))
            return new BlockDataDeserializer();
        return null;
    }
    
    @Override
    public JsonDeserializer<?> findMapLikeDeserializer(MapLikeType type, DeserializationConfig config,
                                                       BeanDescription beanDesc, KeyDeserializer keyDeserializer,
                                                       TypeDeserializer elementTypeDeserializer,
                                                       JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        Class<?> raw = type.getRawClass();
        if (ProbabilityCollection.class.isAssignableFrom(raw))
            return new ProbabilityCollectionDeserializer(type, keyDeserializer, elementDeserializer, elementTypeDeserializer, null);
        return null;
    }
    
    @Override
    public boolean hasDeserializerFor(DeserializationConfig config, Class<?> valueType) {
        return (valueType == ProbabilityCollection.class)
               || false;
    }
}
