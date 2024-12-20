package me.rainma22.musicvisualizer.Image;

import org.apache.commons.math3.util.FastMath;

import java.util.List;

public class PolyLine extends ContainerComponent {
    private static final float PI = 3.141592653589f;

    public static final String NEGATIVE_Y = "-Y";
    public static final String POSITIVE_Y = "Y";
    public static final String POSITIVE_X = "X";
    public static final String NEGATIVE_X = "-X";
    public static final String CENTER = "CENTER";
    public String inside = NEGATIVE_Y;

    private final Component center;

    @Override
    public Integer getCenterX() {
        return getCenter().getX();
    }

    @Override
    public void setCenterX(int x) {
        int xDiff = x - getCenterX();
        setX(getX() + xDiff);
        center.setX(x);
    }

    @Override
    public Integer getCenterY() {
        return getCenter().getY();
    }

    @Override
    public void setCenterY(int y) {
        int yDiff = y - getCenterY();
        setY(getY() + yDiff);
        center.setY(y);
    }

    @Override
    public Component getCenter() {
        return center;
    }

    public PolyLine(int x, int y) {
        super(x, y);
        center = new Point(x,y);
    }

    @Override
    public String getName() {
        return "Line";
    }

    @Override
    public String selfString() {
        return String.join(" ",
                super.selfString(),
                inside);
    }

    public void addPoint(Component component) {
        int newX = getCenterX()*size();
        int newY = getCenterX()*size();
        newX += component.getX();
        newY += component.getY();
        children.add(component);
        center.setX(newX);
        center.setY(newY);
    }

    private float outsideTheta(float theta, Component component) {
        float x = (float) FastMath.cos(theta);
        float y = (float) FastMath.sin(theta);
        switch (inside) {
            case POSITIVE_X:
                return (x > 0) ? theta + PI : theta;
            case NEGATIVE_X:
                return (x < 0) ? theta + PI : theta;
            case POSITIVE_Y:
                return (y > 0) ? theta + PI : theta;
            case NEGATIVE_Y:
            default:
                return (y < 0) ? theta + PI : theta;
            case CENTER:
                Point other = new Point(component.getX(), component.getY());
                other.applyInPlace(theta, 1);
                if(other.SquaredEuclideanDistance(center) >=
                        component.SquaredEuclideanDistance(center)){
                    return theta;
                } else {
                    return theta + PI;
                }

        }
    }

    @Override
    public int size() {
        return super.size() + 1;
    }

    private Component get(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        return index == 0 ? this : children.get(index - 1);
    }

    private float getTangentTheta(int index) throws IndexOutOfBoundsException {
        Component delta;
        if (index == 0 && children.isEmpty()) {
            delta = inside.contains("X") ?
                    new Point(1, 0) :
                    new Point(0, 1);
        } else if (index == 0) {
            delta = subtraction(children.getFirst());
        } else if (index == size() - 1) {
            delta = get(index - 1).subtraction(get(index));
        } else {
            delta = get(index - 1).subtraction(get(index + 1));
        }
        int deltaX = delta.getX();
        int deltaY = delta.getY();
        return (float) FastMath.atan2(deltaY, deltaX);
    }

    public float getNormalTheta(int index) throws IndexOutOfBoundsException {
        return outsideTheta(getTangentTheta(index) + PI / 2f, get(index));
    }

    public PolyLine transform(List<Float> magnitudeAlongNormal) {
        if (magnitudeAlongNormal.size() != size()) throw new ArithmeticException("Arity Mismatch");
        float normal = getNormalTheta(0);
        float magnitude = magnitudeAlongNormal.getFirst();
        int newX = (int) (getX() * magnitude * FastMath.cos(normal));
        int newY = (int) (getY() * magnitude * FastMath.sin(normal));
        PolyLine transformed = new PolyLine(newX, newY);
        transformed.setInside(this.getInside());
        for (int i = 1; i < size(); i++) {
            normal = getNormalTheta(i);
            magnitude = magnitudeAlongNormal.get(i);
            transformed.addPoint(get(i).apply(normal, magnitude));
        }
        return transformed;
    }

    public String getInside() {
        return inside;
    }

    public void setInside(String insideAt) {
        inside = insideAt;
    }

}
