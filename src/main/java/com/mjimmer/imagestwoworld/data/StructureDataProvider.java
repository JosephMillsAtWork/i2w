package com.mjimmer.imagestwoworld.data;

import com.mjimmer.imagestwoworld.image.ImageHandler;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;

public final class StructureDataProvider {
    private final ColorConverter<Holder<Structure>> colorConverter;
    private final ImageHandler imageHandler;

    private float scale;
    private final int width;
    private final int height;
    private final int halfWidth;
    private final int halfHeight;

    public StructureDataProvider(ColorConverter<Holder<Structure>> colorConverter,  float scale, String path){
        this.colorConverter = colorConverter;
        this.imageHandler = new ImageHandler(path,"structures");
        this.scale = scale;
        width = imageHandler.getWidth();
        height = imageHandler.getHeight();
        halfWidth = width / 2;
        halfHeight = height / 2;
    }
    public boolean isInImage(int x, int y){
        int centeredX = (int)(x / (scale)) + halfWidth;
        int centeredY = (int)(y / (scale)) + halfHeight;

        return !(centeredX < 0  || centeredY < 0 ||
                centeredX > width - 1 || centeredY > height - 1);
    }

    public @Nullable Holder<Structure> data(int x, int y){
        int centeredX = (int)(x/scale) + halfWidth;
        int centeredY = (int)(y/scale) + halfHeight;
        if ( !isInImage(x, y) ) {
            centeredX = centeredX >= 0 ? centeredX%width :
                    -width*((centeredX+1)/width)+width+centeredX;
            centeredY = centeredY >= 0 ? centeredY % height :
                    -height*((centeredY+1)/height)+height+centeredY;
            int rgb = imageHandler.getRGB(centeredX, centeredY);
            return colorConverter.value(rgb);
        }
        int rgb = imageHandler.getRGB(centeredX, centeredY);
        return colorConverter.value(rgb);
    }

    public ImageHandler imageManipulator(){return this.imageHandler;}

}
