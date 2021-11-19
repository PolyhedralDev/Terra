/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.block.entity;

import java.util.HashMap;
import java.util.Map;


public class SerialState {
    protected final Map<String, Property<?>> properties = new HashMap<>();
    
    public SerialState() {
    }
    
    public static Map<String, String> parse(String props) {
        String[] sep = props.split(",");
        Map<String, String> map = new HashMap<>();
        for(String item : sep) {
            map.put(item.substring(0, item.indexOf('=')), item.substring(item.indexOf('=') + 1));
        }
        return map;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(String id, Class<T> clazz) {
        checkExists(id);
        Property<?> prop = properties.get(id);
        checkType(clazz, prop.getValue(), id);
        return (T) prop.getValue();
    }
    
    private void checkExists(String prop) {
        if(!properties.containsKey(prop)) throw new IllegalArgumentException("No such property \"" + prop + "\"");
    }
    
    private void checkType(Class<?> clazz, Object o, String id) {
        if(!clazz.isInstance(o))
            throw new IllegalArgumentException("Invalid data for property " + id + ": " + o);
    }
    
    public void setProperty(String id, Object value) {
        checkExists(id);
        Property<?> prop = properties.get(id);
        checkType(prop.getValueClass(), value, id);
        prop.setValue(value);
    }
    
    public int getInteger(String id) {
        checkExists(id);
        Property<?> prop = properties.get(id);
        checkType(Integer.class, prop.getValue(), id);
        return (Integer) prop.getValue();
    }
    
    public String getString(String id) {
        checkExists(id);
        Property<?> prop = properties.get(id);
        checkType(String.class, prop.getValue(), id);
        return (String) prop.getValue();
    }
    
    public long getLong(String id) {
        checkExists(id);
        Property<?> prop = properties.get(id);
        checkType(Long.class, prop.getValue(), id);
        return (Long) prop.getValue();
    }
    
    public boolean getBoolean(String id) {
        checkExists(id);
        Property<?> prop = properties.get(id);
        checkType(Boolean.class, prop.getValue(), id);
        return (Boolean) prop.getValue();
    }
    
    
    protected static class Property<T> {
        private final Class<T> clazz;
        private Object value;
        
        public Property(Class<T> clazz) {
            this.clazz = clazz;
        }
        
        public Class<T> getValueClass() {
            return clazz;
        }
        
        @SuppressWarnings("unchecked")
        public T getValue() {
            return (T) value;
        }
        
        public void setValue(Object value) {
            this.value = value;
        }
    }
}
