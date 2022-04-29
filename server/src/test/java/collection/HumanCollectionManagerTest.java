package collection;

import common.data.HumanBeing;
import common.data.WeaponType;
import jdk.nashorn.internal.parser.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HumanCollectionManagerTest {
    private HumanCollectionManager humanCollectionManager;

    @BeforeEach
    public void setup() {
        System.out.println("Instantiating Human Collection Manager");
        humanCollectionManager = new HumanCollectionManager();
    }


    @Test
    @DisplayName("Проверка метода сортировки коллекции")
    void sort() {
        HumanCollectionManager rightCollection = new HumanCollectionManager();
         HumanBeing humanFirst = new HumanBeing(1, "name1", 2, 3, true,true,
                80, "song1", 50, WeaponType.AXE, "car1", true);
        HumanBeing humanSecond = new HumanBeing(2, "name2", 4, 5, false,false,
                90, "song2", 60, WeaponType.PISTOL, "car2", true);
        HumanBeing humanThird = new HumanBeing(3, "name3", 6, 7, true,false,
                70, "song3", 70, WeaponType.SHOTGUN, "car3", true);
        rightCollection.add(humanFirst);
        rightCollection.add(humanSecond);
        rightCollection.add(humanThird);
        humanCollectionManager.add(humanFirst);
        humanCollectionManager.add(humanThird);
        humanCollectionManager.add(humanSecond);
        humanCollectionManager.sort();
        List<HumanBeing> list1 = rightCollection.getCollection();
        List<HumanBeing> list2 = humanCollectionManager.getCollection();
        assertArrayEquals(list1.toArray(), list2.toArray());
        assertFalse(humanCollectionManager.getCollection().isEmpty());
        assertEquals(3, humanCollectionManager.getCollection().size());
    }

    @Test
    @DisplayName("Проверка метода добавления нового элемента в коллекцию")
    void add() {
        HumanBeing humanFirst = new HumanBeing(1, "name1", 2, 3, true,true,
                80, "song1", 50, WeaponType.AXE, "car1", true);
        humanCollectionManager.add(humanFirst);
        assertFalse(humanCollectionManager.getCollection().isEmpty());
        assertEquals(1, humanCollectionManager.getCollection().size());
    }

    @Test
    @DisplayName("Проверка метода на подтверждение присутсвия элемента с заданным ID в коллекции")
    void checkID() {
         HumanBeing humanFirst = new HumanBeing(1, "name1", 2, 3, true,true,
                80, "song1", 50, WeaponType.AXE, "car1", true);
        HumanBeing humanSecond = new HumanBeing(2, "name2", 4, 5, false,false,
                90, "song2", 60, WeaponType.PISTOL, "car2", true);
        HumanBeing humanThird = new HumanBeing(3, "name3", 6, 7, true,false,
                70, "song3", 70, WeaponType.SHOTGUN, "car3", true);
        humanCollectionManager.add(humanFirst);
        humanCollectionManager.add(humanSecond);
        humanCollectionManager.add(humanThird);
        assertFalse(humanCollectionManager.getCollection().isEmpty());
        assertEquals(3, humanCollectionManager.getCollection().size());
        assertTrue(humanCollectionManager.checkID(2));
        assertFalse(humanCollectionManager.checkID(4));
    }

    @Test
    @DisplayName("Проверка метода удаления элемента из коллекции по выбранному полю ID")
    void removeByID() {
         HumanBeing humanFirst = new HumanBeing(1, "name1", 2, 3, true,true,
                80, "song1", 50, WeaponType.AXE, "car1", true);
        HumanBeing humanSecond = new HumanBeing(2, "name2", 4, 5, false,false,
                90, "song2", 60, WeaponType.PISTOL, "car2", true);
        HumanBeing humanThird = new HumanBeing(3, "name3", 6, 7, true,false,
                70, "song3", 70, WeaponType.SHOTGUN, "car3", true);
        humanCollectionManager.add(humanFirst);
        humanCollectionManager.add(humanSecond);
        humanCollectionManager.add(humanThird);
        humanCollectionManager.removeByID(3);
        assertFalse(humanCollectionManager.getCollection().isEmpty());
        assertEquals(2, humanCollectionManager.getCollection().size());
        assertTrue(humanCollectionManager.getCollection().stream()
                .anyMatch(humanBeing -> humanBeing.getName().equals("name2")));
        assertTrue(humanCollectionManager.getCollection().stream()
                .noneMatch(humanBeing -> humanBeing.getName().equals("name3")));
    }

    @Test
    @DisplayName("Проверка метода ")
    void updateByID() {
    }

    @Test
    @DisplayName("Проверка метода получения размера коллекции")
    void getSize() {
        HumanBeing humanFirst = new HumanBeing(1, "name1", 2, 3, true,true,
                80, "song1", 50, WeaponType.AXE, "car1", true);
        HumanBeing humanSecond = new HumanBeing(2, "name2", 4, 5, false,false,
                90, "song2", 60, WeaponType.PISTOL, "car2", true);
        humanCollectionManager.add(humanFirst);
        humanCollectionManager.add(humanSecond);
        assertFalse(humanCollectionManager.getCollection().isEmpty());
        assertEquals(2, humanCollectionManager.getCollection().size());
    }

    @Test
    @DisplayName("Проверка метода очистки коллекции")
    void clear() {
        HumanBeing humanFirst = new HumanBeing(1, "name1", 2, 3, true,true,
                80, "song1", 50, WeaponType.AXE, "car1", true);
        HumanBeing humanSecond = new HumanBeing(2, "name2", 4, 5, false,false,
                90, "song2", 60, WeaponType.PISTOL, "car2", true);
        humanCollectionManager.add(humanFirst);
        humanCollectionManager.add(humanSecond);
        humanCollectionManager.clear();
        assertTrue(humanCollectionManager.getCollection().isEmpty());
        assertEquals(0, humanCollectionManager.getCollection().size());
    }

    @Test
    @DisplayName("Проверка метода удаления первого элемента в коллекции")
    void removeFirst() {
        HumanBeing humanFirst = new HumanBeing(1, "name1", 2, 3, true,true,
                80, "song1", 50, WeaponType.AXE, "car1", true);
        HumanBeing humanSecond = new HumanBeing(2, "name2", 4, 5, false,false,
                90, "song2", 60, WeaponType.PISTOL, "car2", true);
        humanCollectionManager.add(humanFirst);
        humanCollectionManager.add(humanSecond);
        humanCollectionManager.removeFirst();
        assertFalse(humanCollectionManager.getCollection().isEmpty());
        assertEquals(1, humanCollectionManager.getCollection().size());
        assertTrue(humanCollectionManager.getCollection().stream()
                .anyMatch(human -> human.getName().equals("name2")));
        assertTrue(humanCollectionManager.getCollection().stream()
                .noneMatch(human -> human.getName().equals("name1")));
    }

    @Test
    @DisplayName("Проверка метода добаваления нового элемента в коллекцию при условии, что его значения поля " +
            "скорость удара больлше максимального значения в колекции")
    void addIfMax() {
        HumanBeing humanFirst = new HumanBeing(1, "name1", 2, 3, true,true,
                80, "song1", 50, WeaponType.AXE, "car1", true);
        HumanBeing humanSecond = new HumanBeing(2, "name2", 4, 5, false,false,
                90, "song2", 60, WeaponType.PISTOL, "car2", true);
        HumanBeing humanThird = new HumanBeing(3, "name3", 6, 7, true,false,
                70, "song3", 70, WeaponType.SHOTGUN, "car3", true);
        humanCollectionManager.add(humanFirst);
        humanCollectionManager.addIfMax(humanSecond);
        humanCollectionManager.addIfMax(humanThird);
        assertFalse(humanCollectionManager.getCollection().isEmpty());
        assertEquals(2, humanCollectionManager.getCollection().size());
        assertTrue(humanCollectionManager.getCollection().stream()
                .anyMatch(humanBeing -> humanBeing.getName().equals("name2")));
        assertTrue(humanCollectionManager.getCollection().stream()
                .noneMatch(humanBeing -> humanBeing.getName().equals("name3")));
    }

    @Test
    @DisplayName("Проверка метода добаваления нового элемента в коллекцию при условии, что его значения поля " +
            "скорость удара меньше минимального значения в колекции")
    void addIfMin() {
        HumanBeing humanFirst = new HumanBeing(1, "name1", 2, 3, true,true,
                80, "song1", 50, WeaponType.AXE, "car1", true);
        HumanBeing humanSecond = new HumanBeing(2, "name2", 4, 5, false,false,
                90, "song2", 60, WeaponType.PISTOL, "car2", true);
        HumanBeing humanThird = new HumanBeing(3, "name3", 6, 7, true,false,
                70, "song3", 70, WeaponType.SHOTGUN, "car3", true);
        humanCollectionManager.add(humanFirst);
        humanCollectionManager.addIfMin(humanSecond);
        humanCollectionManager.addIfMin(humanThird);
        assertFalse(humanCollectionManager.getCollection().isEmpty());
        assertEquals(2, humanCollectionManager.getCollection().size());
        assertTrue(humanCollectionManager.getCollection().stream()
                .anyMatch(humanBeing -> humanBeing.getName().equals("name3")));
        assertTrue(humanCollectionManager.getCollection().stream()
                .noneMatch(humanBeing -> humanBeing.getName().equals("name2")));
    }

    @Test
    @DisplayName("Проверка метода ")
    void filterStartsWithName() {
    }

    @Test
    @DisplayName("Проверка метода генерации уникального списка значений по полю скорость удара")
    void getUniqueImpactSpeed() {
        HumanBeing humanFirst = new HumanBeing(1, "name1", 2, 3, true,true,
                80, "song1", 50, WeaponType.AXE, "car1", true);
        HumanBeing humanSecond = new HumanBeing(2, "name2", 4, 5, false,false,
                90, "song2", 60, WeaponType.PISTOL, "car2", true);
        humanCollectionManager.add(humanFirst);
        humanCollectionManager.add(humanSecond);
        List<Integer> list1 = asList(80,90);
        List<Integer> list2 = humanCollectionManager.getUniqueImpactSpeed();
        assertArrayEquals(list1.toArray(), list2.toArray());
        assertFalse(humanCollectionManager.getCollection().isEmpty());
        assertEquals(2, humanCollectionManager.getCollection().size());
    }

    @Test
    @DisplayName("Проверка метода возвращения среднего значения по полю время ожидания у элементов в коллекции")
    void getAverageMinutesOfWaiting() {
        HumanBeing humanFirst = new HumanBeing(1, "name1", 2, 3, true,true,
                80, "song1", 50, WeaponType.AXE, "car1", true);
        HumanBeing humanSecond = new HumanBeing(2, "name2", 4, 5, false,false,
                90, "song2", 60, WeaponType.PISTOL, "car2", true);
        humanCollectionManager.add(humanFirst);
        humanCollectionManager.add(humanSecond);
        humanCollectionManager.getAverageMinutesOfWaiting();
        assertFalse(humanCollectionManager.getCollection().isEmpty());
        assertEquals(55, humanCollectionManager.getAverageMinutesOfWaiting());
    }
}