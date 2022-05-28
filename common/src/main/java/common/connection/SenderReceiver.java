package common.connection;

import java.io.Closeable;

/**
 * Интерфейс для отправки и получения.
 */

public interface SenderReceiver extends Closeable {
    int BUFFER_SIZE = 4096 * 2;
}
