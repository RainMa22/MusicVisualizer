package me.rainma22.musicvisualizer.Image;

import org.apache.commons.lang3.StringUtils;

public abstract class Component {
    private int x,y;

    public Component(int x, int y){
        setX(x);
        setY(y);
    }
    public Integer getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public Component addition(int x, int y){
        return new Point(getX() + x, getY() + y);
    }

    public Component addition(Component other){
        return new Point(getX() + other.getX(), getY() + other.getY());
    }

    public Component subtract(Component other){
        return new Point(getX() - other.getX(), getY() - other.getY());
    }

    abstract public String getName();

    @Override
    public String toString(){
        return stringAsChild(0);
    }

    public String selfString(){
        return String.join(getName(), getX().toString(), getY().toString());
    }

    public String stringAsChild(int tabLevel){
        return StringUtils.repeat('\t', tabLevel).concat(selfString());
    }

}
