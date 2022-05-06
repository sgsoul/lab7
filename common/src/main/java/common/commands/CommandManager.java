package common.commands;

import static common.io.OutputManager.*;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import common.connection.*;
import common.connection.Status;
import common.exceptions.*;
import common.io.*;

public abstract class CommandManager implements Commandable, Closeable {
    private Map<String, Command> map;
    private InputManager inputManager;
    private boolean isRunning;
    private String currentScriptFileName;
    private static Stack<String> callStack = new Stack<>();

    public void clearStack() {
        callStack.clear();
    }

    public Stack<String> getStack() {
        return callStack;
    }

    public String getCurrentScriptFileName() {
        return currentScriptFileName;
    }

    public CommandManager() {
        isRunning = false;
        currentScriptFileName = "";
        map = new HashMap<String, Command>();
    }

    public void addCommand(Command c) {
        map.put(c.getName(), c);
    }

    public void addCommand(String key, Command c) {
        map.put(key, c);
    }

    public Command getCommand(String s) {
        if (!hasCommand(s)) throw new NoSuchCommandException();
        Command cmd = map.get(s);
        return cmd;
    }

    public boolean hasCommand(String s) {
        return map.containsKey(s);
    }

    public void consoleMode() throws FileException, InvalidDataException, ConnectionException {
        inputManager = new ConsoleInputManager();
        isRunning = true;
        while (isRunning) {
            print(new String("Введите команду. 'help' - список команд: ".getBytes(), StandardCharsets.UTF_8));
            CommandMsg commandMsg = inputManager.readCommand();
            Response answerMsg = runCommand(commandMsg);
            if (answerMsg.getStatus().equals(Status.EXIT)) {
                close();
            }
        }
    }

    public void fileMode(String path) throws FileException, InvalidDataException, ConnectionException {
        currentScriptFileName = path;
        inputManager = new FileInputManager(path);
        isRunning = true;
        while (isRunning && inputManager.getScanner().hasNextLine()) {
            CommandMsg commandMsg = inputManager.readCommand();
            Response answerMsg = runCommand(commandMsg);
            if (answerMsg.getStatus().equals(Status.EXIT)) {
                close();
            }
        }
    }

    public void setInputManager(InputManager in) {
        inputManager = in;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public static String getHelp() {
        // TODO: eto pizdec
        return "\n" +
                "help : показать справку для доступных команд\n" +
                "\n" +
                "info : запись в стандартный вывод информации о коллекции (тип,\n" +
                "дата инициализации, количество элементов и т.д.)\n" +
                "\n" +
                "show : вывести на стандартный вывод все элементы коллекции в\n" +
                "строковое представление\n" +
                "\n" +
                "add {element} : добавление новый элемент в коллекцию\n" +
                "\n" +
                "update id {element} : обновите значение элемента коллекции по идентификатору\n" +
                "\n" +
                "remove_by_id id : удалить элемент из коллекции по его идентификатору\n" +
                "\n" +
                "clear : очистить коллекцию\n" +
                "\n" +
                "save (file_name - optional) : сохраните коллекцию в общий файл\n" +
                "\n" +
                "load (file_name - optional): загрузить коллекцию из common.file\n" +
                "\n" +
                "execute_script file_name : считайте и выполняйте скрипт из указанного общего файла.\n" +
                "Скрипт содержит команды в той же форме, в которой они вводятся\n" +
                "пользователь является интерактивным.\n" +
                "\n" +
                "exit : выйдите из программы (без сохранения в общий файл)\n" +
                "\n" +
                "remove_first : удалите первый элемент из коллекции\n" +
                "\n" +
                "add_if_max {element} : добавьте новый элемент в коллекцию, если его\n" +
                "\n" +
                "значение больше значения самого большого элемента этой коллекции\n" +
                "\n" +
                "add_if_min {element} : добавьте новый элемент в коллекцию, если он\n" +
                "значение меньше, чем наименьший элемент этой коллекции\n" +
                "\n" +
                "print_average_of_minutes_of_waiting : выведите средне значение\n" +
                "по полю время ожидания\n" +
                "\n" +
                "filter_starts_with_name name : выходные элементы, значение имени поля\n" +
                "который начинается с заданной подстроки\n" +
                "\n" +
                "print_unique_impact_speed : выведите уникальные значения поля скорости удара";
      
        //return "\r\nhelp : show help for available commands\r\n\r\ninfo : Write to standard output information about the collection (type,\r\ninitialization date, number of elements, etc.)\r\n\r\nshow : print to standard output all elements of the collection in\r\nstring representation\r\n\r\nadd {element} : add a new element to the collection\r\n\r\nupdate id {element} : update the value of the collection element whose id\r\nequal to given\r\n\r\nremove_by_id id : remove an element from the collection by its id\r\n\r\nclear : clear the collection\r\n\r\nsave (file_name - optional) : save the collection to a common.file\r\n\r\nload (file_name - optional): load collection from common.file\r\n\r\nexecute_script file_name : read and execute script from specified common.file.\r\nThe script contains commands in the same form in which they are entered\r\nuser is interactive.\r\n\r\nexit : exit the program (without saving to a common.file)\r\n\r\nremove_first : remove the first element from the collection\r\n\r\nadd_if_max {element} : add a new element to the collection if its\r\n\r\nvalue is greater than the value of the largest element of this collection\r\n\r\nadd_if_min {element} : add a new element to the collection if it\r\nthe value is less than the smallest element of this collection\r\n\r\nprint_average_of_minutes_of_waiting: output minutes of waiting time average\r\n\r\nfilter_starts_with_name name : output elements, value of field name\r\nwhich starts with the given substring\r\n\r\nprint_unique_salary : print the unique values of the salary field of all\r\nitems in the collection\r\n"; main
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void close() {
        setRunning(false);
    }
}
