package commands;

import client.Client;

import static common.io.OutputManager.*;

import common.exceptions.*;
import common.commands.*;
import common.connection.*;

import java.nio.charset.StandardCharsets;

/**
 * Менеджер команд для клиента.
 */

public class ClientCommandManager extends CommandManager {
    private Client client;

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
    public AnswerMsg runCommand(Request msg) throws FileException, InvalidDataException, ConnectionException {
        AnswerMsg res = new AnswerMsg();
        if (hasCommand(msg)) {
            Command cmd = getCommand(msg);
            cmd.setArgument(msg);
            res = (AnswerMsg) cmd.run();
        } else {
            try {
                client.send(msg);
                res = (AnswerMsg) client.receive();
            //} catch (ConnectionTimeoutException e) {
                //res.info("котёнок умер").setStatus(Response.Status.EXIT); // =(
            } catch (InvalidDataException | ConnectionException e) {
                res.error(e.getMessage());
            }
        }
        //print(new String(res.toString().getBytes(), StandardCharsets.UTF_8)); // летит время
        print(res);
        return res;
    }
}
