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
        args = new String[]{"5432", "localhost", "postgres", "s"};
        int port = 0;
        String strPort = "5432";
        String dbHost = "localhost";
        String user = "postgres";
        String password = "arina";
        String url = "jdbc:postgresql://" + dbHost + ":5432/postgres";

        try {
            if (args.length == 4) {
                strPort = args[0];
                dbHost = args[1];
                user = args[2];
                password = args[3];
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
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (server.isRunning()) server.close();
            }, "shutdown thread"));
        } catch (ConnectionException | DatabaseException e) {
            Log.logger.error(e.getMessage());
        }
    }
}

