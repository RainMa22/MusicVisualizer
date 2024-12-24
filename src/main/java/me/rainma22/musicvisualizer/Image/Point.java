package me.rainma22.musicvisualizer.Image;

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
