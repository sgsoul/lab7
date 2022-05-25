package common.exceptions;

public class PermissionException extends AuthException {
    public PermissionException(String user){
        super( "[PermissionException]", "у вас нет разрешения, элемент был создан ["+user+"]");
    }
}
