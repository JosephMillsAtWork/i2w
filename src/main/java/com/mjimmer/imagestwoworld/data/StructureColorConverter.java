package com.mjimmer.imagestwoworld.data;

import com.mjimmer.imagestwoworld.I2W;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.apache.logging.log4j.Level;

import java.util.Comparator;
import java.util.Map;

public class StructureColorConverter implements ColorConverter<Holder<Structure>> {
    private final Map<Integer, Holder<Structure> > structureColorMap;
    public StructureColorConverter(){
        this.structureColorMap = new Int2ObjectOpenHashMap<>();
    }

    public Map<Integer, Holder<Structure>> getStructureColorMap(){ return this.structureColorMap;}

    @Override
    public Holder<Structure> value(int color) {
        int finalColor = color &0x00FFFFFF;
        if( !structureColorMap.containsKey(finalColor) || finalColor == 0){
            return null;
        }
        Holder<Structure> configuredStructureFeature = structureColorMap.get(finalColor);
        if( configuredStructureFeature != null ){
            return configuredStructureFeature;
        }
        configuredStructureFeature = structureColorMap.entrySet().stream().sequential().min(Comparator.comparingDouble(
                (bt1) -> getColorDiff(finalColor, bt1.getKey())
        )).get().getValue();

        I2W.LOGGER.log(Level.DEBUG, "Found unmapped color code "
                + Integer.toHexString(color) + "! Mapping it to similar color!");

        if( finalColor != 0 ) {
            registerStructure(finalColor, configuredStructureFeature );
        }
        return configuredStructureFeature;
    }


    @Override
    public Holder<Structure> defaultValue(int x, int z) {
        return StructureColors.NONE.configuredStructureFeature;
    }

    public boolean registerStructure(int color,  Holder<Structure> structure){
        boolean ret = false;
        if (!structureColorMap.containsKey(color) ){
            structureColorMap.put(color, structure);
            ret = true;
        }
        return ret;
    }

    // FIXME this is two heavy and needs to be removed.
    //  If there is a image pixel error then the structure should just be skipped and logged
    private double getColorDiff(int RGB, int btRGB){
        return square(((RGB)%256)-((btRGB)%256)) + square(((RGB>>8)%256)-((btRGB>>8)%256)) + square(((RGB>>16)%256)-((btRGB>>16)%256));
    }
    private float square(float f) {
        return f * f;
    }
}
