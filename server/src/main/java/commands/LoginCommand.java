package commands;

import auth.UserManager;
import common.auth.User;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.Response;
import common.exceptions.*;


public class LoginCommand extends CommandImpl {
    private final UserManager userManager;

    public LoginCommand(UserManager manager) {
        super("login", CommandType.AUTH);
        userManager = manager;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException, FileException, ConnectionException {
        return null;
    }

    @Override
    public Response run() throws AuthorizationException {
        User user = getArgument().getUser();
        if (user != null && user.getLogin() != null && user.getPassword() != null) {
            if (userManager.isValid(user)) {
                return new AnswerMsg().info("Вход в систему прошел успешно.").setStatus(Response.Status.AUTH_SUCCESS);
            }
        }
        throw new AuthorizationException("Неверный логин или пароль.");

    }
}
