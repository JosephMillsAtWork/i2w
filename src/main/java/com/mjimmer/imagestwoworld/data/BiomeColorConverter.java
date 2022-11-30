package com.mjimmer.imagestwoworld.data;

import com.mjimmer.imagestwoworld.I2W;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import org.apache.logging.log4j.Level;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class BiomeColorConverter implements ColorConverter<Holder<Biome>> {

    private final Holder<Biome> defaultValue;
    private final Registry<Biome> biomeRegistry;
    private final Map<Integer, Holder<Biome>> biomeColorMap;


    public BiomeColorConverter(Holder<Biome> defaultValue, Registry<Biome> biomeRegistry){
        this.defaultValue = defaultValue;
        this.biomeRegistry = biomeRegistry;
        this.biomeColorMap = new ConcurrentHashMap<>();
    }

    @Override
    public Holder<Biome> value(int color) {
        int finalColor = color = color&0x00FFFFFF;
        if( !biomeColorMap.containsKey(finalColor)) {
            // I2W.LOGGER.log(Level.INFO, "WARNING the color " + Integer.toHexString( finalColor ) + " Is not in the biomeColorMap !" + finalColor );
            return this.defaultValue;
        }
        Holder<Biome> biome = biomeColorMap.get(finalColor);
        if (biome.value() != null){
            return biome;
        }
        biome = biomeColorMap.entrySet().stream().sequential().min(
                Comparator.comparingDouble( (bt1) ->
                        getColorDiff( finalColor, bt1.getKey() )
                )
        ).get().getValue();

        I2W.LOGGER.log(Level.DEBUG, "Found un mapped color code " + Integer.toHexString(color)
                + "! Mapping it to similar color!");

        registerBiome(finalColor, biome );
        return biome;
    }

    @Override
    public Holder<Biome> defaultValue(int biomeX,int biomeZ) {
            return this.defaultValue;
    }

    public void registerBiome(int color, Holder<Biome> biome){
        if ( !biomeColorMap.containsKey(color) ) {
            biomeColorMap.put(color, biome );
        }
    }

    private double getColorDiff(int RGB, int btRGB){
        return square(((RGB)%256)-((btRGB)%256)) + square(((RGB>>8)%256)-((btRGB>>8)%256)) + square(((RGB>>16)%256)-((btRGB>>16)%256));
    }
    private float square(float f){
        return f*f;
    }
}
