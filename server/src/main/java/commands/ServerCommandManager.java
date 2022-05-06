package commands;

import common.file.ReaderWriter;
import common.utils.User;
import database.UserManager;
import server.*;
import log.*;
import common.commands.*;
import common.connection.*;
import common.data.HumanBeing;
import common.exceptions.*;

import collection.CollectionManager;

import java.nio.charset.StandardCharsets;

public class ServerCommandManager extends CommandManager {
    private Server server;
    private CollectionManager<HumanBeing> collectionManager;
    private ReaderWriter fileManager;
    private UserManager userManager;

    public ServerCommandManager(Server serv) {
        server = serv;
        collectionManager = server.getCollectionManager();
        fileManager = server.getFileManager();
        addCommand(new ExitCommand());
        addCommand(new HelpCommand());
        addCommand(new ExecuteScriptCommand(this));
        addCommand(new InfoCommand(collectionManager));
        addCommand(new AddCommand(collectionManager));
        addCommand(new AddIfMinCommand(collectionManager));
        addCommand(new AddIfMaxCommand(collectionManager));
        addCommand(new UpdateCommand(collectionManager));
        addCommand(new RemoveByIdCommand(collectionManager));
        addCommand(new ClearCommand(collectionManager));
        addCommand(new RemoveFirstCommand(collectionManager));
        addCommand(new ShowCommand(collectionManager));
        addCommand(new FilterStartsWithNameCommand(collectionManager));
        addCommand(new PrintAverageOfMinutesOfWaiting(collectionManager));
        addCommand(new PrintUniqueImpactSpeedCommand(collectionManager));
        addCommand(new SaveCommand(collectionManager, fileManager));
        addCommand(new LoadCommand(collectionManager, fileManager));
    }

    public Server getServer() {
        return server;
    }

    @Override
    public Response runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        User user = msg.getUser();
        String cmdName = msg.getCommandName();
        boolean isGeneratedByServer = (msg.getStatus() != Request.Status.RECEIVED_BY_SERVER);
        try {
            Command cmd = getCommand(msg);

            //executing command
            res = (AnswerMsg) this.runCommand(msg);
        } catch (CommandException e) {
            res.error(e.getMessage());
        }
        String message = "";

        //format user and current command
        if (user != null) message += "[" + user.getLogin() + "] ";
        if (cmdName != null) message += "[" + cmdName + "] ";

        //format multiline output
        if (res.getMessage().contains("\n")) message += "\n";
        switch (res.getStatus()) {
            case EXIT:
                Log.logger.fatal(message + "до свидания.");
                server.close();
                break;
            case ERROR:
                Log.logger.error(message + res.getMessage());
                break;
            case AUTH_SUCCESS: //check if auth command was invoked by server terminal
                if (isGeneratedByServer) server.setHostUser(user);
            default:
                Log.logger.info(message + res.getMessage());
                break;
        }
        return res;
    }
}
