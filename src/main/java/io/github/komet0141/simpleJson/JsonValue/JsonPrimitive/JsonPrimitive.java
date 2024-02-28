package io.github.komet0141.simpleJson.JsonValue.JsonPrimitive;

import io.github.komet0141.simpleJson.JsonValue.JsonValue;

public abstract class JsonPrimitive<T> implements JsonValue<T> {
    protected final T value;
    
    public JsonPrimitive(T val) {
        value = val;
    }
    
    @Override
    public T getValue() {
        return value;
    }
    
    @Override
    public JsonValue<?> get(Object key) {
        return JsonValue.NULL;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
