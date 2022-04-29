package main;

import common.exceptions.ConnectionException;
import common.exceptions.InvalidPortException;
import common.exceptions.InvalidProgramArgumentsException;
import server.*;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.NoSuchElementException;

import static common.io.OutputManager.print;

/**
 * Основной класс для запуска сервера с аргументами.
 */

public class Main {
    public static void main(String[] args) throws Exception {


        args = new String[]{"4445", "C:\\Users\\Irina\\itmoprog\\lab6\\server\\src\\main\\resources\\humanCollection.json"};
        /*args[0] = "4445";
        args[1] = "C:\\Users\\79006\\OneDrive\\Рабочий стол\\lab6\\server\\humans.json";*/
        int port = 0;
        String strPort = "";
        String path = "";
        try {
            if (args.length >= 2) {
                path = args[1];
                strPort = args[0];
            }
            if (args.length == 1) strPort = args[0];
            if (args.length == 0) throw new InvalidProgramArgumentsException("Адреса не существует.");
            try {
                port = Integer.parseInt(strPort);
            } catch (NumberFormatException e) {
                throw new InvalidPortException();
            }
            Server server = new Server(port, path);
            server.start();
            server.consoleMode();
        }
        catch (InvalidProgramArgumentsException | ConnectionException e){
            print(e.getMessage());
        }
        catch (NoSuchElementException e) {
            print("что за ловушки джокера??");
        }
    }
}
