package com.mjimmer.imagestwoworld.gen.heightmap;

import com.mjimmer.imagestwoworld.I2W;
import com.mjimmer.imagestwoworld.data.HeightDataProvider;
import com.mjimmer.imagestwoworld.data.HeightMapColorConverter;


public class HeightMapHelper {
    private final HeightMapColorConverter heightMapColorConverter;
    private final HeightDataProvider heightMapDataProvider;

    public HeightMapHelper() {
        this.heightMapColorConverter = new HeightMapColorConverter(I2W.CONFIGS.GENERAL.configseaLevel);
        this.heightMapDataProvider = new HeightDataProvider(
                this.heightMapColorConverter,
                I2W.CONFIGS.GENERAL.mapScaleSize,
                I2W.CONFIGS.HEIGHTMAP.heightMapName
        );
    }

    public int getHeight(int x, int z){
        return getHeightMapDataProvider().data(x,z);
    }
    public boolean isInImage(int x,int z){ return getHeightMapDataProvider().isInImage(x,z);}

    public HeightMapColorConverter getHeightMapColorConverter() {
        return heightMapColorConverter;
    }

    public HeightDataProvider getHeightMapDataProvider() {
        return heightMapDataProvider;
    }

    public boolean scanImage(){ return this.getHeightMapDataProvider().imageManipulator().segmentImage( "heightmap" ); }
}
