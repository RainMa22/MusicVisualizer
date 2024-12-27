package me.rainma22.musicvisualizer.intermediateimage;

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
        return new Point(getX(), getY());
    }
}
