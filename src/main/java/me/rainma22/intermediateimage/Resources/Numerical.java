package me.rainma22.intermediateimage.Resources;

/**
 * Represent a numerical value resource
 */
public class Numerical<T extends Number> extends BaseResource {
    T number;

    public Numerical(T number){
        this.number = number;
    }

    public T getValue(){
        return number;
    }

    @Override
    public String getName() {
        return "NUMERICAL";
    }

    @Override
    public String toString(){
        return (String.join(" ", super.toString(), String.valueOf(number)));
    }
}
