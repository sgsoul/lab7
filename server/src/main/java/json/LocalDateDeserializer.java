package json;

import java.lang.reflect.Type;
import java.time.LocalDate;

import static common.utils.DateConverter.*;

import com.google.gson.*;

import common.exceptions.InvalidDateFormatException;

public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return parseLocalDate(json.getAsJsonPrimitive().getAsString());
        } catch (InvalidDateFormatException e) {
            throw new JsonParseException("");
        }
    }
}