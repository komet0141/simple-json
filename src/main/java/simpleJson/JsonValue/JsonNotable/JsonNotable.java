package simpleJson.JsonValue.JsonNotable;

import simpleJson.JsonValue.JsonValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface JsonNotable<K> extends JsonValue {
    String valueRegex = "(" + Arrays.stream(VALUE_TYPE.values()).map(v -> v.REGEX).reduce((acc, s) -> acc + "|" + s).get() + ")";
    String spaceMatcherRegex = "^\\s*(?=\\S)|(?<=\\S)\\s*(?=:)|(?<=:)\\s*(?=\\S)|(?<=\\S)\\s*(?=,)|(?<=,)\\s*$";
    
    K[] getKeys();
    
    char getOpenBracket();
    
    char getClosedBracket();
    
    JsonValue<?> getValue(K key);
    
    String formatKey(K key);
    
    String getKeyRegex();
    
    int getDepth();
    
    void setDepth(int i);
    
    String indent(int depth);
    
    JsonValue<?> get(Object key);
    
    void generateFile(File parent, String name);
    
    JsonValue<?> getByPath(Object... keys);
    
    abstract class CommonMethods {
        protected static void generateFile(File parent, String name, JsonNotable<?> json) {
            File jsonFile = new File(parent, name);
            try {
                FileWriter fw = new FileWriter(jsonFile);
                exportValue(fw, json);
                
                fw.close();
            } catch (IOException e) {
                System.out.println("failed to generate " + jsonFile.getName());
                e.printStackTrace();
            }
        }
        
        protected static <T> void exportValue(FileWriter fw, JsonNotable<T> json) throws IOException {
            int depth = json.getDepth();
            fw.write(json.getOpenBracket() + "\n");
            T[] keys = json.getKeys();
            
            for (int i = 0; i < keys.length; i++) {
                T key = keys[i];
                JsonValue<?> value = json.getValue(key);
                
                fw.write(json.indent(depth + 1) + json.formatKey(key));
                if (value instanceof JsonNotable) ((JsonObject) value).setDepth(depth + 1);
                value.exportValue(fw);
                
                if (i != keys.length - 1) fw.write(",");
                fw.write("\n");
            }
            
            fw.write(json.indent(depth) + json.getClosedBracket());
        }
        
        protected static <JSON extends JsonNotable<?>> JSON parseStringOrDefault(String s, JSON defaultJson) {
            JSON json = (JSON) (defaultJson instanceof JsonObject ? new JsonObject() : new JsonArray());
            
            try {
                if (s.length() < 3) throw new JsonParseError("provided string too short to be a valid .json");
                if (json.getOpenBracket() != s.charAt(0))
                    throw new JsonParseError("provided json type does not match the default type.");
                if (!isBracketMatching(s, json))
                    throw new JsonParseError("provided json does not have matching number of brackets");
                Matcher matcher = Pattern.compile(json.getKeyRegex() + valueRegex).matcher(s.substring(1));
                
                while (matcher.find()) {
                    JsonValue<?> value = JsonValue.parse(matcher.group(matcher.groupCount()));
                    int idx = -1;
                    if (value instanceof JsonObject || value instanceof JsonArray) {
                        JsonNotable<?> valueJson = (JsonNotable<?>) value;
                        int len = findJsonLength(s.substring(matcher.end()), valueJson);
                        idx = matcher.end() + len;
                        value = parseStringOrDefault(s.substring(matcher.end(), idx), valueJson);
                    }
                    
                    if (json instanceof JsonObject) {
                        String key = matcher.group(1);
                        ((JsonObject) json).put(key.substring(1, key.length() - 1), value);
                    } else ((JsonArray) json).add(value);
                    
                    if (idx != -1) matcher.region(idx, matcher.regionEnd());
                }
                
                return json;
            } catch (JsonParseError e) {
                e.printStackTrace();
                return defaultJson;
            }
        }
        
        private static int findJsonLength(String s, JsonNotable<?> json) throws JsonParseError {
            int depth = 0;
            String p = Pattern.quote(json.getOpenBracket() + "") + "|" + VALUE_TYPE.STRING.REGEX + "|" + Pattern.quote(json.getClosedBracket() + "");
            Matcher matcher = Pattern.compile(p).matcher(s);
            while (matcher.find()) {
                String match = matcher.group();
                if (match.length() > 1) continue;
                switch (match.charAt(0) % 32) {
                    case 27: //open bracket
                        depth++;
                        break;
                    case 29: //close bracket
                        depth--;
                        break;
                }
                if (depth == 0) return matcher.end();
            }
            throw new JsonParseError("could not find matching number of brackets while parsing: " + s);
        }
        
        private static boolean isBracketMatching(String s, JsonNotable<?> json) {
            String openBracketFinder = bracketFinderGenerator(json.getOpenBracket());
            String closedBracketFinder = bracketFinderGenerator(json.getClosedBracket());
            return s.replaceAll(openBracketFinder, "$1").length() -
                    s.replaceAll(closedBracketFinder, "$1").length()
                    == 0;
        }
        
        private static String bracketFinderGenerator(char bracket) {
            return "(\".*?\\" + bracket + "*.*?\")|\\" + bracket;
        }
        
        protected static <JSON extends JsonNotable<?>> JSON parseOrDefault(File f, JSON defaultJson) {
            JSON json;
            try {
                BufferedReader bf = Files.newBufferedReader(f.toPath());
                
                json = parseStringOrDefault(
                        bf.lines().map(str -> str.replaceAll(spaceMatcherRegex, ""))
                                .reduce("", String::concat),
                        defaultJson
                );
                
                bf.close();
            } catch (Exception e) {
                System.out.println("failed to parse " + f.getName());
                e.printStackTrace();
                
                json = defaultJson;
            }
            
            
            return json;
        }
        
        protected static <JSON extends JsonNotable<?>> JSON parseOrDefault(String path, JSON defaultJson) {
            return parseOrDefault(new File(path), defaultJson);
        }
        
        protected static JsonValue<?> getByPath(JsonValue<?> json, Object... keys) {
            return (JsonValue<?>) Arrays.stream(keys).reduce(json, (acc, key) -> ((JsonValue<?>) acc).get(key));
        }
    }
    
    abstract class NumIndentSpace {
        private static int num = 4;
        
        public static void set(int i) {
            num = i;
        }
        
        public static int get() {
            return num;
        }
    }
    
    class JsonParseError extends Exception {
        public JsonParseError(String s) {
            super(s);
        }
    }
}