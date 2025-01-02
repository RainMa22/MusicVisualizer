package me.rainma22.intermediateimage.Effects;

import me.rainma22.intermediateimage.Component;
import me.rainma22.intermediateimage.EffectApplier;

/**
 * A class that represent an Effect to be applied to an Image which is mindful
 * of the provided resources
 *
 * @param <T> the type of the component Target
 *
 */
public abstract class ResourcefulEffect<T extends Component> implements Cloneable{

    protected T target;

    /**
     * Constructor, creates a ResourcefulEffect
     *
     * @param target the target Component
     *
     */
    public ResourcefulEffect(T target) {
        this.target = target;
    }

    public T getTarget() {
        return target;
    }

    public void setTarget(T target) {
        this.target = target;
    }


    /**
     *
     * @param currentFrame the index of the current frame, reserved for
     * time-aware effects
     * @oaram applier the applier of the effect
     * @return a **copy** of the component with the effect applied
     *
     */
    public abstract T apply(int currentFrame,
            EffectApplier applier);

    /**
     * @return the name of the Effect
     */
    public abstract String getName();

    public ResourcefulEffect<T> copy(){
        try {
            ResourcefulEffect<T> result = (ResourcefulEffect<T>) clone();
            return result;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.join(" ", getName());
    }
}
