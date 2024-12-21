package me.rainma22.musicvisualizer.Image.Effects;

import me.rainma22.musicvisualizer.Image.Component;

public abstract class Effect<T extends Component> {
    protected T target;
    public Effect(T target){
        this.target = target;
    }

}
