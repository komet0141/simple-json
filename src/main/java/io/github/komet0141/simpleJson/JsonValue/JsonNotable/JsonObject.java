package io.github.komet0141.simpleJson.JsonValue.JsonNotable;

import io.github.komet0141.simpleJson.JsonValue.JsonPrimitive.ValueBoolean;
import io.github.komet0141.simpleJson.JsonValue.JsonPrimitive.ValueDouble;
import io.github.komet0141.simpleJson.JsonValue.JsonPrimitive.ValueInteger;
import io.github.komet0141.simpleJson.JsonValue.JsonPrimitive.ValueString;
import io.github.komet0141.simpleJson.JsonValue.JsonValue;
import io.github.komet0141.simpleJson.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class JsonObject extends HashMap<String, JsonValue> implements JsonNotable<String> {
    private int depth = 0;
    
    public static JsonObject parseStringOrDefault(String s) {
        return CommonMethods.parseStringOrDefault(s, new JsonObject());
    }
    
    public static JsonObject parse(File f) {
        return CommonMethods.parseOrDefault(f, new JsonObject());
    }
    
    public static JsonObject parse(String path) {
        return CommonMethods.parseOrDefault(path, new JsonObject());
    }
    
    public JsonValue<?> put(String key, int i) {
        return super.put(key, new ValueInteger(i));
    }
    
    public JsonValue<?> put(String key, double d) {
        return super.put(key, new ValueDouble(d));
    }
    
    public JsonValue<?> put(String key, boolean b) {
        return super.put(key, new ValueBoolean(b));
    }
    
    public JsonValue<?> put(String key, String s) {
        return super.put(key, new ValueString(s));
    }
    
    @Override
    public JsonValue<?> get(Object key) {
        JsonValue<?> result;
        if ((result = super.get(key)) != null) return result;
        else return JsonValue.NULL;
    }
    
    @Override
    public final String getKeyRegex() {
        return "(" + VALUE_TYPE.STRING.REGEX + "):";
    }
    
    @Override
    public String indent(int depth) {
        return StringUtils.repeat(" ", NumIndentSpace.get() * depth);
    }
    
    @Override
    public final int getDepth() {
        return depth;
    }
    
    @Override
    public final void setDepth(int i) {
        depth = i;
    }
    
    @Override
    public final String[] getKeys() {
        return keySet().toArray(new String[0]);
    }
    
    @Override
    public final char getOpenBracket() {
        return '{';
    }
    
    @Override
    public final char getClosedBracket() {
        return '}';
    }
    
    @Override
    public final JsonValue<?> getValue(String key) {
        return get(key);
    }
    
    @Override
    public final String formatKey(String key) {
        return "\"" + key + "\" : ";
    }
    
    @Override
    public final void exportValue(FileWriter fw) throws IOException {
        CommonMethods.exportValue(fw, this);
    }
    
    @Override
    public JsonObject getValue() {
        return this;
    }
    
    @Override
    public void generateFile(File parent, String name) {
        CommonMethods.generateFile(parent, name, this);
    }
    
    @Override
    public JsonValue<?> getByPath(Object... keys) {
        return CommonMethods.getByPath(this, keys);
    }
}
