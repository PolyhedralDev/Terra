package com.dfsek.terra.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.polydev.gaea.math.ProbabilityCollection;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ProbabilityCollectionDeserializer<T> extends StdDeserializer<ProbabilityCollection<T>> {
    private static final ObjectMapper mapper = new ObjectMapper();

    protected ProbabilityCollectionDeserializer(Class<ProbabilityCollection<java.lang.String>> vc) {
        super(vc);
    }

    protected ProbabilityCollectionDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected ProbabilityCollectionDeserializer(StdDeserializer<ProbabilityCollection<java.lang.String>> src) {
        super(src);
    }

    @Override
    public ProbabilityCollection<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode rootNode = p.getCodec().readTree(p);

        ProbabilityCollection<T> collection = new ProbabilityCollection<>();

        Iterator<Map.Entry<String, JsonNode>> it = rootNode.fields();
        while(it.hasNext()) {
            Map.Entry<String, JsonNode> currentNode = it.next();

            collection.add(mapper.readValue(currentNode.getKey(), new TypeReference<T>() {
            }), currentNode.getValue().numberValue().intValue());
        }

        return collection;
    }
}
