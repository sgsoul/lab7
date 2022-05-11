package main;

import common.exceptions.*;
import server.*;

import log.Log;


import java.io.PrintStream;

import java.util.Properties;


/**
 * Основной класс для запуска сервера с аргументами.
 */

public class Main {
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        args = new String[]{"5432", "localhost"};
        /*args[0] = "4445";
        args[1] = "C:\\Users\\79006\\OneDrive\\Рабочий стол\\lab6\\server\\humans.json";*/
        int port = 0;
        String strPort = "5432";
        //String path = "";
        String dbHost = "localhost";
        String user = "postgres";
        String password = "qwerty";
        String url = "jdbc:postgresql://" + dbHost + ":5432/postgres";

        try {
            if (args.length >= 4) {
                //path = args[1];
                strPort = args[0];
                dbHost = args[1];
                user = args[2];
                password = args[3];
                url = "jdbc:postgresql://" + dbHost + ":5432/postgres";
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
//        } catch (NoSuchElementException e) {
//            print("что за ловушки джокера??");
        } catch (ConnectionException | DatabaseException e) {
            Log.logger.error(e.getMessage());
        }

    }
}

