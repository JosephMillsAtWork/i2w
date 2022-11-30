package com.mjimmer.imagestwoworld.config;

import com.mjimmer.imagestwoworld.I2W;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import org.apache.logging.log4j.Level;


//import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class I2WConfigHelper {
    public static I2WConfig config;
    public static I2WConfig.GeneralDataConfig GENERAL;
    public static I2WConfig.HeightMapDataConfig HEIGHTMAP;
    public static I2WConfig.BiomeDataConfig BIOMES;

    public static I2WConfig.StructuresDataConfig STRUCTURES;
    public static I2WConfig.ExtraDataConfig EXTRA;
    public void initialize(){
        AutoConfig.register(I2WConfig.class, GsonConfigSerializer::new);
        this.config = AutoConfig.getConfigHolder(I2WConfig.class).getConfig();

        this.GENERAL = config.generalDataConfig;
        this.HEIGHTMAP = config.heightMapDataConfig;
        this.BIOMES = config.biomeDataConfig;
        this.STRUCTURES = config.structuresDataConfig;
        this.EXTRA = config.extraDataConfig;


        this.mkdirsIfNeeded();
    }

    private void mkdirsIfNeeded(){
        Path genMapDir = Paths.get("",   I2W.CONFIG_PATH, GENERAL.configImagesDir );
        if (!Files.isDirectory(genMapDir)) {
            boolean dir = genMapDir.toFile().mkdirs();
            if(!dir){
                I2W.LOGGER.log(Level.FATAL,"Could not create folder imagetooworld/image !");
            }
        }
        genMapDir = Paths.get("", I2W.CONFIG_PATH, GENERAL.configGenDir);
        if (!Files.isDirectory(genMapDir)) {
            boolean dir = genMapDir.toFile().mkdirs();
            if(!dir){
                I2W.LOGGER.log(Level.FATAL,"Could not create folder imagetooworld/image/gen !");
            }
        }



    }

    ///////////////////////////////////////
    // GENERAL
    ///////////////////////////////////////
    public static String configDir(){return GENERAL.configDir;}
    public static String configGenDir(){return GENERAL.configGenDir;}
    public static String configImagesDir(){return GENERAL.configImagesDir;}
    public static String spwanCords(){return GENERAL.spwanCords; }
    public static float mapScaleSize(){ return GENERAL.mapScaleSize; };
    public static int configSeaLevel(){ return GENERAL.configseaLevel; }
    public static int configBaseLevel(){ return  GENERAL.configBaseLevel; }

    ///////////////////////////////////////
    // HEIGHTMAP
    ///////////////////////////////////////
    public static boolean customHeightMap(){ return HEIGHTMAP.customHeightMap; }
    public static String heightMapName(){ return HEIGHTMAP.heightMapName; }

    ///////////////////////////////////////
    // BIOME
    ///////////////////////////////////////
    public static boolean biomesImageScale(){ return BIOMES.customBiomeImage;}
    public static String biomesImageName(){ return BIOMES.biomesImageName; }
    public static String defaultBiome(){ return BIOMES.defaultBiome; }
    public static int  The_Void_COLOR                     (){ return BIOMES.The_Void_COLOR                     ; }
    public static int  Plains_COLOR                       (){ return BIOMES.Plains_COLOR                       ; }
    public static int  Sunflower_Plains_COLOR             (){ return BIOMES.Sunflower_Plains_COLOR             ; }
    public static int  Snowy_Plains_COLOR                 (){ return BIOMES.Snowy_Plains_COLOR                 ; }
    public static int  Ice_Spikes_COLOR                   (){ return BIOMES.Ice_Spikes_COLOR                   ; }
    public static int  Desert_COLOR                       (){ return BIOMES.Desert_COLOR                       ; }
    public static int  Swamp_COLOR                        (){ return BIOMES.Swamp_COLOR                        ; }
    public static int  MangroveSwamp_COLOR                (){ return BIOMES.MangroveSwamp_COLOR                ; }
    public static int  Forest_COLOR                       (){ return BIOMES.Forest_COLOR                       ; }
    public static int  Flower_Forest_COLOR                (){ return BIOMES.Flower_Forest_COLOR                ; }
    public static int  Birch_Forest_COLOR                 (){ return BIOMES.Birch_Forest_COLOR                 ; }
    public static int  Dark_Forest_COLOR                  (){ return BIOMES.Dark_Forest_COLOR                  ; }
    public static int  Old_Growth_Birch_Forest_COLOR      (){ return BIOMES.Old_Growth_Birch_Forest_COLOR      ; }
    public static int  Old_Growth_Pine_Taiga_COLOR        (){ return BIOMES.Old_Growth_Pine_Taiga_COLOR        ; }
    public static int  Old_Growth_Spruce_Taiga_COLOR      (){ return BIOMES.Old_Growth_Spruce_Taiga_COLOR      ; }
    public static int  Taiga_COLOR                        (){ return BIOMES.Taiga_COLOR                        ; }
    public static int  Snowy_Taiga_COLOR                  (){ return BIOMES.Snowy_Taiga_COLOR                  ; }
    public static int  Savanna_COLOR                      (){ return BIOMES.Savanna_COLOR                      ; }
    public static int  Savanna_Plateau_COLOR              (){ return BIOMES.Savanna_Plateau_COLOR              ; }
    public static int  Windswept_Hills_COLOR              (){ return BIOMES.Windswept_Hills_COLOR              ; }
    public static int  Windswept_Gravelly_Hills_COLOR     (){ return BIOMES.Windswept_Gravelly_Hills_COLOR     ; }
    public static int  Windswept_Forest_COLOR             (){ return BIOMES.Windswept_Forest_COLOR             ; }
    public static int  Windswept_Savanna_COLOR            (){ return BIOMES.Windswept_Savanna_COLOR            ; }
    public static int  Jungle_COLOR                       (){ return BIOMES.Jungle_COLOR                       ; }
    public static int  Sparse_Edge_COLOR                  (){ return BIOMES.Sparse_Edge_COLOR                  ; }
    public static int  Bamboo_Jungle_COLOR                (){ return BIOMES.Bamboo_Jungle_COLOR                ; }
    public static int  Badlands_COLOR                     (){ return BIOMES.Badlands_COLOR                     ; }
    public static int  Eroded_Badlands_COLOR              (){ return BIOMES.Eroded_Badlands_COLOR              ; }
    public static int  Wooded_Badlands_COLOR              (){ return BIOMES.Wooded_Badlands_COLOR              ; }
    public static int  Meadow_COLOR                       (){ return BIOMES.Meadow_COLOR                       ; }
    public static int  Grove_COLOR                        (){ return BIOMES.Grove_COLOR                        ; }
    public static int  Snowy_Slopes_COLOR                 (){ return BIOMES.Snowy_Slopes_COLOR                 ; }
    public static int  Frozen_Peaks_COLOR                 (){ return BIOMES.Frozen_Peaks_COLOR                 ; }
    public static int  Jagged_Peaks_COLOR                 (){ return BIOMES.Jagged_Peaks_COLOR                 ; }
    public static int  Stony_Peaks_COLOR                  (){ return BIOMES.Stony_Peaks_COLOR                  ; }
    public static int  River_COLOR                        (){ return BIOMES.River_COLOR                        ; }
    public static int  Frozen_River_COLOR                 (){ return BIOMES.Frozen_River_COLOR                 ; }
    public static int  Beach_COLOR                        (){ return BIOMES.Beach_COLOR                        ; }
    public static int  Snowy_Beach_COLOR                  (){ return BIOMES.Snowy_Beach_COLOR                  ; }
    public static int  Stony_Shore_COLOR                  (){ return BIOMES.Stony_Shore_COLOR                  ; }
    public static int  Warm_Ocean_COLOR                   (){ return BIOMES.Warm_Ocean_COLOR                   ; }
    public static int  Lukewarm_Ocean_COLOR               (){ return BIOMES.Lukewarm_Ocean_COLOR               ; }
    public static int  Deep_Lukewarm_Ocean_COLOR          (){ return BIOMES.Deep_Lukewarm_Ocean_COLOR          ; }
    public static int  Ocean_COLOR                        (){ return BIOMES.Ocean_COLOR                        ; }
    public static int  Deep_Ocean_COLOR                   (){ return BIOMES.Deep_Ocean_COLOR                   ; }
    public static int  Cold_Ocean_COLOR                   (){ return BIOMES.Cold_Ocean_COLOR                   ; }
    public static int  Deep_Cold_Ocean_COLOR              (){ return BIOMES.Deep_Cold_Ocean_COLOR              ; }
    public static int  Frozen_Ocean_COLOR                 (){ return BIOMES.Frozen_Ocean_COLOR                 ; }
    public static int  Deep_Frozen_Ocean_COLOR            (){ return BIOMES.Deep_Frozen_Ocean_COLOR            ; }
    public static int  Mushroom_Fields_COLOR              (){ return BIOMES.Mushroom_Fields_COLOR              ; }
    public static int  Dripstone_Caves_COLOR              (){ return BIOMES.Dripstone_Caves_COLOR              ; }
    public static int  Lush_Caves_COLOR                   (){ return BIOMES.Lush_Caves_COLOR                   ; }
    public static int  Deep_Dark_COLOR                    (){ return BIOMES.Deep_Dark_COLOR                    ; }
    public static int  Nether_Wastes_COLOR                (){ return BIOMES.Nether_Wastes_COLOR                ; }
    public static int  Warped_Forest_COLOR                (){ return BIOMES.Warped_Forest_COLOR                ; }
    public static int  Crimson_Forest_COLOR               (){ return BIOMES.Crimson_Forest_COLOR               ; }
    public static int  Soul_Sand_Valley_COLOR             (){ return BIOMES.Soul_Sand_Valley_COLOR             ; }
    public static int  Basalt_Deltas_COLOR                (){ return BIOMES.Basalt_Deltas_COLOR                ; }
    public static int  The_End_COLOR                      (){ return BIOMES.The_End_COLOR                      ; }
    public static int  End_HighLands_COLOR                (){ return BIOMES.End_HighLands_COLOR                ; }
    public static int  End_Midlands_COLOR                 (){ return BIOMES.End_Midlands_COLOR                 ; }
    public static int  Small_End_Island_COLOR             (){ return BIOMES.Small_End_Island_COLOR             ; }
    public static int  End_Barrens_COLOR                  (){ return BIOMES.End_Barrens_COLOR                  ; }


    ///////////////////////////////////////
    // STRUCTURES
    ///////////////////////////////////////
    public static boolean customStructures(){ return STRUCTURES.customStructures; }
    public static String customStructuresImage(){ return STRUCTURES.customStructuresImage; }
    public static int PILLAGER_OUTPOST_COLOR            (){ return STRUCTURES.PILLAGER_OUTPOST_COLOR         ;}
    public static int MINESHAFT_COLOR                   (){ return STRUCTURES.MINESHAFT_COLOR                ;}
    public static int MINESHAFT_MESA_COLOR              (){ return STRUCTURES.MINESHAFT_MESA_COLOR           ;}
    public static int WOODLAND_MANSION_COLOR            (){ return STRUCTURES.WOODLAND_MANSION_COLOR         ;}
    public static int JUNGLE_TEMPLE_COLOR               (){ return STRUCTURES.JUNGLE_TEMPLE_COLOR            ;}
    public static int DESERT_PYRAMID_COLOR              (){ return STRUCTURES.DESERT_PYRAMID_COLOR           ;}
    public static int IGLOO_COLOR                       (){ return STRUCTURES.IGLOO_COLOR                    ;}
    public static int SHIPWRECK_COLOR                   (){ return STRUCTURES.SHIPWRECK_COLOR                ;}
    public static int SHIPWRECK_BEACHED_COLOR           (){ return STRUCTURES.SHIPWRECK_BEACHED_COLOR        ;}
    public static int SWAMP_HUT_COLOR                   (){ return STRUCTURES.SWAMP_HUT_COLOR                ;}
    public static int STRONGHOLD_COLOR                  (){ return STRUCTURES.STRONGHOLD_COLOR               ;}
    public static int OCEAN_MONUMENT_COLOR              (){ return STRUCTURES.OCEAN_MONUMENT_COLOR           ;}
    public static int OCEAN_RUIN_COLD_COLOR             (){ return STRUCTURES.OCEAN_RUIN_COLD_COLOR          ;}
    public static int OCEAN_RUIN_WARM_COLOR             (){ return STRUCTURES.OCEAN_RUIN_WARM_COLOR          ;}
    public static int FORTRESS_COLOR                    (){ return STRUCTURES.FORTRESS_COLOR                 ;}
    public static int NETHER_FOSSIL_COLOR               (){ return STRUCTURES.NETHER_FOSSIL_COLOR            ;}
    public static int END_CITY_COLOR                    (){ return STRUCTURES.END_CITY_COLOR                 ;}
    public static int BURIED_TREASURE_COLOR             (){ return STRUCTURES.BURIED_TREASURE_COLOR          ;}
    public static int BASTION_REMNANT_COLOR             (){ return STRUCTURES.BASTION_REMNANT_COLOR          ;}
    public static int VILLAGE_PLAINS_COLOR              (){ return STRUCTURES.VILLAGE_PLAINS_COLOR           ;}
    public static int VILLAGE_DESERT_COLOR              (){ return STRUCTURES.VILLAGE_DESERT_COLOR           ;}
    public static int VILLAGE_SAVANNA_COLOR             (){ return STRUCTURES.VILLAGE_SAVANNA_COLOR          ;}
    public static int VILLAGE_SNOWY_COLOR               (){ return STRUCTURES.VILLAGE_SNOWY_COLOR            ;}
    public static int VILLAGE_TAIGA_COLOR               (){ return STRUCTURES.VILLAGE_TAIGA_COLOR            ;}
    public static int RUINED_PORTAL_STANDARD_COLOR      (){ return STRUCTURES.RUINED_PORTAL_STANDARD_COLOR   ;}
    public static int RUINED_PORTAL_DESERT_COLOR        (){ return STRUCTURES.RUINED_PORTAL_DESERT_COLOR     ;}
    public static int RUINED_PORTAL_JUNGLE_COLOR        (){ return STRUCTURES.RUINED_PORTAL_JUNGLE_COLOR     ;}
    public static int RUINED_PORTAL_SWAMP_COLOR         (){ return STRUCTURES.RUINED_PORTAL_SWAMP_COLOR      ;}
    public static int RUINED_PORTAL_MOUNTAIN_COLOR      (){ return STRUCTURES.RUINED_PORTAL_MOUNTAIN_COLOR   ;}
    public static int RUINED_PORTAL_OCEAN_COLOR         (){ return STRUCTURES.RUINED_PORTAL_OCEAN_COLOR      ;}
    public static int RUINED_PORTAL_NETHER_COLOR        (){ return STRUCTURES.RUINED_PORTAL_NETHER_COLOR     ;}
    public static int ANCIENT_CITY_COLOR                (){ return STRUCTURES.ANCIENT_CITY_COLOR             ;}
    public static int NONE_COLOR                        (){ return STRUCTURES.NONE_COLOR                     ;}


    ///////////////////////////////////////
    // EXTRAS
    ///////////////////////////////////////
    public static int segmentImageWidth(){ return EXTRA.segmentImageWidth; }
    public static int segmentImageHeight(){ return EXTRA.segmentImageHeight; }
    public static int imageCacheSize(){ return EXTRA.imageCacheSize; }
    public static boolean customOverWorldGenerator(){return EXTRA.customOverWorldGenerator;}
    public static boolean customNetherGenerator(){return EXTRA.customNetherGenerator;}
    public static boolean customEndGenerator(){ return EXTRA.customEndGenerator; }






    //    public static List<BiomeIDAndRGBPair> customBiomes(){return config.customBiomes; }
    //    public static List<StructureAndRGBPair> customStructuresRGB(){ return config.customStructuresRGB ;}
}
