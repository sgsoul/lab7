package collection;


import java.util.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.stream.Collectors;

import common.auth.User;
import common.data.*;
import common.exceptions.EmptyCollectionException;
import common.exceptions.NoSuchIdException;
import json.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


/**
 * Управление коллекцией.
 */

public class HumanCollectionManager implements HumanManager {
    private Vector<HumanBeing> collection;
    private java.time.LocalDateTime initDate;
    private Set<Integer> uniqueIds;

    public HumanCollectionManager() {
        uniqueIds = new HashSet<>();
        collection = new Vector<>();
        initDate = java.time.LocalDateTime.now();
    }

    public int generateNextId() {
        if (collection.isEmpty())
            return 1;
        else {
            Integer id = collection.lastElement().getId() + 1;
            if (uniqueIds.contains(id)) {
                while (uniqueIds.contains(id)) id += 1;
            }
            return id;
        }
    }

    public void sort() {
        Collections.sort(collection, new HumanBeing.SortingComparator());
    }

    /**
     * Возвращает коллекцию.
     */

    public Vector<HumanBeing> getCollection() {
        return collection;
    }

    /**
     * Добавление эллемента в коллекцию.
     */

    public void add(HumanBeing human) {
        Integer id = generateNextId();
        uniqueIds.add(id);
        human.setId(id);
        collection.add(human);
    }


    /**
     * Получение информации о коллекции.
     */

    public String getInfo() {
        return "Информация о коллекции, размер: " + collection.size() + ", дата инициализации: " + initDate.toString();
    }

    /**
     * Информация о том, используется ли этот идентификатор.
     */

    public boolean checkID(Integer ID) {
        if (uniqueIds.contains(ID)) return true;
        return false;
    }

    /**
     * Удалить элемент по идентификатору.
     */

    public boolean removeByID(Integer id) {
        Optional<HumanBeing> human = collection.stream()
                .filter(h -> h.getId() == id)
                .findFirst();
        if (human.isPresent()) {
            collection.remove(human.get());
            uniqueIds.remove(id);
            return true;
        }
        return false;
    }

    /**
     * Обновить элемент по идентификатору.
     */

    public boolean updateByID(Integer id, HumanBeing newHuman) {
        Optional<HumanBeing> human = collection.stream()
                .filter(h -> h.getId() == id)
                .findFirst();
        if (human.isPresent()) {
            collection.remove(human.get());
            newHuman.setId(id);
            collection.add(newHuman);
            return true;
        }
        return false;
    }

    /**
     * Получить размер коллекции.
     */

    public int getSize() {
        return collection.size();
    }

    /**
     * Очистить коллекцию.
     * @param user
     */

    public void clear(User user) {
        collection.clear();
        uniqueIds.clear();
    }

    /**
     * Удалить первого.
     */

    public void removeFirst() {
        int id = collection.get(0).getId();
        collection.remove(0);
        uniqueIds.remove(id);
    }

    /**
     * Добавить, если идентификатор элемента больше максимального в коллекции.
     */

    public boolean addIfMax(HumanBeing human) {
        if (collection.stream()
                .max(HumanBeing::compareTo)
                .filter(h -> h.compareTo(human) == 1)
                .isPresent()) {
            return false;
        }
        add(human);
        return true;
    }

    /**
     * Добавить, если идентификатор элемента меньше минимального в коллекции.
     */

    public boolean addIfMin(HumanBeing human) {
        if (collection.stream()
                .min(HumanBeing::compareTo)
                .filter(h -> h.compareTo(human) == -1)
                .isPresent()) {
            return false;
        }
        add(human);
        return true;
    }

    /**
     * Вывести элементы коллекции имя которых начинается с заданного значения
     */
    public List<HumanBeing> filterStartsWithName(String start) {

        List<HumanBeing> list = collection.stream()
                .filter(h -> h.getName().startsWith(start.trim()))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * Вывести список уникальных значений скорости
     */

    public List<Integer> getUniqueImpactSpeed() {
        List<Integer> impactSpeed = new LinkedList<>();
        impactSpeed = collection.stream()
                .map(human -> human.getImpactSpeed())
                .distinct()
                .collect(Collectors.toList());
        return impactSpeed;
    }

    /**
     * Вывести среднее время ожидания всех объектов коллекции
     */

    public double getAverageMinutesOfWaiting() {
        OptionalDouble minutes = collection.stream()
                .mapToDouble(humanBeing -> humanBeing.getMinutesOfWaiting())
                .average();
        return minutes.getAsDouble();
    }

    public HumanBeing getByID(Integer id) {
        assertNotEmpty();
        Optional<HumanBeing> worker = collection.stream()
                .filter(w -> w.getId() == id)
                .findFirst();
        if (!worker.isPresent()) {
            throw new NoSuchIdException(id);
        }
        return worker.get();
    }

    public void assertNotEmpty() {
        if (collection.isEmpty()) throw new EmptyCollectionException();
    }

    /**
     * Десериализация коллекции
     */

    public boolean deserializeCollection(String json) {
        boolean success = true;
        try {
            if (json == null || json.equals("")) {
                collection = new Vector<HumanBeing>();
            } else {
                Type collectionType = new TypeToken<Vector<HumanBeing>>() {
                }.getType();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                        .registerTypeAdapter(Date.class, new DateDeserializer())
                        .registerTypeAdapter(collectionType, new CollectionDeserializer(uniqueIds))
                        .create();
                collection = gson.fromJson(json.trim(), collectionType);
            }
        } catch (JsonParseException e) {
            success = false;
        }
        return success;

    }

    /**
     * Сериализация коллекции
     */
    public String serializeCollection() {
        if (collection == null || collection.isEmpty()) return "";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(Date.class, new DateSerializer())
                .setPrettyPrinting().create();
        String json = gson.toJson(collection);
        return json;
    }
}