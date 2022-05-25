package common.data;

import java.io.Serializable;

public class Car implements Serializable {
    private String name; //Поле может быть null
    private boolean cool;

    public Car(String name, boolean cool) {
        this.name = name;
        this.cool = cool;
    }

    public Car() {

    }

    /**
     * @return Название машины.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Крутая машина - ДА, ПОТОМУ ЧТО ЭТО ГТА.
     */

    public boolean checkCool() {
        return cool;
    }

    @Override
    public String toString() {
        return "Name:" + name + " Cool:" + cool;
    }

}