package common.exceptions;

public class CannotRemoveException extends CollectionException{
    public CannotRemoveException(Integer id){
        super("[CannotUpdateException] �� ������� �������� ������� ["+id+"]");
    }
}
