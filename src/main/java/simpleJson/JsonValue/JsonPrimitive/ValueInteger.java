package simpleJson.JsonValue.JsonPrimitive;

import java.io.FileWriter;
import java.io.IOException;

public class ValueInteger extends JsonPrimitive<Integer> {
    public ValueInteger(int val) {
        super(val);
    }
    
    @Override
    public void exportValue(FileWriter fw) throws IOException {
        fw.write(String.valueOf(value));
    }
}
