package simpleJson.JsonValue.JsonPrimitive;

import java.io.FileWriter;
import java.io.IOException;

public class ValueString extends JsonPrimitive<String> {
    public ValueString(String val) {
        super(val);
    }
    
    @Override
    public void exportValue(FileWriter fw) throws IOException {
        fw.write("\"" + value + "\"");
    }
}
