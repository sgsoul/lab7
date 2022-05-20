package common.io;

import common.auth.User;
import common.data.Car;
import common.data.Coordinates;
import common.data.WeaponType;

import java.util.Scanner;

import static common.io.ConsoleOutputter.print;

public class ConsoleInputManager extends InputManagerImpl {

    public ConsoleInputManager() {
        super(new Scanner(System.in));
        getScanner().useDelimiter("\n");
    }

    @Override
    public String readName() {
        return new Question<>("¬ведите им€:", super::readName).getAnswer();
    }

    @Override
    public String readFullName() {
        return new Question<>("¬ведите фамилию:", super::readFullName).getAnswer();
    }

    @Override
    public double readXCoord() {
        return new Question<>("¬ведите координату х:", super::readXCoord).getAnswer();
    }

    @Override
    public double readYCoord() {
        return new Question<>("¬ведите координату у:", super::readYCoord).getAnswer();
    }

    @Override
    public Coordinates readCoords() {
        print("¬ведите координаты.");
        double x = readXCoord();
        double y = readYCoord();
        return new Coordinates(x, y);
    }

    @Override
    public boolean readRealHero() {
        return new Question<>("√ерой реальный? (true/false): ", super::readRealHero).getAnswer();
    }

    @Override
    public boolean readHasToothPick() {
        return new Question<>("” него есть зубочистка? (true/false):", super::readHasToothPick).getAnswer();
    }

    @Override
    public Integer readImpactSpeed() {
        return new Question<>("¬ведите скорость удара:", super::readImpactSpeed).getAnswer();
    }


    @Override
    public String readSoundtrackName() {
        return new Question<>("¬ведите название песни:", super::readSoundtrackName).getAnswer();
    }

    @Override
    public float readMinutesOfWaiting() {
        return new Question<>("¬ведите количество минут ожидани€:", super::readMinutesOfWaiting).getAnswer();
    }

    @Override
    public WeaponType readWeaponType() {
        return new Question<>("¬ведите тип оружи€ (AXE, PISTOL, SHOTGUN):", super::readWeaponType).getAnswer();
    }

    @Override
    public Car readCar() {
        return new Question<>("¬ведите машину:", super::readCar).getAnswer();
    }

    @Override
    public String readLogin() {
        return new Question<>("¬ведите логин:", super::readLogin).getAnswer();
    }

    @Override
    public String readPassword() {
        return new Question<>("¬ведите пароль:", super::readPassword).getAnswer();
    }

    @Override
    public User readUser() {
        String login = readLogin();
        String password = readPassword();
        return new User(login, password);
    }
}
