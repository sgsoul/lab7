package common.io;

import java.util.Scanner;

import common.auth.User;
import common.connection.CommandMsg;
import common.data.*;
import common.exceptions.*;

public interface InputManager {

    /**
     * Считывает имя из входных данных.
     */

    String readName() throws EmptyStringException;

    /**
     * Считывает полное имя из входных данных.
     */

    String readFullName();

    /**
     * Считывает координату х из входных данных.
     */

    double readXCoord() throws InvalidNumberException;

    /**
     * Считывает координату у из входных данных.
     */

    double readYCoord() throws InvalidNumberException;

    /**
     * Считывает координаты из входных данных.
     */

    Coordinates readCoords() throws InvalidNumberException;

    /**
     * Считывает скорость удара из входных данных.
     */

    Integer readImpactSpeed() throws InvalidNumberException;

    /**
     * Считывает наличие зубочистки в зубах из входных данных.
     */

    boolean readHasToothPick() throws IncorrectInputInScriptException;

    /**
     * Считывает реальный ли герой из входных данных.
     */

    boolean readRealHero() throws IncorrectInputInScriptException;

    /**
     * Считывает тип оружия из входных данных.
     */

    WeaponType readWeaponType() throws InvalidEnumException;

    /**
     * Считывает название песни из входных данных.
     */

    String readSoundtrackName() throws EmptyStringException;

    /**
     * Считывает машину из входных данных.
     */

    Car readCar() throws InvalidDataException;

    /**
     * Считывает количество минут ожидания из входных данных.
     */

    float readMinutesOfWaiting() throws InvalidNumberException;

    /**
     * Считывает человека из входных данных.
     */

    HumanBeing readHuman() throws InvalidDataException;

    /**
     * Считывает пару команда-аргумент из входных данных.
     */

    CommandMsg readCommand();

    /**
     * Получение сканнера.
     */

    Scanner getScanner();

    boolean hasNextLine();


    String readPassword() throws InvalidDataException;

    String readLogin() throws InvalidDataException;

    User readUser() throws InvalidDataException;
}
