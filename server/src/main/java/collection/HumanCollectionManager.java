package collection;


import java.util.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import common.collection.HumanManagerImpl;
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

public class HumanCollectionManager extends HumanManagerImpl<ConcurrentLinkedDeque<HumanBeing>> {
    private Deque<HumanBeing> collection;
    private final java.time.LocalDateTime initDate;
    private final Set<Integer> uniqueIds;

    public HumanCollectionManager() {
        uniqueIds = new ConcurrentSkipListSet<>();
        collection = new ConcurrentLinkedDeque<>();
        initDate = java.time.LocalDateTime.now();
    }

    public int generateNextId() {
        if (collection.isEmpty())
            return 1;
        else {
            int id = collection.element().getId() + 1;
            if (uniqueIds.contains(id)) {
                while (uniqueIds.contains(id)) id += 1;
            }
            return id;
        }
    }

    @Override
    public Deque<HumanBeing> getCollection() {
        return collection;
    }

    public void sort() {
        collection = collection.stream().sorted(new HumanBeing.SortingComparator()).collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
    }

    /**
     * Добавление элемента в коллекцию.
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


    public void removeByID(Integer id) {
        assertNotEmpty();
        Optional<HumanBeing> human = collection.stream()
                .filter(h -> h.getId() == id)
                .findFirst();
        if (!human.isPresent()) {
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

    public void clear() {
        collection.clear();
        uniqueIds.clear();
    }


    /**
     * Удалить первого.
     */

    public void removeFirst() {
        assertNotEmpty();
        int id = collection.getFirst().getId();
        collection.removeFirst();
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
        Optional<HumanBeing> human = collection.stream()
                .filter(h -> h.getId() == id)
                .findFirst();
        if (!human.isPresent()) {
            throw new NoSuchIdException(id);
        }
        return human.get();
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
                collection = new ConcurrentLinkedDeque<>();
            } else {
                Type collectionType = new TypeToken<Queue<HumanBeing>>() {
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

    protected Collection<HumanBeing> getAll(Collection<Integer> ids){
        Iterator<Integer> iterator = ids.iterator();
        Collection<HumanBeing> selected = new HashSet<>();
        while (iterator.hasNext()){
            Integer id = iterator.next();
            selected.addAll(collection.stream().filter(w->w.getId()==id).collect(Collectors.toCollection(HashSet::new)));
            iterator.remove();
        }
        return selected;
    }

    protected void removeAll(Collection<Integer> ids) {
        Iterator<Integer> iterator = ids.iterator();
        while (iterator.hasNext()) {
            Integer id = iterator.next();
            collection.removeIf(human -> human.getId() == id);
            iterator.remove();
        }
    }

    public Set<Integer> getUniqueIds(){
        return uniqueIds;
    }
}