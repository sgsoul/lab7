package common.collection;

import common.data.HumanBeing;

import java.util.Collection;
import java.util.List;

/**
 * Iнтерфейс для хранения элементов.
 */

public interface HumanManager {

    /**
     * sorts collection
     */

    void sort();

    Collection<HumanBeing> getCollection();

    /**
     * adds new element
     */

    void add(HumanBeing element);

    /**
     * get information about collection
     */

    String getInfo();

    /**
     * checks if collection contains element with particular id
     */

    boolean checkID(Integer ID);

    /**
     * removes element by id
     */

    void removeByID(Integer id);

    /**
     * updates element by id
     */

    void updateByID(Integer id, HumanBeing newElement);


    void clear();

    HumanBeing getByID(Integer id);

    void removeFirst();

    /**
     * adds element if it is greater than max
     */

    void addIfMax(HumanBeing element);

    /**
     * adds element if it is smaller than min
     */

    void addIfMin(HumanBeing element);

    /**
     * print all elements which name starts with substring
     */

    List<HumanBeing> filterStartsWithName(String start);


    /**
     * convert collection to json
     */

    void deserializeCollection(String data);

    /**
     * parse collection from json
     */

    String serializeCollection();

    /**
     * Вывести список уникальных значений скорости
     */

    List<Integer> getUniqueImpactSpeed();

    /**
     * Вывести среднее время ожидания всех объектов коллекции
     */

    double getAverageMinutesOfWaiting();

}

