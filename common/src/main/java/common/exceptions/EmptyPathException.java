package common.exceptions;

/**
 * Выбрасывается, когда путь к файлу пуст
 */

public class EmptyPathException extends FileException{
    public EmptyPathException(){
        super("Пусть к файлу пуст.");
    }
}
