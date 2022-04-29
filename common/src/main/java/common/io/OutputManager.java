package common.io;

import java.nio.charset.StandardCharsets;

/**
 * Класс со статическими методами для печати.
 */

public interface OutputManager {
    public static void print(Object o){
        System.out.println(o.toString());
    }
    public static void printErr(Object o){
        System.out.println("Error: " + o.toString());
    }
}
