package common.connection;

import java.io.Closeable;

/**
 * ��������� ��� �������� � ���������.
 */

public interface SenderReceiver extends Closeable {
    int BUFFER_SIZE = 4096 * 2;
}
