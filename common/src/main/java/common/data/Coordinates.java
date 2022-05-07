package common.data;

import java.io.Serializable;

public class Coordinates implements Validateable, Serializable {
    private final Double x;
    private final Double y; // Поле не может быть null
    public Coordinates(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * @return x coord
     */

    public double getX() {
        return x;
    }
    
    /**
     * @return y coord
     */

    public double getY() {
        return y;
    }

    @Override
    public String toString(){
        String s = "";
        s += "{\"x\" : " + x + ", ";
        s += "\"y\" : " + y + "}";
        return s;
    }
    
    public boolean validate(){
        return true;
    }

}
