package common.exceptions;

public class PermissionException extends AuthException {
    public PermissionException(String user){
        super( "[PermissionException]", "� ��� ��� ����������, ������� ��� ������ ["+user+"]");
    }
}
