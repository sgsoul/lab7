package common.exceptions;

/**
 * Ошибки подключения
 */

public class ConnectionException extends Exception {
    public ConnectionException(String s) {
        super("[ConnectionException] " + s);
    }
    public ConnectionException(){
        super("[ConnectionException] Что-то не так с подключением.");
    }
    public ConnectionException(String t, String msg){
        super(t+" "+msg);
    }
}
