package me.rainma22.musicvisualizer.Image.Effects;

import java.util.List;
import me.rainma22.musicvisualizer.Image.Component;
import me.rainma22.musicvisualizer.Image.Resources.ResourceManager;

public abstract class ResourcefulEffect<T extends Component> {
    protected final T target;
    protected List<String> effectIds;
    public ResourcefulEffect(T target, List<String> eids) {
        this.target = target;
        effectIds = List.copyOf(eids);
    }
    
    public abstract T apply(int index, ResourceManager resMan);
}
