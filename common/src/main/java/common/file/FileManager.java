package common.file;

import common.exceptions.*;

import java.io.*;

/**
 * Управляет основной коллекцией для сохранения или загрузки.
 */

public class FileManager implements ReaderWriter {
    private String path;

    public FileManager(String pth) {
        path = pth;
    }

    public void setPath(String pth) {
        path = pth;
    }

    public FileManager() {
        path = null;
    }

    public String read() throws FileException {
        String str = "";
        try {
            if (path == null) throw new EmptyPathException();
            InputStreamReader reader = null;

            File file = new File(path);
            if (!file.exists()) throw new FileNotExistsException();
            if (!file.canRead()) throw new FileWrongPermissionsException("Не удаётся открыть файл.");
            InputStream inputStream = new FileInputStream(file);
            reader = new InputStreamReader(inputStream);
            int currectSymbol;
            while ((currectSymbol = reader.read()) != -1) {
                str += ((char) currectSymbol);
            }
            reader.close();
        } catch (IOException e) {
            throw new FileException("Не удаётся открыть файл.");
        }
        return str;
    }

    private void create(File file) throws CannotCreateFileException {
        try {
            boolean success = file.createNewFile();
            if (!success) throw new CannotCreateFileException();
        } catch (IOException e) {
            throw new CannotCreateFileException();
        }
    }

    public void write(String str) throws FileException {
        try {
            if (path == null) throw new EmptyPathException();
            File file = new File(path);

            if (!file.exists()) {
                create(file);
            }
            ;
            if (!file.canWrite()) throw new FileWrongPermissionsException("Не удаётся записать файл.");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            throw new FileException("Не удаётся получить доступ к файлу.");
        }
    }
}