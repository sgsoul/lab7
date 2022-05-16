package common.data;

import common.auth.User;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * Класс HumanBeing.
 */

public class HumanBeing implements Collectionable, Serializable {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final Boolean realHero; //Поле не может быть null
    private final Boolean hasToothpick; //Поле не может быть null
    private final Integer impactSpeed; //Поле не может быть null
    private final String soundtrackName; //Поле не может быть null
    private final float minutesOfWaiting;
    private final WeaponType weaponType; //Поле не может быть null
    private final Car car; //Поле не может быть null
    private String userLogin;

    /**
     * New human
     *
     * @param name             name
     * @param coordinates      coordinates
     * @param realHero         realHero
     * @param hasToothpick     hasToothpick
     * @param impactSpeed      impactSpeed
     * @param soundtrackName   soundTrackName
     * @param minutesOfWaiting minutesOfWaiting
     * @param weaponType       weaponType
     * @param car              car
     */

    public HumanBeing(String name, Coordinates coordinates,
                      Boolean realHero, Boolean hasToothpick, Integer impactSpeed, String soundtrackName,
                      float minutesOfWaiting, WeaponType weaponType, Car car) {

        this.name = name;
        this.coordinates = coordinates;
        this.realHero = realHero;
        this.hasToothpick = hasToothpick;
        this.impactSpeed = impactSpeed;
        this.soundtrackName = soundtrackName;
        this.minutesOfWaiting = minutesOfWaiting;
        this.weaponType = weaponType;
        this.car = car;
    }

    //todo wtf
    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String login) {
        userLogin = login;
    }

    public void setUser(User usr) {
        userLogin = usr.getLogin();
    }

    /**
     * Компаратор для сортировки.
     */

    public static class SortingComparator implements Comparator<HumanBeing> {
        public int compare(HumanBeing first, HumanBeing second) {
            int result = Double.compare(first.getCoordinates().getX(), second.getCoordinates().getX());
            if (result == 0) {
                result = Double.compare(first.getCoordinates().getY(), second.getCoordinates().getY());
            }
            return result;
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int ID) {
        id = ID;
    }

    public String getName() {
        return name;
    }

    public void setCreationDate(Date date) {
        creationDate = date;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Boolean checkRealHero() {
        return realHero;
    }

    /**
     * Method to check hasToothpick
     *
     * @return hasToothpick
     */
    public Boolean checkHasToothpick() {
        return hasToothpick;
    }

    /**
     * Method to get impactSpeed
     *
     * @return impactSpeed
     */
    public Integer getImpactSpeed() {
        return impactSpeed;
    }

    /**
     * Method to get soundTrackName
     *
     * @return soundTrackName
     */
    public String getSoundtrackName() {
        return soundtrackName;
    }

    /**
     * Method to get minutesOfWaiting
     *
     * @return minutesOfWaiting
     */
    public float getMinutesOfWaiting() {
        return minutesOfWaiting;
    }

    /**
     * Method to get weaponType
     *
     * @return weaponType
     */
    public WeaponType getWeaponType() {
        return weaponType;
    }

    /**
     * Method to get car
     *
     * @return car
     */
    public Car getCar() {
        return car;
    }


    /**
     * @return String
     */

    @Override
    public String toString() {
        return "id: " + id + "\n name: " + name + "\n coordinates: " + coordinates + "\n creationDate: " + creationDate +
                "\n realHero: " + realHero + "\n hasToothpick: " + hasToothpick + "\n impactSpeed: " + impactSpeed +
                "\n soundtrackName: " + soundtrackName + "\n minutesOfWaiting: " + minutesOfWaiting +
                "\n weaponType: " + weaponType + "\n Car: " + car + "\n\n";
    }

    /**
     * @return boolean
     */

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        HumanBeing another = (HumanBeing) obj;
        return this.getId() == another.getId();
    }


    /**
     * @return int
     */

    public int compareTo(Collectionable human) {
        return Integer.compare(impactSpeed, human.getImpactSpeed());
    }

    @Override
    public boolean validate() {
        return (
                coordinates != null && coordinates.validate() &&
                        (car == null) &&
                        (impactSpeed > 0) && (id > 0) &&
                        name != null && !name.equals("") &&
                        weaponType != null &&
                        creationDate != null
        );
    }

}
