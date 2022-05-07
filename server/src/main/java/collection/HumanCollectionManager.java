package collection;


import java.util.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.stream.Collectors;

import common.data.*;
import common.exceptions.CollectionException;
import common.exceptions.CannotAddException;
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
    private final java.time.LocalDateTime initDate;
    private final Set<Integer> uniqueIds;

    public HumanCollectionManager() {
        uniqueIds = new HashSet<>();
        collection = new Vector<>();
        initDate = java.time.LocalDateTime.now();
    }

    public int generateNextId() {
        if (collection.isEmpty())
            return 1;
        else {
            int id = collection.lastElement().getId() + 1;
            if (uniqueIds.contains(id)) {
                while (uniqueIds.contains(id)) id += 1;
            }
            return id;
        }
    }

    public void sort() {
        collection.sort(new HumanBeing.SortingComparator());
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
        int id = generateNextId();
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
        return uniqueIds.contains(ID);
    }

    /**
     * Удалить элемент по идентификатору.
     *
     */

    public void removeByID(Integer id) {
        assertNotEmpty();
        Optional<HumanBeing> human = collection.stream()
                .filter(h -> h.getId() == id)
                .findFirst();
        if (human.isPresent()) {
            throw new NoSuchIdException(id);
        }
        collection.remove(human.get());
        uniqueIds.remove(id);
    }

    /**
     * Обновить элемент по идентификатору.
     */

    public void updateByID(Integer id, HumanBeing newHuman) {
        assertNotEmpty();
        Optional<HumanBeing> human = collection.stream()
                .filter(h -> h.getId() == id)
                .findFirst();
        if (!human.isPresent()) {
            throw new NoSuchIdException(id);
        }
        collection.remove(human.get());
        newHuman.setId(id);
        collection.add(newHuman);
    }

    /**
     * Получить размер коллекции.
     */

    public int getSize() {
        return collection.size();
    }

    /**
     * Очистить коллекцию.
     */
// User user
    public void clear() {
        collection.clear();
        uniqueIds.clear();
    }

    /**
     * Удалить первого.
     */

    public void removeFirst() {
        assertNotEmpty();
        int id = collection.get(0).getId();
        collection.remove(0);
        uniqueIds.remove(id);
    }

    /**
     * Добавить, если идентификатор элемента больше максимального в коллекции.
     */

    public void addIfMax(HumanBeing human) {
        if (collection.stream()
                .max(HumanBeing::compareTo)
                .filter(h -> h.compareTo(human) == 1)
                .isPresent()) {
            throw new CannotAddException();
        }
        add(human);
    }

    /**
     * Добавить, если идентификатор элемента меньше минимального в коллекции.
     */

    public void addIfMin(HumanBeing human) {
        if (collection.stream()
                .min(HumanBeing::compareTo)
                .filter(h -> h.compareTo(human) == -1)
                .isPresent()) {
            throw new CannotAddException();
        }
        add(human);
    }

    /**
     * Вывести элементы коллекции имя которых начинается с заданного значения
     */
    public List<HumanBeing> filterStartsWithName(String start) {
        assertNotEmpty();
        return collection.stream()
                .filter(w -> w.getName().startsWith(start.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Вывести список уникальных значений скорости
     */

    public List<Integer> getUniqueImpactSpeed() {
        assertNotEmpty();
        List<Integer> impactSpeed = new LinkedList<>();
        impactSpeed = collection.stream()
                .map(HumanBeing::getImpactSpeed)
                .distinct()
                .collect(Collectors.toList());
        return impactSpeed;
    }

    /**
     * Вывести среднее время ожидания всех объектов коллекции
     */

    public double getAverageMinutesOfWaiting() {
        OptionalDouble minutes = collection.stream()
                .mapToDouble(HumanBeing::getMinutesOfWaiting)
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

    public void deserializeCollection(String json) {
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
            throw new CollectionException("Не удалось загрузить.");
        }
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
        return gson.toJson(collection);
    }

    protected void addWithoutIdGeneration(HumanBeing human) {
        uniqueIds.add(human.getId());
        collection.add(human);
    }

    protected void removeAll(Collection<Integer> ids){
        Iterator<Integer> iterator = ids.iterator();
        while (iterator.hasNext()){
            Integer id = iterator.next();
            collection.removeIf(human -> human.getId()==id);
            iterator.remove();
        }
    }

}