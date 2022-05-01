package common.io;

import java.util.Scanner;

import common.auth.User;
import common.connection.CommandMsg;

import java.time.LocalDate;

import common.data.*;
import common.exceptions.*;

/**
 * Реализация диспетчера ввода.
 */

public abstract class InputManagerImpl implements InputManager {
    private Scanner scanner;

    public InputManagerImpl(Scanner sc) {
        this.scanner = scanner;
    }

    private String read() {
        return scanner.nextLine();
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner sc) {
        scanner = sc;
    }

    public String readName() throws EmptyStringException {
        String s = scanner.nextLine().trim();
        if (s.equals("")) {
            throw new EmptyStringException();
        }
        return s;
    }

    public String readFullName() {
        String s = scanner.nextLine().trim();
        if (s.equals("")) {
            return null;
        }
        return s;
    }

    public double readXCoord() throws InvalidNumberException {
        double x;
        try {
            x = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidNumberException();
        }
        if (Double.isInfinite(x) || Double.isNaN(x)) throw new InvalidNumberException("не double");
        return x;
    }

    public double readYCoord() throws InvalidNumberException {
        double y;
        try {
            y = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidNumberException();
        }
        if (Double.isInfinite(y) || Double.isNaN(y)) throw new InvalidNumberException("не double");
        return y;
    }

    public Coordinates readCoords() throws InvalidNumberException {
        Double x = readXCoord();
        Double y = readYCoord();
        Coordinates coord = new Coordinates(x, y);
        return coord;
    }

    public Integer readImpactSpeed() throws InvalidNumberException {
        Integer s;
        try {
            s = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new InvalidNumberException();
        }
        return s;
    }

    public WeaponType readWeaponType() throws InvalidEnumException {
        String s = scanner.nextLine().trim();
        if (s.equals("")) {
            return null;
        } else {
            try {
                return WeaponType.valueOf(s);
            } catch (IllegalArgumentException e) {
                throw new InvalidEnumException();
            }
        }
    }

    public String readSoundtrackName() throws EmptyStringException {
        String soundtrackName = scanner.nextLine().trim();
        if (soundtrackName.equals("")) {
            throw new EmptyStringException();
        }
        return soundtrackName;
    }

    //todo тут сделала другой вывод car (add cool check=true)
    public Car readCar() throws InvalidDataException {
        //Car car = null;
        String name = scanner.nextLine().trim();
        if (name.equals("")) {
            return null;
        }
        Car car = new Car(name, true);
        return car;
    }

    public float readMinutesOfWaiting() throws InvalidNumberException {
        float minutesOfWaiting;
        try {
            minutesOfWaiting = Float.parseFloat(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new InvalidNumberException();
        }
        return minutesOfWaiting;
    }

    //todo тут первые 2 ошибки
    public boolean readRealHero() /*throws EmptyStringException*/ { /*InvalidDataException*/
        String strRealHero;
        boolean realHero;
        strRealHero = scanner.nextLine().trim().toLowerCase();
        if (strRealHero.equalsIgnoreCase("yes") || strRealHero.equalsIgnoreCase("да")) {
            realHero = true;
        } else realHero = false;
        /*if (strRealHero.equals("")){
            throw new EmptyStringException();
        };*/
        return realHero;
    }

    //todo третья ошибка тут
    public boolean readHasToothPick() /*throws InvalidDataException*/ {
        String strHasToothpick;
        boolean hasToothpick;
        strHasToothpick = scanner.nextLine().trim().toLowerCase();
        if (strHasToothpick.equalsIgnoreCase("yes") || strHasToothpick.equalsIgnoreCase("да")) {
            hasToothpick = true;
        } else hasToothpick = false;
        return hasToothpick;
    }

    public HumanBeing readHuman() throws InvalidDataException {
        HumanBeing human = null;

        String name = readName();
        Coordinates coords = readCoords();
        Boolean realHero = readRealHero();
        Boolean hasToothpick = readHasToothPick();
        Integer impactSpeed = readImpactSpeed();
        String soundtrackName = readSoundtrackName();
        Float minutesOfWaiting = readMinutesOfWaiting();
        WeaponType weaponType = readWeaponType();
        Car car = readCar();
        human = new DefaultHuman(name, coords, realHero, hasToothpick, impactSpeed, soundtrackName, minutesOfWaiting, weaponType, car);

        return human;

    }

    public String readPassword() throws InvalidDataException {
        String s = read();
        if (s.equals("")) throw new EmptyStringException();
        return s;
    }

    public String readLogin() throws InvalidDataException {
        String s = read();
        if (s.equals("")) throw new EmptyStringException();
        return s;
    }

    public User readUser() throws InvalidDataException {
        return new User(readPassword(), readLogin());
    }

    public CommandMsg readCommand() {
        String cmd = read();
        String arg = null;
        HumanBeing human = null;
        User user = null;
        if (cmd.contains(" ")) {
            String[] arr = cmd.split(" ", 2);
            cmd = arr[0];
            arg = arr[1];
        }
        if (cmd.equals("add") || cmd.equals("add_if_min") || cmd.equals("add_if_max") || cmd.equals("update")) {
            try {
                human = readHuman();
            } catch (InvalidDataException ignored) {
            }
        } else if (cmd.equals("login") || cmd.equals("register")) {
            try {
                user = readUser();
            } catch (InvalidDataException ignored) {
            }
            return new CommandMsg(cmd, null, null, user);
        }
        return new CommandMsg(cmd, arg, human);
    }
}
