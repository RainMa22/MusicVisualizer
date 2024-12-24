package me.rainma22.musicvisualizer.Image;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.FastMath;

/**
 * represents a Component of an IntermediateImage
 */
public abstract class Component {

    private int x, y;

    public Component(int x, int y) {
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

    /**
     *
     * @param x the 1st numerical component to add
     * @param y the 2nd numerical component to add
     * @return a Point with the added x and added y
     */
    public Component addition(int x, int y) {
        return new Point(getX() + x, getY() + y);
    }

    /**
     *
     * @param other the other vector to add
     * @return a Point with the added x and added y
     */
    public Component addition(Component other) {
        return addition(other.getX(), other.getY());
    }

    /**
     * @param x the subtrahand of the 1st component 
     * @param y the cubtrahand of the 2nd component
     * @return a point with x=(this.x - x) and y=(this.y - y)
     */
    public Component subtraction(int x, int y) {
        return new Point(getX() - x, getY() - y);
    }
    
    /**
     * @param other the subtrahand vector
     * @return a point that has coordinates this-other by component
     */
    public Component subtraction(Component other) {
        return subtraction(other.getX(), other.getY());
    }
    
    /**
     * @param x the 1st component to multiply
     * @param y the 2nd component to multiply
     * @return a point with x=this.x*x, y = this.y*y
     */
    public Component multiplicationByComponent(int x, int y) {
        return new Point(getX() * x, getY() * y);
    }

    
    /**
     * @param other the other vector to multiply
     * @return a point that has coordinates this*other by component
     */
    public Component multiplicationByComponent(Component other) {
        return multiplicationByComponent(other.getX(), other.getY());
    }

    /**
     * @param x the 1st component of the other vector
     * @param y the 2nd component of the other vector
     * @return the scalar product of this vector and other vector
     */
    public int dot(int x, int y) {
        return getX() * x + getY() * y;
    }

    /**
     * @param other the other vector
     * @return the scalar product of this vector and other vector
     */
    public int dot(Component other) {
        return dot(other.getX(), other.getY());
    }
    
    /**
     * @param x the other x
     * @param y the other y
     * @return the sum of distance squared of x and y
     */
    public int SquaredEuclideanDistance(int x, int y) {
        return SquaredEuclideanDistance(new Point(x, y));
    }
    
    /**
     * 
     * @param other the other vector
     * @return the sum of distance squared of the 2 vectors
     */
    public int SquaredEuclideanDistance(Component other) {
        Component diff = subtraction(other);
        return diff.dot(diff);
    }
    
    /**
     * changes this Components x and y along the given theta and magnitude
     * @see Polar Coordinates for more detail
     * @param theta the translation direction in Radians 
     * @param magnitude the magnitude of translation
     */
    public void polarTranslationInPlace(float theta, float magnitude) {
        x += (int) (magnitude * FastMath.cos(theta));
        y += (int) (magnitude * FastMath.sin(theta));
    }

    /**
     * 
     * @param theta the translation direction in Radians
     * @param magnitude the magnitude of translation
     * @return a copy of the Component with the translation applied
     */
    public Component polarTranslation(float theta, float magnitude) {
        Component p = copy();
        p.polarTranslationInPlace(theta, magnitude);
        return p;
    }

    abstract public String getName();

    abstract public <T extends Component> T copy();

    /**
     * @return a String that retain ALL the information that this Component maintains 
     */
    @Override
    public String toString() {
        return stringAsChild(0);
    }

    /**
     * @return a string representing self 
     */
    public String selfString() {
        return String.join(getName(), getX().toString(), getY().toString());
    }

    /**
     * @param tabLevel the amount of tabs of self 
     * @return the string of self prefixed by the given amount of tabs
     */
    public String stringAsChild(int tabLevel) {
        return StringUtils.repeat('\t', tabLevel).concat(selfString());
    }

}
