package com.dfsek.terra.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.polydev.gaea.math.ProbabilityCollection;

import java.io.IOException;

public class ProbabilityCollectionDeserializer extends ContainerDeserializerBase<ProbabilityCollection<Object>> implements ContextualDeserializer {

    protected final TypeDeserializer _valueTypeDeserializer;
    protected KeyDeserializer _keyDeserializer;
    protected JsonDeserializer<?> _valueDeserializer;

    public ProbabilityCollectionDeserializer(JavaType type, KeyDeserializer keyDeser, JsonDeserializer<?> valueDeser,
                                             TypeDeserializer valueTypeDeser, NullValueProvider nuller) {
        super(type, nuller, null);
        _keyDeserializer = keyDeser;
        _valueDeserializer = valueDeser;
        _valueTypeDeserializer = valueTypeDeser;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
                                                BeanProperty property) throws JsonMappingException {
        KeyDeserializer keyDeser = _keyDeserializer;
        if(keyDeser == null) {
            keyDeser = ctxt.findKeyDeserializer(_containerType.getKeyType(), property);
        } else {
            if(keyDeser instanceof ContextualKeyDeserializer) {
                keyDeser = ((ContextualKeyDeserializer) keyDeser).createContextual(ctxt, property);
            }
        }

        JsonDeserializer<?> valueDeser = _valueDeserializer;
        // [databind#125]: May have a content converter
        if(property != null) {
            valueDeser = findConvertingContentDeserializer(ctxt, property, valueDeser);
        }
        final JavaType vt = _containerType.getContentType();
        if(valueDeser == null) {
            valueDeser = ctxt.findContextualValueDeserializer(vt, property);
        } else { // if directly assigned, probably not yet contextual, so:
            valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, vt);
        }
        TypeDeserializer vtd = _valueTypeDeserializer;
        if(vtd != null) {
            vtd = vtd.forProperty(property);
        }
        AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
        return withResolved(keyDeser, vtd, valueDeser,
                findContentNullProvider(ctxt, property, valueDeser));
    }

    protected ProbabilityCollectionDeserializer withResolved(KeyDeserializer keyDeser, TypeDeserializer valueTypeDeser,
                                                             JsonDeserializer<?> valueDeser, NullValueProvider nuller) {
        if((_keyDeserializer == keyDeser) && (_valueDeserializer == valueDeser)
                && (_valueTypeDeserializer == valueTypeDeser) && (_nullProvider == nuller)) {
            return this;
        }
        return new ProbabilityCollectionDeserializer(_containerType, keyDeser, valueDeser, valueTypeDeser, nuller);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<Object> getContentDeserializer() {
        return (JsonDeserializer<Object>) _valueDeserializer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ProbabilityCollection<Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Ok: must point to START_OBJECT, FIELD_NAME or END_OBJECT
        JsonToken t = p.getCurrentToken();
        if(t != JsonToken.START_OBJECT) {
            return (ProbabilityCollection<Object>) ctxt.handleUnexpectedToken(getValueType(ctxt), t, p, null);
        }
        final ProbabilityCollection<Object> result = new ProbabilityCollection<>();
        _readAndBind(p, ctxt, result);
        return result;
    }

    protected final void _readAndBind(JsonParser p, DeserializationContext ctxt,
                                      ProbabilityCollection<Object> result) throws IOException {
        final KeyDeserializer keyDes = _keyDeserializer;

        String keyStr;
        if(p.isExpectedStartObjectToken()) {
            keyStr = p.nextFieldName();
        } else {
            JsonToken t = p.getCurrentToken();
            if(t == JsonToken.END_OBJECT) {
                return;
            }
            ctxt.reportWrongTokenException(this, JsonToken.FIELD_NAME, null);
            keyStr = p.getCurrentName();
        }

        while(keyStr != null) {
            Object key = keyDes.deserializeKey(keyStr, ctxt);
            int value = _parseIntPrimitive(ctxt, p.nextToken().asString());
            result.add(key, value);


            keyStr = p.nextFieldName();
        }
    }

}
