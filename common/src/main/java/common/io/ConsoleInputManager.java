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
        return new Question<>("������� ���:", super::readName).getAnswer();
    }

    @Override
    public String readFullName() {
        return new Question<>("������� �������:", super::readFullName).getAnswer();
    }

    @Override
    public double readXCoord() {
        return new Question<>("������� ���������� �:", super::readXCoord).getAnswer();
    }

    @Override
    public double readYCoord() {
        return new Question<>("������� ���������� �:", super::readYCoord).getAnswer();
    }

    @Override
    public Coordinates readCoords() {
        print("������� ����������.");
        double x = readXCoord();
        double y = readYCoord();
        return new Coordinates(x, y);
    }

    @Override
    public boolean readRealHero() {
        return new Question<>("����� ��������? (true/false): ", super::readRealHero).getAnswer();
    }

    @Override
    public boolean readHasToothPick() {
        return new Question<>("� ���� ���� ����������? (true/false):", super::readHasToothPick).getAnswer();
    }

    @Override
    public Integer readImpactSpeed() {
        return new Question<>("������� �������� �����:", super::readImpactSpeed).getAnswer();
    }


    @Override
    public String readSoundtrackName() {
        return new Question<>("������� �������� �����:", super::readSoundtrackName).getAnswer();
    }

    @Override
    public float readMinutesOfWaiting() {
        return new Question<>("������� ���������� ����� ��������:", super::readMinutesOfWaiting).getAnswer();
    }

    @Override
    public WeaponType readWeaponType() {
        return new Question<>("������� ��� ������ (AXE, PISTOL, SHOTGUN):", super::readWeaponType).getAnswer();
    }

    @Override
    public Car readCar() {
        return new Question<>("������� ������:", super::readCar).getAnswer();
    }

    @Override
    public String readLogin() {
        return new Question<>("������� �����:", super::readLogin).getAnswer();
    }

    @Override
    public String readPassword() {
        return new Question<>("������� ������:", super::readPassword).getAnswer();
    }

    @Override
    public User readUser() {
        String login = readLogin();
        String password = readPassword();
        return new User(login, password);
    }
}
