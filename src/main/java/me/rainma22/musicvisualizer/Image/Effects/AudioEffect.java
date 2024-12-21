package me.rainma22.musicvisualizer.Image.Effects;

import me.rainma22.musicvisualizer.Image.Component;
import me.rainma22.musicvisualizer.Image.Resources.Audio;

public class AudioEffect<T extends Component> extends Effect<T> {
    protected Audio audio;
    public AudioEffect(T target, Audio audio) {
        super(target);
        this.audio = audio;
    }
}
