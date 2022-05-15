package commands;

import client.Client;

import static common.io.ConsoleOutputter.print;
import static common.io.OutputManager.*;

import common.exceptions.*;
import common.commands.*;
import common.connection.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Менеджер команд для клиента.
 */

public class ClientCommandManager extends CommandManager {
    private final Client client;
    public final Lock lock = new ReentrantLock();

    public ClientCommandManager(Client c) {
        client = c;
        addCommand(new ExecuteScriptCommand(this));
        addCommand(new ExitCommand());
        addCommand(new HelpCommand());
    }

    public Client getClient() {
        return client;
    }

    @Override
    public AnswerMsg runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        if (hasCommand(msg)) {
            res = (AnswerMsg) super.runCommand(msg);
            if (res.getStatus() == Response.Status.EXIT) {
                res.info("Отключение...");
            }
        } else {
            try {
                if (client.getUser() != null && msg.getUser() == null) msg.setUser(client.getUser());
                else client.setAttemptUser(msg.getUser());
                client.send(msg);
                res = (AnswerMsg) client.receive();
                if (res.getStatus() == Response.Status.AUTH_SUCCESS) {
                    client.setUser(msg.getUser());
                }
            } catch (ConnectionTimeoutException e) {
                res.info("Попытки всё...bye").setStatus(Response.Status.EXIT);
            } catch (InvalidDataException | ConnectionException e) {
                res.error(e.getMessage());
            }
        }
        print(res);
        return res;
    }
}