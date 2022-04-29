package common.io;

import common.exceptions.*;

/**
 * Вызов пользовательского ввода
 */

@FunctionalInterface
public interface Askable<T> {
    T ask() throws InvalidDataException, IncorrectInputInScriptException;
}