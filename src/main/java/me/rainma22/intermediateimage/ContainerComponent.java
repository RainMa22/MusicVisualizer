package me.rainma22.intermediateimage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a container Component which can have a list of components as it
 * children
 */
public abstract class ContainerComponent extends Component {

    protected List<Component> children;
    protected ColorProvider backgroundColor_rgba
            = ColorProvider.ofColor(ColorRGBA.TRANSPARENT);
    protected ColorProvider strokeColor_rgba
            = ColorProvider.ofColor(ColorRGBA.TRANSPARENT);

    public ColorProvider getBackgroundColor_rgba() {
        return backgroundColor_rgba;
    }

    public void setBackgroundColor_rgba(ColorProvider backgroundColor_rgba) {
        this.backgroundColor_rgba = backgroundColor_rgba;
    }

    public ColorProvider getStrokeColor_rgba() {
        return strokeColor_rgba;
    }

    public void setStrokeColor_rgba(ColorProvider strokeColor_rgba) {
        this.strokeColor_rgba = strokeColor_rgba;
    }
    protected int strokeSize_px = 0;

    public abstract Integer getCenterX();

    public abstract void setCenterX(int x);

    public abstract Integer getCenterY();

    public abstract void setCenterY(int y);

    public Component getCenter() {
        return new Point(getCenterX(), getCenterY());
    }

    public void setCenter(Component coord) {
        setCenterX(coord.getX());
        setCenterY(coord.getY());
    }

    public Integer getStrokeSize_px() {
        return strokeSize_px;
    }

    public void setStrokeSize_px(int strokeSize_px) {
        this.strokeSize_px = strokeSize_px;
    }

    public ContainerComponent(int x, int y) {
        super(x, y);
        children = new ArrayList<>();
    }

    public List<Component> getChildren() {
        return List.copyOf(children);
    }

    public int size() {
        return children.size();
    }

    public void clear() {
        children = new ArrayList<>();
    }

    /**
     *
     * @return a String representative of self, excluding the children
     */
    @Override
    public String selfString() {
        return String.join(" ",
                super.selfString(),
                getStrokeSize_px().toString(),
                getStrokeColor_rgba().toString(),
                getBackgroundColor_rgba().toString());
    }

    /**
     *
     * @param tabLevel the amount of tabs
     * @return the String representation of self, prefixed by given amount of
     * tabs the String of self is suffixed by a new line then the String of all
     * children which has 1 additional tab level and is joined by new line
     */
    @Override
    public String stringAsChild(int tabLevel) {
        List<String> childrenString = new ArrayList<>(size() + 1);
        childrenString.add(selfString());
        childrenString.addAll(
                getChildren().stream()
                        .map(
                                (x) -> x.stringAsChild(tabLevel + 1))
                        .toList());
        return String.join("\n", childrenString);
    }

    @Override
    public Component applyEffects(int currentFrame, EffectApplier applier) {
        ContainerComponent result = (ContainerComponent) super.applyEffects(currentFrame, applier);
        result.children = new ArrayList<>();
        for(Component child: children){
            result.children.add(child.applyEffects(currentFrame, applier));
        }
         return result;
    }
}