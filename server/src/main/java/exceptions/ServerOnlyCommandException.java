package exceptions;

import common.exceptions.CommandException;

/**
 * �������������, ���� ������� ������������� ������ ��� �������.
 */

public class ServerOnlyCommandException extends CommandException {
    public ServerOnlyCommandException() {
        super("��� ������� ������������� ������ ��� �������.");
    }
}
