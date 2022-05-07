package common.io;

import common.exceptions.*;

/**
 * Оболочка пользовательского ввода.
 */

public class Question<T> {
    private T answer;

    public Question(String msg, Askable<T> askable) {
        while (true) {
            try {
                System.out.print(msg + " ");
                answer = askable.ask();
                break;
            } catch (InvalidDataException | IncorrectInputInScriptException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public T getAnswer() {
        return answer;
    }
}