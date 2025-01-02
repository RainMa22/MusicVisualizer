package me.rainma22.intermediateimage;

import org.apache.commons.math3.util.FastMath;

import java.util.List;

import static org.apache.commons.math3.util.FastMath.atan2;

/**
 * Represent a line which is drawn from its own coordinates through each child's
 * coordinates, and the last line is of the last child and its own XY
 * coordinates
 */
public class PolyLine extends ContainerComponent {

    private static final float PI = 3.141592653589f;

    private enum InsideDirection {
        NEGATIVE_X,
        NEGATIVE_Y,
        POSITVE_X,
        POSITIVE_Y,
        CENTER
    }



    public static final InsideDirection NEGATIVE_Y = InsideDirection.NEGATIVE_Y;
    public static final InsideDirection POSITIVE_Y = InsideDirection.POSITIVE_Y;
    public static final InsideDirection POSITIVE_X = InsideDirection.POSITVE_X;
    public static final InsideDirection NEGATIVE_X = InsideDirection.NEGATIVE_X;
    public static final InsideDirection CENTER = InsideDirection.CENTER;
    private InsideDirection inside = NEGATIVE_Y;

    private Component center;

    @Override
    public void setX(int x){
        int totalX = center.getX()*size();
        totalX -= getX();
        totalX += x;
        center.setX(totalX/size());
        super.setX(x);
    }


    @Override
    public void setY(int y){
        int totalY = center.getY()*size();
        totalY -= getY();
        totalY += y;
        center.setY(totalY/size());
        super.setY(y);
    }

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
        center = new Point(x, y);
    }

    @Override
    public String getName() {
        return "Line";
    }

    @Override
    public String selfString() {
        return String.join(" ",
                super.selfString(),
                getInside());
    }

    @Override
    public void add(Component component) {
        int newX = getCenterX() * size();
        int newY = getCenterX() * size();
        newX += component.getX();
        newY += component.getY();
        center.setX(newX / (size() + 1));
        center.setY(newY / (size() + 1));
        super.add(component);
    }

    @Override
    public void polarTranslationInPlace(float theta, float magnitude) {
        super.polarTranslationInPlace(theta, magnitude);
        center.polarTranslationInPlace(theta, magnitude);
    }

    private float outsideTheta(float theta, Component component) {
        float x = (float) FastMath.cos(theta);
        float y = (float) FastMath.sin(theta);
        switch (inside) {
            case POSITVE_X:
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
                other.polarTranslationInPlace(theta, 5);
                int otherDist = other.SquaredEuclideanDistance(center);
                int thisDist = component.SquaredEuclideanDistance(center);
                if (otherDist >= thisDist) {
                    return theta;
                } else {
                    return theta + PI;
                }

        }
    }

    private void recalculateCenter(){
        int totalX = 0;
        int totalY = 0;
        for (int i = 0; i < size(); i++) {
            totalX += get(i).getX();
            totalY += get(i).getY();
        }
        center = new Point(totalX/size(), totalY/size());
    }

    @Override
    public Component applyEffects(int currentFrame, EffectApplier applier) {
        PolyLine result = (PolyLine) super.applyEffects(currentFrame, applier);
        result.recalculateCenter();
        return result;
    }

    @Override
    public int size() {
        return super.size() + 1;
    }

    @Override
    public PolyLine copy() {
//        PolyLine result = new PolyLine(getX(), getY());
//        result.setInside(inside);
//        for (Component c : children) {
//            result.add((Component) c.copy());
//        }
        PolyLine result = (PolyLine) super.copy();
        result.inside = inside;
        result.center = center.copy();
        return result;
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
            delta = getInside().contains("X")
                    ? new Point(1, 0)
                    : new Point(0, 1);
        } else if (index == 0) {
            delta = subtraction(children.get(0));
        } else if (index == size() - 1) {
            delta = get(index - 1).subtraction(get(index));
        } else {
            delta = get(index - 1).subtraction(get(index + 1));
        }
        return (float) atan2(delta.getY(), delta.getX());
    }

    /**
     * @param index the index of the coordinates(0 being the polyline object
     *              itself)
     * @return the radians representation of the direction of the normal which
     * points OUTSIDE of the defined inside position
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public float getNormalTheta(int index) throws IndexOutOfBoundsException {
        if (inside != CENTER) return outsideTheta(getTangentTheta(index) + PI / 2f, get(index));
        Component delta = center.subtraction(get(index));
        return outsideTheta((float) atan2(delta.getY(), delta.getX()), get(index));

    }

    /**
     * @param magnitudeAlongNormal magnitude of change along Normal theta
     * @return a copy of this PolyLine which all of its points are modified to
     * @requires magnitudeAlongNormal.size() == self.size()
     */
    public PolyLine transform(List<Float> magnitudeAlongNormal) {
        if (magnitudeAlongNormal.size() != size()) {
            throw new ArithmeticException("Arity Mismatch");
        }
        recalculateCenter();
        float normal = getNormalTheta(0);
        float magnitude = magnitudeAlongNormal.get(0);
        int newX = (int) (getX() * magnitude * FastMath.cos(normal));
        int newY = (int) (getY() * magnitude * FastMath.sin(normal));
        PolyLine transformed = copy();
        transformed.setX(newX);
        transformed.setY(newY);
        for (int i = 1; i < size(); i++) {
            Component point = get(i);
            normal = getNormalTheta(i);
            magnitude = magnitudeAlongNormal.get(i);
            Component otherPoint = point.polarTranslation(normal, magnitude);
            transformed.add(otherPoint);
        }
        return transformed;
    }

    @Override
    public void clear() {
        super.clear();
        this.center = new Point(getX(), getY());
    }

    /**
     * @return the String representation of the current InsideDirection
     */
    public String getInside() {
        switch (inside) {
            case POSITVE_X:
                return "+X";
            case POSITIVE_Y:
                return "+Y";
            case NEGATIVE_X:
                return "-X";
            case NEGATIVE_Y:
            default:
                return "-Y";
            case CENTER:
                return "Center";

        }
    }

    /**
     * sets the inside direction of the PolyLine,
     * <ul>
     * <li> NEGATIVE_X represents that the inside is to the -x direction of the
     * point
     * </li>
     *
     * <li> NEGATIVE_Y represents that the inside is to the -y direction of the
     * point
     * </li>
     *
     * <li> POSITIVE_X represents that the inside is to the +x direction of the
     * point
     * </li>
     *
     * <li>
     * POSITIVE_Y represents that the inside is to the +y direction of the point
     * </li>
     *
     * <li> CENTER calculates the inside based on the center of the PolyLine
     * <li>
     * </ul>
     *
     * @param insideAt the Parameter of inside
     */
    public void setInside(InsideDirection insideAt) {
        inside = insideAt;
    }


    @Override
    public void render(ImageRenderer renderer) {
        renderer.drawPolyLine(this);
    }

}
