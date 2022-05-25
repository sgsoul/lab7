package common.exceptions;

public class CannotUpdateException extends CollectionException{
    public CannotUpdateException(Integer id){
        super("[CannotUpdateException] не удалось обновить элемент ["+id+"]");
    }
}
