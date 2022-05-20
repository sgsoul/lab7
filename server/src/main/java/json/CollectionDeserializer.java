package json;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.Vector;

import common.data.*;
import com.google.gson.*;
import log.Log;


/**
 * ��������������.
 */

public class CollectionDeserializer implements JsonDeserializer<Vector<HumanBeing>> {
    private final Set<Integer> uniqueIds;

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
                    Log.logger.error("������ ������ �������.");
                    throw new JsonParseException("������ �������.");
                }
                if (!jsonHuman.getAsJsonObject().has("id")) {
                    Log.logger.error("������ ������� ��� id.");
                    throw new JsonParseException("��� id");
                }
                human = context.deserialize(jsonHuman, HumanBeing.class);

                Integer id = human.getId();

                if (uniqueIds.contains(id)) {
                    Log.logger.error("������� � ����� ��������������� ��� ���� #" + id);
                    throw new JsonParseException("���� id �� �������� ����������.");
                }
                if (!human.validate()) {
                    Log.logger.error("human #" + id + " �� ������������� ��������.");
                    throw new JsonParseException("�������� ������.");
                }
                uniqueIds.add(id);
                collection.add(human);
            } catch (JsonParseException e) {
                damagedElements += 1;
            }
        }
        if (collection.size() == 0) {
            if (damagedElements == 0) Log.logger.error("���� �����.");
            else Log.logger.error("��� �������� ����������.");
            throw new JsonParseException("��� ������.");
        }
        if (damagedElements != 0)
            Log.logger.error(damagedElements + " �������� � ���� ������ ����������.");
        return collection;
    }
}
