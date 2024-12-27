package me.rainma22.musicvisualizer.intermediateimage;

/**
 * Represent a circle shape/container in the intermediateImage
 */
public class Circle extends ContainerComponent {

    private static final float PI = 3.141592653589f;
    private float radius;

    public Float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Circle(int x, int y, float radius) {
        super(x, y);
        setRadius(radius);
    }

    @Override
    public String getName() {
        return "CIRCLE";
    }

    @Override
    public Integer getCenterX() {
        return (int) (getX() + radius);
    }

    @Override
    public void setCenterX(int x) {
        setX((int) (x - radius));
    }

    @Override
    public Integer getCenterY() {
        return (int) (getY() + radius);
    }

    @Override
    public void setCenterY(int y) {
        setY((int) (y - radius));
    }

    @Override
    public Circle copy() {
        return new Circle(getX(), getY(), getRadius());
    }

    /**
     * @return a PolyLine representation of the circle
     * @param vertices the amount of vertices the converted PolyLine
     * should have
     */
    public PolyLine toPolyline(int vertices) {
        Component center = getCenter();
        PolyLine line = new PolyLine(center.getX(), center.getY());
        line.polarTranslationInPlace(0, radius);
        for (int i = 1; i < vertices; i++) {
            line.addPoint(center.polarTranslation(2 * PI * i / (vertices + 1), radius));
        }
        line.setInside(PolyLine.CENTER);
        return line;
    }

    @Override
    public String selfString() {
        return String.join(" ",
                super.selfString(),
                getRadius().toString());
    }
}
