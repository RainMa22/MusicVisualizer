package me.rainma22.musicvisualizer.Image;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ContainerComponent extends Component implements Iterable<Component>{
    protected List<Component> children;
    protected int backgroundColor_rgba = 0x0;
    protected int strokeColor_rgba = 0x0;
    protected int strokeSize_px = 0;

    public abstract Integer getCenterX();
    public abstract void setCenterX(int x);

    public abstract Integer getCenterY();
    public abstract void setCenterY(int y);


    public Integer getBackgroundColor_rgba() {
        return backgroundColor_rgba;
    }

    public void setBackgroundColor_rgba(int backgroundColor_rgba) {
        this.backgroundColor_rgba = backgroundColor_rgba;
    }

    public Integer getStrokeColor_rgba() {
        return strokeColor_rgba;
    }

    public void setStrokeColor_rgba(int strokeColor_rgba) {
        this.strokeColor_rgba = strokeColor_rgba;
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

    @Override
    public String selfString(){
        return String.join(" ",
                super.selfString(),
                getStrokeSize_px().toString(),
                Integer.toHexString(getStrokeColor_rgba()),
                Integer.toHexString(getBackgroundColor_rgba()));
    }
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
    public Iterator<Component> iterator() {
        return children.iterator();
    }
}
