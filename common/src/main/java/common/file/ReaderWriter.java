package common.file;

import common.exceptions.FileException;

public interface ReaderWriter {
    /**
     * Путь к файлу.
     */

    void setPath(String pth);

    /**
     * Чтение данных.
     */

    String read() throws FileException;

    /**
     * Сохранение данных.
     */

    void write(String data) throws FileException;
}
