package common.exceptions;

import java.nio.charset.StandardCharsets;

/**
 * ���������� � �������
 */

public class FileException extends Exception {
    public FileException(String msg) {
        super(msg);
    }

    public FileException() {
        super("[FileException] �� ������� ��������� ����.");
    }
}