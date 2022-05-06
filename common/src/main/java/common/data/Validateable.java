package common.data;

public interface Validateable {

    /**
     * Проверяет поля после десериализации json.
     */

    boolean validate();
}
