package common.collection;

import common.data.HumanBeing;
import common.exceptions.CannotAddException;
import common.exceptions.EmptyCollectionException;
import common.exceptions.NoSuchIdException;

import java.util.*;
import java.util.stream.Collectors;

public abstract class HumanManagerImpl<T extends Collection<HumanBeing>> implements HumanManager {

    private final java.time.LocalDateTime initDate;

    /**
     * Конструктор для начальных значений.
     */

    public HumanManagerImpl() {
        initDate = java.time.LocalDateTime.now();
    }

    public int generateNextId() {
        if (getCollection().isEmpty())
            return 1;
        else {
            int id = 1;
            if (getUniqueIds().contains(id)) {
                while (getUniqueIds().contains(id)) id += 1;
            }
            return id;
        }
    }

    public void sort() {
    }

    /**
     * Return collection
     */

    public abstract Collection<HumanBeing> getCollection();


    /**
     * Add element to collection
     */

    public void add(HumanBeing human) {
        int id = generateNextId();
        getUniqueIds().add(id);
        human.setId(id);
        getCollection().add(human);
    }

    public HumanBeing getByID(Integer id) {
        assertNotEmpty();
        Optional<HumanBeing> human = getCollection().stream()
                .filter(h -> h.getId() == id)
                .findFirst();
        if (!human.isPresent()) {
            throw new NoSuchIdException(id);
        }
        return human.get();
    }

    protected void addWithoutIdGeneration(HumanBeing human) {
        getUniqueIds().add(human.getId());
        getCollection().add(human);
    }

    protected void removeAll(Collection<Integer> ids) {
        Iterator<Integer> iterator = ids.iterator();
        while (iterator.hasNext()) {
            Integer id = iterator.next();
            getCollection().removeIf(human -> human.getId() == id);
            iterator.remove();
        }
    }

    /**
     * Get information about collection
     */

    public String getInfo() {
        return "[DatabaseInfo] База данных HumanBeing, размер: [" + getCollection().size() + "] ; дата инициализации: [" + initDate.toString()+"]";
    }

    /**
     * Give info about is this ID used
     */

    public boolean checkID(Integer ID) {
        return getUniqueIds().contains(ID);
    }

    public void assertNotEmpty() {
        if (getCollection().isEmpty()) throw new EmptyCollectionException();
    }

    /**
     * Delete element by ID
     */

    public void removeByID(Integer id) {
        assertNotEmpty();
        Optional<HumanBeing> human = getCollection().stream()
                .filter(h -> h.getId() == id)
                .findFirst();
        if (!human.isPresent()) {
            throw new NoSuchIdException(id);
        }
        getCollection().remove(human.get());
        getUniqueIds().remove(id);
    }

    /**
     * Delete element by ID
     *
     * @param id ID
     */
    public void updateByID(Integer id, HumanBeing newHuman) {
        assertNotEmpty();
        Optional<HumanBeing> human = getCollection().stream()
                .filter(h -> h.getId() == id)
                .findFirst();
        if (!human.isPresent()) {
            throw new NoSuchIdException(id);
        }
        getCollection().remove(human.get());
        newHuman.setId(id);
        getCollection().add(newHuman);
    }

    /**
     * Get size of collection
     */

    public int getSize() {
        return getCollection().size();
    }


    public void clear() {
        getCollection().clear();
        getUniqueIds().clear();
    }

    public void removeFirst() {
        assertNotEmpty();
        Iterator<HumanBeing> it = getCollection().iterator();
        int id = it.next().getId();
        it.remove();
        getUniqueIds().remove(id);
    }

    /**
     * Add if ID of element bigger than max in collection
     */

    public void addIfMax(HumanBeing human) {
        if (getCollection().stream()
                .max(HumanBeing::compareTo)
                .filter(h -> h.compareTo(human) == 1)
                .isPresent()) {
            throw new CannotAddException();
        }
        add(human);
    }

    /**
     * Add if ID of element smaller than min in collection
     */

    public void addIfMin(HumanBeing human) {
        if (getCollection().stream()
                .min(HumanBeing::compareTo)
                .filter(h -> h.compareTo(human) < 0)
                .isPresent()) {
            throw new CannotAddException();
        }
        add(human);
    }

    public List<HumanBeing> filterStartsWithName(String start) {
        assertNotEmpty();
        return getCollection().stream()
                .filter(h -> h.getName().startsWith(start.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Вывести список уникальных значений скорости
     */

    public List<Integer> getUniqueImpactSpeed() {
        assertNotEmpty();
        List<Integer> impactSpeed = new LinkedList<>();
        impactSpeed = getCollection().stream()
                .map(HumanBeing::getImpactSpeed)
                .distinct()
                .collect(Collectors.toList());
        return impactSpeed;
    }

    /**
     * Вывести среднее время ожидания всех объектов коллекции
     */

    public double getAverageMinutesOfWaiting() {
        OptionalDouble minutes = getCollection().stream()
                .mapToDouble(HumanBeing::getMinutesOfWaiting)
                .average();
        return minutes.getAsDouble();
    }

    @Override
    public void deserializeCollection(String data) {
    }

    @Override
    public String serializeCollection() {
        return null;
    }

    abstract public Set<Integer> getUniqueIds();


}
