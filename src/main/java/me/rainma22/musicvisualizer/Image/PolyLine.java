package me.rainma22.musicvisualizer.Image;

import org.apache.commons.math3.util.FastMath;

import java.util.List;

public class PolyLine extends ContainerComponent {
    private static final float FLOAT_PI = (float) FastMath.PI;

    public final String NEGATIVE_Y = "-Y";
    public final String POSITIVE_Y = "Y";
    public final String POSITIVE_X = "X";
    public final String NEGATIVE_X = "-X";
    private String inside = NEGATIVE_Y;

    @Override
    public Integer getCenterX() {
        int result = getX();
        for (Component c: children){
            result += c.getX();
        }
        return result/size();
    }

    @Override
    public void setCenterX(int x) {
        int xDiff = x - getCenterX();
        setX(getX() + xDiff);
    }

    @Override
    public Integer getCenterY() {
        int result = getY();
        for (Component c: children){
            result += c.getY();
        }
        return result/size();
    }

    @Override
    public void setCenterY(int y) {
        int yDiff = y - getCenterY();
        setY(getY() + yDiff);
    }

    public PolyLine(int x, int y) {
        super(x, y);
    }

    @Override
    public String getName() {
        return "Line";
    }

    public void addPoint(Component component) {
        children.add(component);
    }

    private float outsideTheta(float theta) {
        float x = (float) FastMath.cos(theta);
        float y = (float) FastMath.sin(theta);
        switch (inside) {
            case POSITIVE_X:
                return (x > 0) ? theta + FLOAT_PI : theta;
            case NEGATIVE_X:
                return (x < 0) ? theta + FLOAT_PI : theta;
            case POSITIVE_Y:
                return (y > 0) ? theta + FLOAT_PI : theta;
            case NEGATIVE_Y:
            default:
                return (y < 0) ? theta + FLOAT_PI : theta;
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
            delta = subtract(children.getFirst());
        } else if (index == size() - 1) {
            delta = get(index - 1).subtract(get(index));
        } else {
            delta = get(index - 1).subtract(get(index + 1));
        }
        int deltaX = delta.getX();
        int deltaY = delta.getY();
        return (float) FastMath.atan2(deltaY, deltaX);
    }

    public float getNormalTheta(int index) throws IndexOutOfBoundsException {
        return outsideTheta(getTangentTheta(index) + FLOAT_PI / 2f);
    }

    public PolyLine transform(List<Integer> magnitudeAlongNormal) {
        if(magnitudeAlongNormal.size() != size()) throw new ArithmeticException("Arity Mismatch");
        float normal = getNormalTheta(0);
        int magnitude = magnitudeAlongNormal.getFirst();
        int newX = (int) (getX()*magnitude*FastMath.cos(normal));
        int newY = (int) (getY()*magnitude*FastMath.sin(normal));
        PolyLine transformed = new PolyLine(newX, newY);
        transformed.setInside(this.getInside());

        for (int i = 1; i < size(); i++) {
            normal = getNormalTheta(i);
            magnitude = magnitudeAlongNormal.get(i);
            int deltaX = (int) (magnitude*FastMath.cos(normal));
            int deltaY = (int) (magnitude*FastMath.sin(normal));
            transformed.addPoint(get(i).addition(deltaX,deltaY));
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
