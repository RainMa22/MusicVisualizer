package me.rainma22.musicvisualizer.intermediateimage.Effects;

import java.util.List;
import me.rainma22.musicvisualizer.intermediateimage.Component;
import me.rainma22.musicvisualizer.intermediateimage.EffectApplier;
import me.rainma22.musicvisualizer.intermediateimage.Resources.ResourceManager;

/**
 * A class that represent an Effect to be applied to an Image
 * which is mindful of the provided resources
 * @param <T> the type of the component Target
 **/

public abstract class ResourcefulEffect<T extends Component> {
    protected final T target;
    protected List<String> resourceIds;
    /**
     * Constructor, creates a ResourcefulEffect
     * @param target the target Component
     * @param resourceIds The list of resourceIds used by this Effect
     **/
    public ResourcefulEffect(T target, List<String> resourceIds) {
        this.target = target;
        this.resourceIds = List.copyOf(resourceIds);
    }
    
    /**
     * 
     * @param currentFrame the index of the current frame, reserved for 
     * time-aware effects
     * @oaram applier the applier of the effect
     * @param resMan the ResourceManager
     * @return a **copy** of the component with the effect applied  
     **/
    public abstract T apply(int currentFrame, 
            EffectApplier applier,
            ResourceManager resMan);
    
    /**
     * @return the name of the Effect 
     */
    public abstract String getName();
    
    @Override
    public String toString(){
        return String.join(" ", getName(),
                String.join(" ", resourceIds));
    }
}
