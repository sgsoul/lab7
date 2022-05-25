package common.io;

import common.auth.User;
import common.connection.CommandMsg;
import common.data.*;
import common.exceptions.EmptyStringException;
import common.exceptions.InvalidDataException;
import common.exceptions.InvalidEnumException;
import common.exceptions.InvalidNumberException;

import java.util.Scanner;

/**
 * Реализация диспетчера ввода.
 */

public abstract class InputManagerImpl implements InputManager {
    private Scanner scanner;

    public InputManagerImpl(Scanner scanner) {
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
        String s = read();
        if (s.equals("")) {
            throw new EmptyStringException("[NameEmptyException] имя не может быть пустым");
        }
        if (s.length() > 1000) {
            throw new EmptyStringException("хватит баловаться");
        }
        return s;
    }

    public String readFullName() {
        String s = read();
        if (s.equals("")) {
            return null;
        }
        return s;
    }

    public double readXCoord() throws InvalidNumberException {
        double x;
        try {
            x = Double.parseDouble(read());
        } catch (NumberFormatException e) {
            throw new InvalidNumberException("[XCoordFormatException] X цифра!");
        }
        if (Double.isInfinite(x) || Double.isNaN(x)) throw new InvalidNumberException("[XCoordFormatException] X цифра!");
        return x;
    }

    public double readYCoord() throws InvalidNumberException {
        double y;
        try {
            y = Double.parseDouble(read());
        } catch (NumberFormatException e) {
            throw new InvalidNumberException();
        }
        if (Double.isInfinite(y) || Double.isNaN(y)) throw new InvalidNumberException("не double");
        return y;
    }

    public Coordinates readCoords() throws InvalidNumberException {
        double x = readXCoord();
        double y = readYCoord();
        Coordinates coord = new Coordinates(x, y);
        return coord;
    }

    public Integer readImpactSpeed() throws InvalidNumberException {
        int s;
        try {
            s = Integer.parseInt(read());
        } catch (NumberFormatException e) {
            throw new InvalidNumberException();
        }
        return s;
    }

    public WeaponType readWeaponType() throws InvalidEnumException {
        String s = read();
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
        String soundtrackName = read();
        if (soundtrackName.equals("")) {
            throw new EmptyStringException();
        }
        if (soundtrackName.length() > 1000) {
            throw new EmptyStringException("хватит баловаться");
        }
        return soundtrackName;
    }

    //todo тут сделала другой вывод car (add cool check=true)
    public Car readCar() {
        //Car car = null;
        String name = read();
        if (name.equals("")) {
            return null;
        }
        return new Car(name, true);
    }

    public float readMinutesOfWaiting() throws InvalidNumberException {
        float minutesOfWaiting;
        try {
            minutesOfWaiting = Float.parseFloat(read());
        } catch (NumberFormatException e) {
            throw new InvalidNumberException();
        }
        return minutesOfWaiting;
    }

    public boolean readRealHero() /*throws EmptyStringException*/ { /*InvalidDataException*/
        String strRealHero;
        boolean realHero;
        strRealHero = read().toLowerCase();
        realHero = strRealHero.equalsIgnoreCase("yes") || strRealHero.equalsIgnoreCase("да");
        /*if (strRealHero.equals("")){
            throw new EmptyStringException();
        };*/
        return realHero;
    }

    public boolean readHasToothPick() /*throws InvalidDataException*/ {
        String strHasToothpick;
        boolean hasToothpick;
        strHasToothpick = read().toLowerCase();
        hasToothpick = strHasToothpick.equalsIgnoreCase("yes") || strHasToothpick.equalsIgnoreCase("да");
        return hasToothpick;
    }

    public HumanBeing readHuman() throws InvalidDataException {
        HumanBeing human;

        String name = readName();
        Coordinates coords = readCoords();
        Boolean realHero = readRealHero();
        Boolean hasToothpick = readHasToothPick();
        Integer impactSpeed = readImpactSpeed();
        String soundtrackName = readSoundtrackName();
        float minutesOfWaiting = readMinutesOfWaiting();
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

    public CommandMsg readCommand() throws InvalidDataException {
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

            human = readHuman();
        } else if (cmd.equals("login") || cmd.equals("register")) {
            user = readUser();

            return new CommandMsg(cmd, null, null, user);
        }
        return new CommandMsg(cmd, arg, human);
    }
}
