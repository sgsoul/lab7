package common.exceptions;

/**
 * Выбрасывается, когда адрес недействителен.
 */

public class InvalidAddressException extends ConnectionException{
    public InvalidAddressException(){
        super("Адрес недействителен.");
    }
    public InvalidAddressException(String s){
        super(s);
    }
}
