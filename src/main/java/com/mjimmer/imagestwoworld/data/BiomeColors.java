package com.mjimmer.imagestwoworld.data;
import com.mjimmer.imagestwoworld.config.I2WConfigHelper;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public enum BiomeColors{
    The_Void(                   I2WConfigHelper.The_Void_COLOR                     (),               Biomes.THE_VOID ),
    Plains(                     I2WConfigHelper.Plains_COLOR                       (),               Biomes.PLAINS),
    Sunflower_Plains(           I2WConfigHelper.Sunflower_Plains_COLOR             (),               Biomes.SUNFLOWER_PLAINS),
    Snowy_Plains(               I2WConfigHelper.Snowy_Plains_COLOR                 (),               Biomes.SNOWY_PLAINS),
    Ice_Spikes(                 I2WConfigHelper.Ice_Spikes_COLOR                   (),               Biomes.ICE_SPIKES),
    Desert(                     I2WConfigHelper.Desert_COLOR                       (),               Biomes.DESERT),
    Swamp(                      I2WConfigHelper.Swamp_COLOR                        (),               Biomes.SWAMP),
    MangroveSwamp(              I2WConfigHelper.MangroveSwamp_COLOR                (),               Biomes.MANGROVE_SWAMP),
    Forest(                     I2WConfigHelper.Forest_COLOR                       (),               Biomes.FOREST),
    Flower_Forest(              I2WConfigHelper.Flower_Forest_COLOR                (),               Biomes.FLOWER_FOREST),
    Birch_Forest(               I2WConfigHelper.Birch_Forest_COLOR                 (),               Biomes.BIRCH_FOREST),
    Dark_Forest(                I2WConfigHelper.Dark_Forest_COLOR                  (),               Biomes.DARK_FOREST),
    Old_Growth_Birch_Forest(    I2WConfigHelper.Old_Growth_Birch_Forest_COLOR      (),               Biomes.OLD_GROWTH_BIRCH_FOREST),
    Old_Growth_Pine_Taiga(      I2WConfigHelper.Old_Growth_Pine_Taiga_COLOR        (),               Biomes.OLD_GROWTH_PINE_TAIGA),
    Old_Growth_Spruce_Taiga(    I2WConfigHelper.Old_Growth_Spruce_Taiga_COLOR      (),               Biomes.OLD_GROWTH_SPRUCE_TAIGA),
    Taiga(                      I2WConfigHelper.Taiga_COLOR                        (),               Biomes.TAIGA),
    Snowy_Taiga(                I2WConfigHelper.Snowy_Taiga_COLOR                  (),               Biomes.SNOWY_TAIGA),
    Savanna(                    I2WConfigHelper.Savanna_COLOR                      (),               Biomes.SAVANNA),
    Savanna_Plateau(            I2WConfigHelper.Savanna_Plateau_COLOR              (),               Biomes.SAVANNA_PLATEAU),
    Windswept_Hills(            I2WConfigHelper.Windswept_Hills_COLOR              (),               Biomes.WINDSWEPT_HILLS),
    Windswept_Gravelly_Hills(   I2WConfigHelper.Windswept_Gravelly_Hills_COLOR     (),               Biomes.WINDSWEPT_GRAVELLY_HILLS),
    Windswept_Forest(           I2WConfigHelper.Windswept_Forest_COLOR             (),               Biomes.WINDSWEPT_FOREST),
    Windswept_Savanna(          I2WConfigHelper.Windswept_Savanna_COLOR            (),               Biomes.WINDSWEPT_SAVANNA),
    Jungle(                     I2WConfigHelper.Jungle_COLOR                       (),               Biomes.JUNGLE),
    Sparse_Edge(                I2WConfigHelper.Sparse_Edge_COLOR                  (),               Biomes.SPARSE_JUNGLE),
    Bamboo_Jungle(              I2WConfigHelper.Bamboo_Jungle_COLOR                (),               Biomes.BAMBOO_JUNGLE),
    Badlands(                   I2WConfigHelper.Badlands_COLOR                     (),               Biomes.BADLANDS),
    Eroded_Badlands(            I2WConfigHelper.Eroded_Badlands_COLOR              (),               Biomes.ERODED_BADLANDS),
    Wooded_Badlands(            I2WConfigHelper.Wooded_Badlands_COLOR              (),               Biomes.WOODED_BADLANDS),
    Meadow(                     I2WConfigHelper.Meadow_COLOR                       (),               Biomes.MEADOW),
    Grove(                      I2WConfigHelper.Grove_COLOR                        (),               Biomes.GROVE),
    Snowy_Slopes(               I2WConfigHelper.Snowy_Slopes_COLOR                 (),               Biomes.SNOWY_SLOPES),
    Frozen_Peaks(               I2WConfigHelper.Frozen_Peaks_COLOR                 (),               Biomes.FROZEN_PEAKS),
    Jagged_Peaks(               I2WConfigHelper.Jagged_Peaks_COLOR                 (),               Biomes.JAGGED_PEAKS),
    Stony_Peaks(                I2WConfigHelper.Stony_Peaks_COLOR                  (),               Biomes.STONY_PEAKS),
    River(                      I2WConfigHelper.River_COLOR                        (),               Biomes.RIVER),
    Frozen_River(               I2WConfigHelper.Frozen_River_COLOR                 (),               Biomes.FROZEN_RIVER),
    Beach(                      I2WConfigHelper.Beach_COLOR                        (),               Biomes.BEACH),
    Snowy_Beach(                I2WConfigHelper.Snowy_Beach_COLOR                  (),               Biomes.SNOWY_BEACH),
    Stony_Shore(                I2WConfigHelper.Stony_Shore_COLOR                  (),               Biomes.STONY_SHORE),
    Warm_Ocean(                 I2WConfigHelper.Warm_Ocean_COLOR                   (),               Biomes.WARM_OCEAN),
    Lukewarm_Ocean(             I2WConfigHelper.Lukewarm_Ocean_COLOR               (),               Biomes.LUKEWARM_OCEAN),
    Deep_Lukewarm_Ocean(        I2WConfigHelper.Deep_Lukewarm_Ocean_COLOR          (),               Biomes.DEEP_LUKEWARM_OCEAN),
    Ocean(                      I2WConfigHelper.Ocean_COLOR                        (),               Biomes.OCEAN),
    Deep_Ocean(                 I2WConfigHelper.Deep_Ocean_COLOR                   (),               Biomes.DEEP_OCEAN),
    Cold_Ocean(                 I2WConfigHelper.Cold_Ocean_COLOR                   (),               Biomes.COLD_OCEAN),
    Deep_Cold_Ocean(            I2WConfigHelper.Deep_Cold_Ocean_COLOR              (),               Biomes.DEEP_COLD_OCEAN),
    Frozen_Ocean(               I2WConfigHelper.Frozen_Ocean_COLOR                 (),               Biomes.FROZEN_OCEAN),
    Deep_Frozen_Ocean(          I2WConfigHelper.Deep_Frozen_Ocean_COLOR            (),               Biomes.DEEP_FROZEN_OCEAN),
    Mushroom_Fields(            I2WConfigHelper.Mushroom_Fields_COLOR              (),               Biomes.MUSHROOM_FIELDS),
    Dripstone_Caves(            I2WConfigHelper.Dripstone_Caves_COLOR              (),               Biomes.DRIPSTONE_CAVES),
    Lush_Caves(                 I2WConfigHelper.Lush_Caves_COLOR                   (),               Biomes.LUSH_CAVES),
    Deep_Dark(                  I2WConfigHelper.Deep_Dark_COLOR                    (),               Biomes.DEEP_DARK),
    Nether_Wastes(              I2WConfigHelper.Nether_Wastes_COLOR                (),               Biomes.NETHER_WASTES),
    Warped_Forest(              I2WConfigHelper.Warped_Forest_COLOR                (),               Biomes.WARPED_FOREST),
    Crimson_Forest(             I2WConfigHelper.Crimson_Forest_COLOR               (),               Biomes.CRIMSON_FOREST),
    Soul_Sand_Valley(           I2WConfigHelper.Soul_Sand_Valley_COLOR             (),               Biomes.SOUL_SAND_VALLEY),
    Basalt_Deltas(              I2WConfigHelper.Basalt_Deltas_COLOR                (),               Biomes.BASALT_DELTAS        ),
    The_End(                    I2WConfigHelper.The_End_COLOR                      (),               Biomes.THE_END              ),
    End_HighLands(              I2WConfigHelper.End_HighLands_COLOR                (),               Biomes.END_HIGHLANDS        ),
    End_Midlands(               I2WConfigHelper.End_Midlands_COLOR                 (),               Biomes.END_MIDLANDS         ),
    Small_End_Island(           I2WConfigHelper.Small_End_Island_COLOR             (),               Biomes.SMALL_END_ISLANDS    ),
    End_Barrens(                I2WConfigHelper.End_Barrens_COLOR                  (),               Biomes.END_BARRENS          );

    private final int rgb; // Examples 0x8DB360;
    private final Holder<Biome> biome;

    BiomeColors(int rgb, ResourceKey<Biome>  resourceKey ){
        this.rgb = rgb;
        this.biome = BuiltinRegistries.BIOME.getHolderOrThrow( resourceKey );
    }
    public Holder<Biome>  getBiome(){
        return biome;
    }
    public int getRGB(){
        return rgb;
    }
}