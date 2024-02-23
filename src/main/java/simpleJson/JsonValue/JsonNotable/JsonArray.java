package simpleJson.JsonValue.JsonNotable;

import simpleJson.JsonValue.JsonPrimitive.ValueBoolean;
import simpleJson.JsonValue.JsonPrimitive.ValueDouble;
import simpleJson.JsonValue.JsonPrimitive.ValueInteger;
import simpleJson.JsonValue.JsonPrimitive.ValueString;
import simpleJson.JsonValue.JsonValue;
import simpleJson.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JsonArray extends ArrayList<JsonValue<?>> implements JsonNotable<JsonValue<?>> {
    private int depth = 0;
    
    public static JsonArray parseStringOrDefault(String s) {
        return CommonMethods.parseStringOrDefault(s, new JsonArray());
    }
    
    public static JsonArray parse(File f) {
        return CommonMethods.parseOrDefault(f, new JsonArray());
    }
    
    public static JsonArray parse(String path) {
        return CommonMethods.parseOrDefault(path, new JsonArray());
    }
    
    public void add(int i) {
        super.add(new ValueInteger(i));
    }
    
    public void add(double d) {
        super.add(new ValueDouble(d));
    }
    
    public void add(boolean b) {
        super.add(new ValueBoolean(b));
    }
    
    public void add(String s) {
        super.add(new ValueString(s));
    }
    
    @Override
    public final String getKeyRegex() {
        return "";
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
    public final JsonValue[] getKeys() {
        return toArray(new JsonValue[0]);
    }
    
    @Override
    public final char getOpenBracket() {
        return '[';
    }
    
    @Override
    public final char getClosedBracket() {
        return ']';
    }
    
    @Override
    public final JsonValue getValue(JsonValue key) {
        return key;
    }
    
    @Override
    public final String formatKey(JsonValue key) {
        return "";
    }
    
    @Override
    public final void exportValue(FileWriter fw) throws IOException {
        CommonMethods.exportValue(fw, this);
    }
    
    @Override
    public JsonArray getValue() {
        return this;
    }
    
    @Override
    public JsonValue<?> get(Object key) {
        if (key instanceof Integer) return get((int) key);
        return JsonValue.NULL;
    }
    
    @Override
    public JsonValue<?> get(int index) {
        try {
            return super.get(index);
        } catch (Exception e) {
            return JsonValue.NULL;
        }
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
