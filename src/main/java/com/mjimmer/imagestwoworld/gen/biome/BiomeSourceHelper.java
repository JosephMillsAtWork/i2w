package com.mjimmer.imagestwoworld.gen.biome;

import com.mjimmer.imagestwoworld.I2W;
import com.mjimmer.imagestwoworld.data.BiomeColorConverter;
import com.mjimmer.imagestwoworld.data.BiomeColors;
import com.mjimmer.imagestwoworld.data.BiomeDataProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class BiomeSourceHelper {
    private final BiomeColorConverter biomeColorConverter;
    private final BiomeDataProvider biomeDataProvider;
    public BiomeSourceHelper() {
        Registry<Biome> biomeRegistry = BuiltinRegistries.BIOME;
        // TODO read the settings and get the default biome.
        this.biomeColorConverter = new BiomeColorConverter(
                biomeRegistry.getHolderOrThrow( Biomes.OCEAN ),
                biomeRegistry
        );
        this.biomeDataProvider = new BiomeDataProvider(
                this.biomeColorConverter,
                I2W.CONFIGS.GENERAL.mapScaleSize,
                I2W.CONFIGS.BIOMES.biomesImageName
        );
    }
    public boolean scanImage(){
        return biomeDataProvider.imageManipulator().segmentImage("biomes");
    }
    public BiomeColorConverter getBiomeColorConverter(){return biomeColorConverter;}
    public BiomeDataProvider getBiomeDataProvider(){return biomeDataProvider;}

    public void registerBiomes() {
        for ( BiomeColors biomeColor : BiomeColors.values() ) {
            Holder<Biome> biome = biomeColor.getBiome();
            this.biomeColorConverter.registerBiome( biomeColor.getRGB(), biome );
        }
    }


    public Holder<Biome> getDefaultBiome(){
        ResourceLocation bID = getIdFromString( I2W.CONFIGS.BIOMES.defaultBiome );
        if( bID == null ) {
            return BuiltinRegistries.BIOME.getOrCreateHolderOrThrow( Biomes.OCEAN );
        }
        Biome bKey = BuiltinRegistries.BIOME.get( bID );
        Holder<Biome> biome = BuiltinRegistries.BIOME.getOrCreateHolderOrThrow( BuiltinRegistries.BIOME.getResourceKey( bKey ).get() );
        return biome.value() != null
                ? biome
                : BuiltinRegistries.BIOME.getOrCreateHolderOrThrow( Biomes.OCEAN );
    }

    private ResourceLocation getIdFromString(String biomeID) {
        String[] str = biomeID.toLowerCase().split(":");
        if (str.length != 2) {
            return null;
        } else return new ResourceLocation(str[0], str[1]);
    }

}
