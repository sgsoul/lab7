package common.exceptions;

/**
 * �������������, ����� �� ������� ������� ����
 */

public class CannotCreateFileException extends FileException {
    public CannotCreateFileException() {
        super("�� ������� ������� ����.");
    }
}
