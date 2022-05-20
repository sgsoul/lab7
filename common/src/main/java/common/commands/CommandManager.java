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
            print("������� �������. 'help' - ������ ������: ");
            try {
                CommandMsg commandMsg = inputManager.readCommand();
                answerMsg = runCommand(commandMsg);
            } catch (NoSuchElementException e) {
                close();
                print("���������������� ���� ����������.");
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
                "register {user} : ����������� ������ ������������\n" +
                "\n" +
                "login {user} : ���� � �������\n" +
                "\n" +
                "help : �������� ������� ��� ��������� ������\n" +
                "\n" +
                "info : ������ � ����������� ����� ���������� � ��������� (���,\n" +
                "���� �������������, ���������� ��������� � �.�.)\n" +
                "\n" +
                "show : ������� �� ����������� ����� ��� �������� ��������� �\n" +
                "��������� �������������\n" +
                "\n" +
                "add {element} : ���������� ����� ������� � ���������\n" +
                "\n" +
                "update id {element} : �������� �������� �������� ��������� �� ��������������\n" +
                "\n" +
                "remove_by_id id : ������� ������� �� ��������� �� ��� ��������������\n" +
                "\n" +
                "clear : �������� ���������\n" +
                "\n" +
                "save (file_name - optional) : ��������� ��������� � ����� ����\n" +
                "\n" +
                "load (file_name - optional): ��������� ��������� �� common.file\n" +
                "\n" +
                "execute_script file_name : �������� � ���������� ������ �� ���������� ������ �����.\n" +
                "������ �������� ������� � ��� �� �����, � ������� ��� ��������\n" +
                "������������ �������� �������������.\n" +
                "\n" +
                "exit : ������� �� ��������� (��� ���������� � ����� ����)\n" +
                "\n" +
                "remove_first : ������� ������ ������� �� ���������\n" +
                "\n" +
                "add_if_max {element} : �������� ����� ������� � ���������, ���� ���\n" +
                "\n" +
                "�������� ������ �������� ������ �������� �������� ���� ���������\n" +
                "\n" +
                "add_if_min {element} : �������� ����� ������� � ���������, ���� ��\n" +
                "�������� ������, ��� ���������� ������� ���� ���������\n" +
                "\n" +
                "print_average_of_minutes_of_waiting : �������� ������ ��������\n" +
                "�� ���� ����� ��������\n" +
                "\n" +
                "filter_starts_with_name name : �������� ��������, �������� ����� ����\n" +
                "������� ���������� � �������� ���������\n" +
                "\n" +
                "print_unique_impact_speed : �������� ���������� �������� ���� �������� �����";

    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void close() {
        setRunning(false);
    }
}
