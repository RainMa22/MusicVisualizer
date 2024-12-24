package me.rainma22.musicvisualizer.Image.Effects;

import java.util.List;
import me.rainma22.musicvisualizer.Image.Component;
import me.rainma22.musicvisualizer.Image.Resources.Audio;

public class AudioEffect<T extends Component> extends Effect<T> {
    protected List<String> effectIds;
    public AudioEffect(T target, List<String> eids) {
        super(target);
        effectIds = List.copyOf(eids);
    }
}
