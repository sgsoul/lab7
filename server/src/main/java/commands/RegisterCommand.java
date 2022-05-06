package commands;

import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.Response;
import common.exceptions.CommandException;
import common.exceptions.ConnectionException;
import common.exceptions.FileException;
import common.exceptions.InvalidDataException;
import common.utils.User;
import database.UserManager;
import exceptions.DataBaseException;

public class RegisterCommand extends CommandImpl {
        private final UserManager userManager;

    public RegisterCommand(UserManager manager) {
        super("register", CommandType.AUTH);
        userManager = manager;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException, FileException, ConnectionException {
        return null;
    }

    @Override
    public Response run() throws DataBaseException {

        User user = getArgument().getUser();
        if (user != null && user.getLogin() != null && user.getPassword() != null) {
            if (userManager.isPresent(user.getLogin())) {
                throw new DataBaseException("user " + user.getLogin() + " already exist");
            }
            userManager.add(user);
            return new AnswerMsg().info("user " + user.getLogin() + " successfully registered").setStatus(Response.Status.AUTH_SUCCESS);
        }
        throw new DataBaseException("something wrong with user");

    }
}
