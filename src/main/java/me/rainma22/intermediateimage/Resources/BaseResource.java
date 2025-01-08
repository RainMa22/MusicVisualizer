package me.rainma22.intermediateimage.Resources;

public abstract class BaseResource {
    public abstract String getName();

    @Override
    public String toString(){
        return getName();
    }
}
