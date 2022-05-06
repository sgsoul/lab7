package commands;

import auth.UserManager;
import collection.HumanManager;
import common.auth.User;

import server.*;
import log.*;
import common.commands.*;
import common.connection.*;
import common.data.HumanBeing;
import common.exceptions.*;


public class ServerCommandManager extends CommandManager {
    private final Server server;

    private final UserManager userManager;

    public ServerCommandManager(Server serv) {
        server = serv;
        HumanManager collectionManager = server.getCollectionManager();
        userManager = server.getUserManager();
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

        addCommand(new LoginCommand(userManager));
        addCommand(new RegisterCommand(userManager));
        addCommand(new ShowUsersCommand(userManager));
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
            if (cmd.getType() != CommandType.AUTH && cmd.getType() != CommandType.SPECIAL) {
                if (isGeneratedByServer) {
                    user = server.getHostUser();
                    msg.setUser(user);
                }
                if (user == null) throw new AuthException();
                if (!userManager.isValid(user)) throw new AuthException();

                HumanBeing worker = msg.getHuman();
                if (worker != null) worker.setUser(user);
            }
            res = (AnswerMsg) super.runCommand(msg);
        } catch (ConnectionException | CommandException e) {
            res.error(e.getMessage());
        }
        String message = "";

        if (user != null) message += "[" + user.getLogin() + "] ";
        if (cmdName != null) message += "[" + cmdName + "] ";

        if (res.getMessage().contains("\n")) message += "\n";
        switch (res.getStatus()) {
            case EXIT:
                Log.logger.fatal(message + "Выключение...");
                server.close();
                break;
            case ERROR:
                Log.logger.error(message + res.getMessage());
                break;
            case AUTH_SUCCESS:
                if (isGeneratedByServer) server.setHostUser(user);
            default:
                Log.logger.info(message + res.getMessage());
                break;
        }
        return res;
    }
}
