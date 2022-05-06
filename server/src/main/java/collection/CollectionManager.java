package collection;

import java.util.List;
import java.util.Vector;

/**
 * Интерфейс для хранения элементов.
 */

public interface CollectionManager<T> {

    /**
     * Генерирует новый уникальный идентификатор для коллекции.
     */

    public int generateNextId();

    /**
     * Сортировка коллекции.
     */

    public void sort();

    public Vector<T> getCollection();

    /**
     * Добавление нового элемента.
     */

    public void add(T element);

    /**
     * Получение информации о коллекции.
     */

    public String getInfo();

    /**
     * Проверяет, содержит ли коллекция элемент с определенным идентификатором.
     */

    public boolean checkID(Integer ID);

    /**
     * Удаляет элемент по идентификатору.
     * @return
     */

    public boolean removeByID(Integer id);

    /**
     * Обновляет элемент по идентификатору.
     */

    public boolean updateByID(Integer id, T newElement);

    /**
     * Получение размера коллекции.
     */

    public int getSize();

    public void clear();

    public void removeFirst();

    /**
     * Добавляет элемент, если он больше максимального.
     */

    public boolean addIfMax(T element);

    /**
     * Добавляет элемент, если он меньше минимального.
     */

    public boolean addIfMin(T element);

    /**
     * Вывод всех элементов, имя которых начинается с подстроки.
     */

    public List<T> filterStartsWithName(String start);


    /**
     * Вывод всех уникальных значений поля скорости удара.
     */

    public List<Integer> getUniqueImpactSpeed();

    /**
     * Преобразование коллекции в JSON.
     */

    public boolean deserializeCollection(String json);

    /**
     * Парсинг коллекции из JSON.
     */

    public String serializeCollection();

    public double getAverageMinutesOfWaiting();
}
