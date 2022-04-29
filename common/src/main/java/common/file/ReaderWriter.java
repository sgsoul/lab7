package common.file;

import common.exceptions.FileException;

public interface ReaderWriter {
    /**
     * Путь к файлу.
     */

    public void setPath(String pth);

    /**
     * Чтение данных.
     */

    public String read() throws FileException;

    /**
     * Сохранение данных.
     */

    public void write(String data) throws FileException;
}
