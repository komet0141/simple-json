import simpleJson.JsonValue.JsonNotable.JsonArray;
import simpleJson.JsonValue.JsonNotable.JsonObject;

import java.io.File;

public class Driver {
    public static void main(String[] args) {
        JsonArray myJSA = new JsonArray();
        JsonObject myJson = new JsonObject();
        myJSA.add(myJson);
        myJson.put("integer", 1024);
        myJson.put("string", "hello world");
        myJson.put("foo", "bar");
        
        JsonObject innerJson = new JsonObject();
        myJson.put("object", innerJson);
        innerJson.put("recursive", "hello world from inner object");
        innerJson.put("double", 3.14);
        myJSA.add(1);
        myJSA.add(myJson);
        myJSA.add(true);
        
        
        File parent = new File(new File(".").getAbsoluteFile().getParentFile(), "/src/test/resources/");
        myJSA.generateFile(parent, "test.json");
        
        JsonObject parsedJson = JsonObject.parse(new File(parent, "example.json"));
        System.out.println(parsedJson);
        
        // parsedJson.glossary.GlossDiv.GlossList.GlossEntry.GlossDef.GlossSeeAlso[1]
        System.out.println(parsedJson.get("glossary").get("GlossDiv").get("GlossList").get("GlossEntry").get("GlossDef").get("GlossSeeAlso").get(1));
        // root[0].integer.object.recursive
        System.out.println(myJSA.getByPath(0, "object", "recursive"));
    }
}
