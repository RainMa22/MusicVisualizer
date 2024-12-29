package me.rainma22.intermediateimage;

/**
 * Represents a Rectangle shape/container
 */
public class Rectangle extends ContainerComponent {

    private int width, height;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rectangle(int x, int y, int width, int height) {
        super(x, y);
        setWidth(width);
        setHeight(height);
    }

    @Override
    public String getName() {
        return "RECT";
    }

    @Override
    public Integer getCenterX() {
        return getX() + width / 2;
    }

    @Override
    public void setCenterX(int x) {
        setX(x - width / 2);
    }

    @Override
    public Integer getCenterY() {
        return getY() + height / 2;
    }

    @Override
    public void setCenterY(int y) {
        setY(y - height / 2);
    }

    @Override
    public String selfString() {
        return String.join(" ", super.selfString(),
                getWidth().toString(), getHeight().toString());
    }

    @Override
    public Rectangle copy() {
        Rectangle result = new Rectangle(getX(), getY(), getWidth(), getHeight());
        for (Component child : children) {
            result.children.add(child.copy());
        }
        
        result.setBackgroundColor_rgba(backgroundColor_rgba);
        result.setStrokeColor_rgba(strokeColor_rgba);
        result.setStrokeSize_px(strokeSize_px);

        return result;
    }

}
