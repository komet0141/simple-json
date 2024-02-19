package simpleJson.JsonValue.JsonPrimitive;

import java.io.FileWriter;
import java.io.IOException;

public class ValueBoolean extends JsonPrimitive<Boolean> {
    public ValueBoolean(boolean val) {
        super(val);
    }
    
    @Override
    public void exportValue(FileWriter fw) throws IOException {
        fw.write(String.valueOf(value));
    }
}
