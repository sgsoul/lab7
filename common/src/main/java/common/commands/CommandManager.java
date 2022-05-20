package common.commands;

import common.connection.AnswerMsg;
import common.connection.CommandMsg;
import common.connection.Request;
import common.connection.Response;
import common.exceptions.*;
import common.io.ConsoleInputManager;
import common.io.FileInputManager;
import common.io.InputManager;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

import static common.io.ConsoleOutputter.print;

import javax.xml.crypto.Data;

public abstract class CommandManager implements Commandable, Closeable {
    private final Map<String, Command> map;
    private InputManager inputManager;
    private boolean isRunning;
    private static final Stack<String> callStack = new Stack<>();


    public Stack<String> getStack() {
        return callStack;
    }


    public CommandManager() {
        isRunning = false;
        map = new HashMap<>();
    }

    public void addCommand(Command c) {
        map.put(c.getName(), c);
    }

    public Command getCommand(String s) {
        if (!hasCommand(s)) throw new NoSuchCommandException();
        return map.get(s);
    }

    public boolean hasCommand(String s) {
        return map.containsKey(s);
    }

    public void consoleMode() {
        inputManager = new ConsoleInputManager();
        isRunning = true;
        while (isRunning) {
            Response answerMsg;
            print("Введите команду. 'help' - список команд: ");
            try {
                CommandMsg commandMsg = inputManager.readCommand();
                answerMsg = runCommand(commandMsg);
            } catch (NoSuchElementException e) {
                close();
                print("Пользовательский ввод недоступен.");
                break;
            }
            if (answerMsg.getStatus() == Response.Status.EXIT) {
                close();
            }
        }
    }

    public void fileMode(String path) throws FileException {
        inputManager = new FileInputManager(path);
        isRunning = true;
        while (isRunning && inputManager.hasNextLine()) {
            CommandMsg commandMsg = inputManager.readCommand();
            Response answerMsg = runCommand(commandMsg);
            if (answerMsg.getStatus() == Response.Status.EXIT) {
                close();
            }
        }
    }

    public Response runCommand(Request msg) throws DatabaseException {
        AnswerMsg res = new AnswerMsg();
        try {
            Command cmd = getCommand(msg);
            cmd.setArgument(msg);
            res = (AnswerMsg) cmd.run();

        } catch (ExitException e) {
            res.setStatus(Response.Status.EXIT);
        } catch (CommandException | InvalidDataException | ConnectionException | FileException | CollectionException e) {
            res.error(e.getMessage());
        }
        return res;
    }

    public static String getHelp() {
        return " \n" +
                "register {user} : регистрация нового пользователя\n" +
                "\n" +
                "login {user} : вход в систему\n" +
                "\n" +
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

    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void close() {
        setRunning(false);
    }
}
