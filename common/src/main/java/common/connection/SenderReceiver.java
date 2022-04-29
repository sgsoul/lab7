package common.connection;

import java.io.Closeable;

/**
 * Интерфейс для отправки и получения.
 */

public interface SenderReceiver extends Closeable {
    public final int BUFFER_SIZE = 4096;
}
