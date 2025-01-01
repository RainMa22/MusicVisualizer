package me.rainma22.intermediateimage;

/**
 * Represent a point in 2D space;
 */
public class Point extends Component {

    public Point(int x, int y) {
        super(x, y);
    }

    @Override
    public String getName() {
        return "Point";
    }

    @Override
    public Point copy() {
        return (Point) super.copy();
    }

    @Override
    public void render(ImageRenderer renderer) {
        renderer.drawPoint(this);
    }
}
