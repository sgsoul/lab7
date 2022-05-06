package common.io;


/**
 * Класс со статическими методами для печати.
 */

public interface OutputManager {
    static void print(Object o) {
        System.out.println(o.toString());
    }

    static void printErr(Object o) {
        System.out.println("Error: " + o.toString());
    }
}
