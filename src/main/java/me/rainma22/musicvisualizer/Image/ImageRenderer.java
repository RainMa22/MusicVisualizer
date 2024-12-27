package me.rainma22.musicvisualizer.Image;

/**
 * represents a renderer of a intermediate Image
 */
public abstract class ImageRenderer {

    private EffectApplier applier;

    public ImageRenderer(EffectApplier applier) {
        setApplier(applier);
    }

    public EffectApplier getApplier() {
        return applier;
    }

    public void setApplier(EffectApplier applier) {
        this.applier = applier;
    }

    public abstract void drawCircle(Circle c);

    public abstract void drawIntermediateImage(IntermediateImage iimg);

    public abstract void drawPoint(Point p);

    public abstract void drawPolyLine(PolyLine pl);

    public abstract void drawRectangle(Rectangle r);
}
