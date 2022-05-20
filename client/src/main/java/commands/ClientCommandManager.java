package commands;

import client.Client;

import static common.io.ConsoleOutputter.print;

import common.exceptions.*;
import common.commands.*;
import common.connection.*;


/**
 * �������� ������ ��� �������.
 */

public class ClientCommandManager extends CommandManager {
    private final Client client;

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
                res.info("����������...");
            }
        } else {
            try {
                if (client.getUser() != null && msg.getUser() == null) msg.setUser(client.getUser());
                else client.setAttemptUser(msg.getUser());
                client.send(msg);
                res = (AnswerMsg) client.receiveWithoutTimeLimits();
                if (res.getStatus() == Response.Status.AUTH_SUCCESS) {
                    client.setUser(msg.getUser());
                }
            } catch (ConnectionTimeoutException e) {
                res.info("������� ��...bye").setStatus(Response.Status.EXIT);
            } catch (InvalidDataException | ConnectionException e) {
                res.error(e.getMessage());
            }
        }
        print(res);
        return res;
    }
}