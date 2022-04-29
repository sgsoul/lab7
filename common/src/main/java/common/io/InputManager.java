package common.io;

import java.util.Scanner;

import common.connection.CommandMsg;
import common.data.*;
import common.exceptions.*;

public interface InputManager {

    /**
     * Считывает имя из входных данных.
     */

    public String readName() throws EmptyStringException;

    /**
     * Считывает полное имя из входных данных.
     */

    public String readFullName();

    /**
     * Считывает координату х из входных данных.
     */

    public double readXCoord() throws InvalidNumberException;

    /**
     * Считывает координату у из входных данных.
     */

    public double readYCoord() throws InvalidNumberException;

    /**
     * Считывает координаты из входных данных.
     */

    public Coordinates readCoords() throws InvalidNumberException;

    /**
     * Считывает скорость удара из входных данных.
     */

    public Integer readImpactSpeed() throws InvalidNumberException;

    /**
     * Считывает наличие зубочистки в зубах из входных данных.
     */

    public boolean readHasToothPick() throws IncorrectInputInScriptException;

    /**
     * Считывает реальный ли герой из входных данных.
     */

    public boolean readRealHero() throws IncorrectInputInScriptException;

    /**
     * Считывает тип оружия из входных данных.
     */

    public WeaponType readWeaponType() throws InvalidEnumException;

    /**
     * Считывает название песни из входных данных.
     */

    public String readSoundtrackName() throws EmptyStringException;

    /**
     * Считывает машину из входных данных.
     */

    public Car readCar() throws InvalidDataException;

    /**
     * Считывает количество минут ожидания из входных данных.
     */

    public float readMinutesOfWaiting() throws InvalidNumberException;

    /**
     * Считывает человека из входных данных.
     */

    public HumanBeing readHuman() throws InvalidDataException;

    /**
     * Считывает пару команда-аргумент из входных данных.
     */

    public CommandMsg readCommand();

    /**
     * Получение сканнера.
     */

    public Scanner getScanner();
}
