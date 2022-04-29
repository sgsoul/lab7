package commands;

import common.file.ReaderWriter;
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
    public AnswerMsg runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        try {
            Command cmd = getCommand(msg.getCommandName());
            cmd.setArgument(msg);
            res = (AnswerMsg) cmd.run();
        } catch (CommandException e) {
            res.error(e.getMessage());
        }
        switch (res.getStatus()) {
            case EXIT:
                //Log.logger.fatal(new String(res.getMessage().getBytes(), StandardCharsets.UTF_8));
                Log.logger.fatal(res.getMessage());
                server.close();
                break;
            case ERROR:
                //Log.logger.error(new String(res.getMessage().getBytes(), StandardCharsets.UTF_8));
                Log.logger.fatal(res.getMessage());
                break;
            default:
                //Log.logger.info(new String(res.getMessage().getBytes(), StandardCharsets.UTF_8));
                Log.logger.fatal(res.getMessage());
                break;
        }
        return res;
    }
}
