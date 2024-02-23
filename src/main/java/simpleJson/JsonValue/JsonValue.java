package simpleJson.JsonValue;

import simpleJson.JsonValue.JsonNotable.JsonArray;
import simpleJson.JsonValue.JsonNotable.JsonObject;
import simpleJson.JsonValue.JsonPrimitive.*;

import java.io.FileWriter;
import java.io.IOException;

public interface JsonValue<T> {
    static JsonValue parse(String s) {
        switch (s.charAt(0)) {
            case '{':
                return new JsonObject();
            case '[':
                return new JsonArray();
            case '"':
                return new ValueString(s.substring(1, s.length() - 1));
            case 't':
                return new ValueBoolean(true);
            case 'f':
                return new ValueBoolean(false);
        }
        
        if (s.contains(".")) return new ValueDouble(Double.parseDouble(s));
        else return new ValueInteger(Integer.parseInt(s));
    }
    
    void exportValue(FileWriter fw) throws IOException;
    
    T getValue();
    
    JsonValue<?> get(Object key);
    
    enum VALUE_TYPE {
        BOOLEAN("true|false"),
        INTEGER("-?\\d+"),
        DOUBLE("-?\\d+\\.\\d+"),
        STRING("\".*?(?<!\\\\)\""),
        JSON_OBJECT("\\{"),
        JSON_ARRAY("\\[");
        
        public final String REGEX;
        
        VALUE_TYPE(String regex) {
            REGEX = regex;
        }
    }
    
    JsonValue<?> NULL = new JsonPrimitive(null) {
        @Override
        public void exportValue(FileWriter fw) throws IOException {
            throw new IOException("unable to export null");
        }
        
        @Override
        public JsonValue<?> get(Object key) {
            return JsonValue.NULL;
        }
    };
}
