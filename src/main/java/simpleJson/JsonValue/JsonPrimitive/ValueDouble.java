package simpleJson.JsonValue.JsonPrimitive;

import java.io.FileWriter;
import java.io.IOException;

public class ValueDouble extends JsonPrimitive<Double> {
    public ValueDouble(double val) {
        super(val);
    }
    
    @Override
    public void exportValue(FileWriter fw) throws IOException {
        fw.write(String.valueOf(value));
    }
}
