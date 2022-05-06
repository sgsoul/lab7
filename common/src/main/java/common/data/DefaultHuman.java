package common.data;

import java.util.Date;

public class DefaultHuman extends HumanBeing {
    public DefaultHuman(String name, Coordinates coordinates, Boolean realHero, Boolean hasToothPick, Integer impactSpeed, String soundtrackName, float minutesOfWaiting, WeaponType weaponType, Car car) {
        super(name, coordinates, realHero, hasToothPick, impactSpeed, soundtrackName, minutesOfWaiting, weaponType, car);
        setCreationDate(new Date());
        setUserLogin("");
    }
}

