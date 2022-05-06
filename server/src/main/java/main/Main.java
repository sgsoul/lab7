package main;

import common.exceptions.*;
import database.ConnectionManager;
import org.postgresql.Driver;
import server.*;

import java.awt.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Properties;

import static common.io.OutputManager.print;

/**
 * Основной класс для запуска сервера с аргументами.
 */

public class Main {
    public static void main(String[] args) throws SQLException, FileException, InvalidDataException {

        args = new String[]{"4445", "C:\\Users\\Irina\\itmoprog\\lab6\\server\\src\\main\\resources\\humanCollection.json"};
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
            if (args.length == 0) throw new InvalidProgramArgumentsException("Адреса не существует.");
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
        } catch (InvalidProgramArgumentsException | ConnectionException e) {
            print(e.getMessage());
        } catch (NoSuchElementException e) {
            print("что за ловушки джокера??");
        }

    }
}
