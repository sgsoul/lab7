package common.io;

import java.util.Scanner;

import java.time.LocalDate;

import common.data.*;
import common.exceptions.IncorrectInputInScriptException;

import static common.io.OutputManager.*;

public class ConsoleInputManager extends InputManagerImpl {

    public ConsoleInputManager() {
        super(new Scanner(System.in));
        getScanner().useDelimiter("\n");
    }

    @Override
    public String readName() {
        return new Question<String>("Введите имя:", super::readName).getAnswer();
    }

    @Override
    public String readFullName() {
        return new Question<String>("Введите фамилию:", super::readFullName).getAnswer();
    }

    @Override
    public double readXCoord() {
        return new Question<Double>("Введите координату х:", super::readXCoord).getAnswer();
    }

    @Override
    public double readYCoord() {
        return new Question<Double>("Введите координату у:", super::readYCoord).getAnswer();
    }

    @Override
    public Coordinates readCoords() {
        print("Введите координаты.");
        double x = readXCoord();
        double y = readYCoord();
        Coordinates coord = new Coordinates(x, y);
        return coord;
    }

    @Override
    public boolean readRealHero() {
        return new Question<Boolean>("Герой реальный? (true/false): ", super::readRealHero).getAnswer();
    }

    @Override
    public boolean readHasToothPick() {
        return new Question<Boolean>("У него есть зубочистка? (true/false):", super::readHasToothPick).getAnswer();
    }

    @Override
    public Integer readImpactSpeed() {
        return new Question<Integer>("Введите скорость удара:", super::readImpactSpeed).getAnswer();
    }


    @Override
    public String readSoundtrackName() {
        return new Question<String>("Введите название песни:", super::readSoundtrackName).getAnswer();
    }

    @Override
    public float readMinutesOfWaiting() {
        return new Question<Float>("Введите количество минут ожидания:", super::readMinutesOfWaiting).getAnswer();
    }

    @Override
    public WeaponType readWeaponType() {
        return new Question<WeaponType>("Введите тип оружия (AXE, PISTOL, SHOTGUN):", super::readWeaponType).getAnswer();
    }

    @Override
    public Car readCar() {
        return new Question<Car>("Введите машину:", super::readCar).getAnswer();
    }

}
