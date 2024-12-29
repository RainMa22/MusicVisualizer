package me.rainma22.intermediateimage.Effects.ContainerEffects;

import java.util.List;
import me.rainma22.intermediateimage.ColorProvider;
import me.rainma22.intermediateimage.ContainerComponent;
import me.rainma22.intermediateimage.EffectApplier;
import me.rainma22.intermediateimage.Effects.ResourcefulEffect;
import me.rainma22.intermediateimage.Resources.Image;
import me.rainma22.intermediateimage.Resources.ResourceManager;

/**
 * Represents a background image effect
 */
public class BackgroundImage extends ResourcefulEffect<ContainerComponent> {

    private String imageId = null;

    public BackgroundImage(ContainerComponent target, List<String> resourceIds) {
        super(target, resourceIds);
        if (!resourceIds.isEmpty()) {
            imageId = resourceIds.getFirst();
        }
    }

//    /**
//     *
//     * @param index the index of frame
//     * @param resMan the resource manager
//     * @return a copy of target with the background image set to the image
//     * associated with given imageId, simply returns a copy if resourceIds is
//     * not given or the first of the given resourceIds does not refer to an
//     * Image;
//     */
//    @Override
//    public ContainerComponent apply(int index, ResourceManager resMan) {
//        if (imageId == null) {
//            return target.copy();
//        }
//        Image image = resMan.getImage(imageId);
//        if (image == null) {
//            return target.copy();
//        }
//
//        ContainerComponent result = target.copy();
//        
//        result.setBackgroundColor_rgba
//                (ColorProvider.ofImage(image));
//        return result;
//    }

    @Override
    public String getName() {
        return "BACKGROUND_IMG";
    }

    @Override
    public ContainerComponent apply(int currentFrame, EffectApplier applier) {
        return applier.applyBackgroundImage(currentFrame, this);
    }

}
