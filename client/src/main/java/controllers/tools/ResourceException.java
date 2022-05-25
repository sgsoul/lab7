package controllers.tools;

public class ResourceException extends RuntimeException{
    public ResourceException(String key){
        super("Ресурс не найден: " + key);
    }
}
