package me.rainma22.musicvisualizer.Image;

public class Circle extends ContainerComponent {
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
    public String selfString() {
        return String.join(" ",
                super.selfString(),
                getRadius().toString());
    }
}
