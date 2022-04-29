package json;

import java.lang.reflect.Type;
import java.time.LocalDate;

import static common.utils.DateConverter.*;

import com.google.gson.*;

public class LocalDateSerializer implements JsonSerializer<LocalDate> {
    @Override
    public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(dateToString(localDate));
    }
}
