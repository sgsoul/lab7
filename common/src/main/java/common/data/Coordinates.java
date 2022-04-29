package common.data;

import java.io.Serializable;

public class Coordinates implements Validateable, Serializable {
    private Double x;
    private Double y; // Поле не может быть null
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
        s += "{\"x\" : " + Double.toString(x) + ", ";
        s += "\"y\" : " + Double.toString(y) + "}";
        return s;
    }
    
    public boolean validate(){
        return !(y==null || x==null);
    }

}
