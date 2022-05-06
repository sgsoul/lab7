package main;

import common.exceptions.ConnectionException;
import common.exceptions.DatabaseException;
import common.exceptions.InvalidPortException;
import log.Log;
import server.*;

import java.io.PrintStream;
import java.util.Properties;


/**
 * Основной класс для запуска сервера с аргументами.
 */

public class Main {
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        args = new String[]{"4445", "localhost"};
        int port = 0;
        String strPort = "4445";
        String dbHost = "pg";
        String user = "s340384";
        String password = "lolkek";
        String url = "jdbc:postgresql://" + dbHost + ":5432/studs"; // использование базы данной

        try {
            if (args.length == 4) {
                strPort = args[0];
                dbHost = args[1];
                user = args[2];
                password = args[3];
                url = "jdbc:postgresql://" + dbHost + ":5432/studs";
            }
            if (args.length == 1) strPort = args[0];
            if (args.length == 0) Log.logger.info("Нет порта, переданного аргументом, размещенного на " + strPort);
            try {
                port = Integer.parseInt(strPort);
            } catch (NumberFormatException e) {
                throw new InvalidPortException();
            }
            Properties settings = new Properties();
            settings.setProperty("url", url);
            settings.setProperty("user", user);
            settings.setProperty("password", password);
            Server server = new Server(port, settings);

            server.start();
            server.consoleMode();

        } catch (ConnectionException | DatabaseException e) {
            Log.logger.error(e.getMessage());
        }
    }
}

