package collection;

import common.data.HumanBeing;

import java.util.Collection;
import java.util.List;

/**
 * Интерфейс для хранения элементов.
 */

public interface HumanManager {

    /**
     * Сортировка коллекции.
     */

    void sort();

    Collection<HumanBeing> getCollection();

    /**
     * Добавление нового элемента.
     */

    void add(HumanBeing element);

    /**
     * Получение информации о коллекции.
     */

    String getInfo();

    /**
     * Проверяет, содержит ли коллекция элемент с определенным идентификатором.
     */

    boolean checkID(Integer ID);

    /**
     * Удаляет элемент по идентификатору.
     */

    void removeByID(Integer id);

    /**
     * Обновляет элемент по идентификатору.
     */

    void updateByID(Integer id, HumanBeing newElement);


    void clear();

    void removeFirst();

    /**
     * Добавляет элемент, если он больше максимального.
     */

    void addIfMax(HumanBeing element);

    /**
     * Добавляет элемент, если он меньше минимального.
     */

    void addIfMin(HumanBeing element);

    /**
     * Вывод всех элементов, имя которых начинается с подстроки.
     */

    List<HumanBeing> filterStartsWithName(String start);


    /**
     * Вывод всех уникальных значений поля скорости удара.
     */

    List<Integer> getUniqueImpactSpeed();

    /**
     * Преобразование коллекции в JSON.
     */

    void deserializeCollection(String data);

    /**
     * Парсинг коллекции из JSON.
     */

    String serializeCollection();

    double getAverageMinutesOfWaiting();

    HumanBeing getByID(Integer id);

}
