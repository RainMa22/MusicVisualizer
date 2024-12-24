package me.rainma22.musicvisualizer.Image;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.FastMath;

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
        return addition(other.getX(), other.getY());
    }


    public Component subtraction(int x, int y){
        return new Point(getX() - x, getY() - y);
    }

    public Component subtraction(Component other){
        return subtraction(other.getX(), other.getY());
    }

    public Component multiplicationByComponent(int x, int y){
        return new Point(getX()*x, getY()*y);
    }

    public Component multiplicationByComponent(Component other){
        return multiplicationByComponent(other.getX(), other.getY());
    }


    public int dot(int x, int y){
        return getX()*x + getY()*y;
    }

    public int dot(Component other){
        return dot(other.getX(), other.getY());
    }

    public int SquaredEuclideanDistance(int x, int y){
        return SquaredEuclideanDistance(new Point(x,y));
    }

    public int SquaredEuclideanDistance(Component other){
        Component diff = subtraction(other);
        return diff.dot(diff);
    }

    public void applyInPlace(float theta, float magnitude){
        x += (int) (magnitude * FastMath.cos(theta));
        y += (int) (magnitude * FastMath.sin(theta));
    }

    public Component apply(float theta, float magnitude){
        Point p = new Point(getX(),getY());
        p.applyInPlace(theta, magnitude);
        return p;
    }

    abstract public String getName();
    abstract public <T extends Component> T copy();
    
    
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
