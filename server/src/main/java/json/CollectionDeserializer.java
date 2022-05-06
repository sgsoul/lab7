package json;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.Vector;

import common.data.*;
import com.google.gson.*;
import log.Log;


/**
 * Десериализатор.
 */

public class CollectionDeserializer implements JsonDeserializer<Vector<HumanBeing>> {
    private Set<Integer> uniqueIds;

    public CollectionDeserializer(Set<Integer> uniqueIds) {
        this.uniqueIds = uniqueIds;
    }

    @Override
    public Vector<HumanBeing> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        Vector<HumanBeing> collection = new Vector<>();
        JsonArray humans = json.getAsJsonArray();
        int damagedElements = 0;
        for (JsonElement jsonHuman : humans) {
            HumanBeing human = null;
            try {
                if (jsonHuman.getAsJsonObject().entrySet().isEmpty()) {
                    Log.logger.error("Найден пустой человек.");
                    throw new JsonParseException("Пустой человек.");
                }
                if (!jsonHuman.getAsJsonObject().has("id")) {
                    Log.logger.error("Найден человек без id.");
                    throw new JsonParseException("Нет id");
                }
                human = context.deserialize(jsonHuman, HumanBeing.class);

                Integer id = human.getId();

                if (uniqueIds.contains(id)) {
                    Log.logger.error("Человек с таким идентификатором уже есть #" + id);
                    throw new JsonParseException("Этот id не является уникальным.");
                }
                if (!human.validate()) {
                    Log.logger.error("human #" + id + " не соответствует условиям.");
                    throw new JsonParseException("Неверные данные.");
                }
                uniqueIds.add(id);
                collection.add(human);
            } catch (JsonParseException e) {
                damagedElements += 1;
            }
        }
        if (collection.size() == 0) {
            if (damagedElements == 0) Log.logger.error("База пуста.");
            else Log.logger.error("Все элементы повреждены.");
            throw new JsonParseException("Нет данных.");
        }
        if (damagedElements != 0)
            Log.logger.error(damagedElements + " элементы в базе данных повреждены.");
        return collection;
    }
}
