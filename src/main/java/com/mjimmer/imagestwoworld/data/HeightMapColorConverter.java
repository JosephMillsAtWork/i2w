package com.mjimmer.imagestwoworld.data;

import com.mjimmer.imagestwoworld.I2W;

public class HeightMapColorConverter implements ColorConverter<Integer>{

    private final int defaultHeight;

    public HeightMapColorConverter(int defaultValue)
    {
        this.defaultHeight = defaultValue;
    }

    @Override
    public Integer value(int color) {
        Integer ret = color&0xFF;
        if (I2W.CONFIGS.GENERAL.configBaseLevel < 0 ){
            ret = (ret - I2W.CONFIGS.GENERAL.configBaseLevel);
        }else{
            ret = (ret + I2W.CONFIGS.GENERAL.configBaseLevel);
        }
        return ret ;
    }

    @Override
    public Integer defaultValue(int biomeX,int biomeZ) {
        return defaultHeight;
    }
}
