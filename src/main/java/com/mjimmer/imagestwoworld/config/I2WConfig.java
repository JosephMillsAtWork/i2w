package com.mjimmer.imagestwoworld.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "i2w" )
public class I2WConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public GeneralDataConfig generalDataConfig = new GeneralDataConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public HeightMapDataConfig heightMapDataConfig = new HeightMapDataConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public BiomeDataConfig biomeDataConfig = new BiomeDataConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public StructuresDataConfig structuresDataConfig = new StructuresDataConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public ExtraDataConfig extraDataConfig = new ExtraDataConfig();

    /////////////////
    // HEIGHTMAP
    public static class GeneralDataConfig {
        @Comment("Root configuration folder")
        public String configDir = "i2w";
        @Comment("Generation directory")
        public String configGenDir = "gen";
        @Comment("Images Directory")
        public String configImagesDir = "image";

        @Comment("spawn area example \"0,64,0\" ")
        public static String spwanCords = "0,64,0";

        @Comment("This is the base color of your heightmap at the color 0x00")
        public int configBaseLevel = 0;

        @Comment("custom sea level")
        public int configseaLevel = 63;

        @Comment("scale of the height map.")
        public float mapScaleSize = 1;
    }


    /////////////////
    // HEIGHTMAP
    public static class HeightMapDataConfig  {
        @Comment("use a custom height map")
        public boolean customHeightMap = true;
        @Comment("the images name")
        public String heightMapName = "heightmap.png";
    }

    /////////////////
    /// BIOMES


    public static class BiomeDataConfig {
        @Comment("use a custom biome image")
        public boolean customBiomeImage = true;
        @Comment("Biome Image to read from")
        public String biomesImageName = "biomes.png";
        @Comment("Internal default biome for generator")
        public String defaultBiome = "minecraft:ocean";

        @ConfigEntry.ColorPicker
        public int The_Void_COLOR                     =   0x000000     ;
        @ConfigEntry.ColorPicker
        public int Plains_COLOR                       =   0x8DB360     ;
        @ConfigEntry.ColorPicker
        public int Sunflower_Plains_COLOR             =   0xB5DB88     ;
        @ConfigEntry.ColorPicker
        public int Snowy_Plains_COLOR                 =   0xFFFFFF     ;
        @ConfigEntry.ColorPicker
        public int Ice_Spikes_COLOR                   =   0xB4DCDC     ;
        @ConfigEntry.ColorPicker
        public int Desert_COLOR                       =   0xFA9418     ;
        @ConfigEntry.ColorPicker
        public int Swamp_COLOR                        =   0x07F9B2     ;
        @ConfigEntry.ColorPicker
        public int MangroveSwamp_COLOR                =   0x07F9C2     ;
        @ConfigEntry.ColorPicker
        public int Forest_COLOR                       =   0x056621     ;
        @ConfigEntry.ColorPicker
        public int Flower_Forest_COLOR                =   0x2D8E49     ;
        @ConfigEntry.ColorPicker
        public int Birch_Forest_COLOR                 =   0x307444     ;
        @ConfigEntry.ColorPicker
        public int Dark_Forest_COLOR                  =   0x40511A     ;
        @ConfigEntry.ColorPicker
        public int Old_Growth_Birch_Forest_COLOR      =   0x589C6C     ;
        @ConfigEntry.ColorPicker
        public int Old_Growth_Pine_Taiga_COLOR        =   0x596651     ;
        @ConfigEntry.ColorPicker
        public int Old_Growth_Spruce_Taiga_COLOR      =   0x818E79     ;
        @ConfigEntry.ColorPicker
        public int Taiga_COLOR                        =   0x0B6659     ;
        @ConfigEntry.ColorPicker
        public int Snowy_Taiga_COLOR                  =   0x31554A     ;
        @ConfigEntry.ColorPicker
        public int Savanna_COLOR                      =   0xBDB25F     ;
        @ConfigEntry.ColorPicker
        public int Savanna_Plateau_COLOR              =   0xA79D64     ;
        @ConfigEntry.ColorPicker
        public int Windswept_Hills_COLOR              =   0x163933     ;
        @ConfigEntry.ColorPicker
        public int Windswept_Gravelly_Hills_COLOR     =   0x888888     ;
        @ConfigEntry.ColorPicker
        public int Windswept_Forest_COLOR             =   0x454F3E     ;
        @ConfigEntry.ColorPicker
        public int Windswept_Savanna_COLOR            =   0xE5DA87     ;
        @ConfigEntry.ColorPicker
        public int Jungle_COLOR                       =   0x537B09     ;
        @ConfigEntry.ColorPicker
        public int Sparse_Edge_COLOR                  =   0x628B17     ;
        @ConfigEntry.ColorPicker
        public int Bamboo_Jungle_COLOR                =   0x768E14     ;
        @ConfigEntry.ColorPicker
        public int Badlands_COLOR                     =   0xD94515     ;
        @ConfigEntry.ColorPicker
        public int Eroded_Badlands_COLOR              =   0xFF6D3D     ;
        @ConfigEntry.ColorPicker
        public int Wooded_Badlands_COLOR              =   0xb09765     ;
        @ConfigEntry.ColorPicker
        public int Meadow_COLOR                       =   0x72789A     ;
        @ConfigEntry.ColorPicker
        public int Grove_COLOR                        =   0x338E81     ;
        @ConfigEntry.ColorPicker
        public int Snowy_Slopes_COLOR                 =   0x597D72     ;
        @ConfigEntry.ColorPicker
        public int Frozen_Peaks_COLOR                 =   0x606060     ;
        @ConfigEntry.ColorPicker
        public int Jagged_Peaks_COLOR                 =   0xA0A0A0     ;
        @ConfigEntry.ColorPicker
        public int Stony_Peaks_COLOR                  =   0x789878     ;
        @ConfigEntry.ColorPicker
        public int River_COLOR                        =   0x0000FF     ;

        @ConfigEntry.ColorPicker
        public int Frozen_River_COLOR                 =   0xA0A0FF     ;
        @ConfigEntry.ColorPicker
        public int Beach_COLOR                        =   0xFADE55     ;
        @ConfigEntry.ColorPicker
        public int Snowy_Beach_COLOR                  =   0xFAF0C0     ;
        @ConfigEntry.ColorPicker
        public int Stony_Shore_COLOR                  =   0xA2A284     ;
        @ConfigEntry.ColorPicker
        public int Warm_Ocean_COLOR                   =   0x0000AC     ;
        @ConfigEntry.ColorPicker
        public int Lukewarm_Ocean_COLOR               =   0x000090     ;
        @ConfigEntry.ColorPicker
        public int Deep_Lukewarm_Ocean_COLOR          =   0x000040     ;
        @ConfigEntry.ColorPicker
        public int Ocean_COLOR                        =   0x000070     ;
        @ConfigEntry.ColorPicker
        public int Deep_Ocean_COLOR                   =   0x000030     ;
        @ConfigEntry.ColorPicker
        public int Cold_Ocean_COLOR                   =   0x202070     ;
        @ConfigEntry.ColorPicker
        public int Deep_Cold_Ocean_COLOR              =   0x202038     ;
        @ConfigEntry.ColorPicker
        public int Frozen_Ocean_COLOR                 =   0x7070D6     ;
        @ConfigEntry.ColorPicker
        public int Deep_Frozen_Ocean_COLOR            =   0x404090     ;
        @ConfigEntry.ColorPicker
        public int Mushroom_Fields_COLOR              =   0xFF00FF     ;
        @ConfigEntry.ColorPicker
        public int Dripstone_Caves_COLOR              =   0xCA8C65     ;
        @ConfigEntry.ColorPicker
        public int Lush_Caves_COLOR                   =   0x3B470A     ;
        @ConfigEntry.ColorPicker
        public int Deep_Dark_COLOR                    =   0xA000FF     ;
        @ConfigEntry.ColorPicker
        public int Nether_Wastes_COLOR                =   0xbf3b3b     ;
        @ConfigEntry.ColorPicker
        public int Warped_Forest_COLOR                =   0x49907B     ;
        @ConfigEntry.ColorPicker
        public int Crimson_Forest_COLOR               =   0xDD0808     ;
        @ConfigEntry.ColorPicker
        public int Soul_Sand_Valley_COLOR             =   0x5e3830     ;
        @ConfigEntry.ColorPicker
        public int Basalt_Deltas_COLOR                =   0x403636     ;
        @ConfigEntry.ColorPicker
        public int The_End_COLOR                      =   0x8080FF     ;
        @ConfigEntry.ColorPicker
        public int End_HighLands_COLOR                =   0x5050FF     ;
        @ConfigEntry.ColorPicker
        public int End_Midlands_COLOR                 =   0x6060FF     ;
        @ConfigEntry.ColorPicker
        public int Small_End_Island_COLOR             =   0x7070FF     ;
        @ConfigEntry.ColorPicker
        public int End_Barrens_COLOR                  =   0x9090FF     ;

    }


    /////////////////
    /// STRUCTURES
    public static class StructuresDataConfig {
        @Comment("add custom structures to the world(using the image)")
        public boolean customStructures = false;
        @Comment("Image map to custom structures")
        public String customStructuresImage = "structures.png";


        @ConfigEntry.ColorPicker
        public int PILLAGER_OUTPOST_COLOR        = 0xB5DB88 ;
        @ConfigEntry.ColorPicker
        public int MINESHAFT_COLOR               = 0x056621 ;
        @ConfigEntry.ColorPicker
        public int MINESHAFT_MESA_COLOR          = 0xD94515 ;
        @ConfigEntry.ColorPicker
        public int WOODLAND_MANSION_COLOR        = 0x40511A ;
        @ConfigEntry.ColorPicker
        public int JUNGLE_TEMPLE_COLOR           = 0x537B09 ;
        @ConfigEntry.ColorPicker
        public int DESERT_PYRAMID_COLOR          = 0xFFBC40 ;
        @ConfigEntry.ColorPicker
        public int IGLOO_COLOR                   = 0x243F36 ;
        @ConfigEntry.ColorPicker
        public int SHIPWRECK_COLOR               = 0x000090 ;
        @ConfigEntry.ColorPicker
        public int SHIPWRECK_BEACHED_COLOR       = 0xFADE55 ;
        @ConfigEntry.ColorPicker
        public int SWAMP_HUT_COLOR               = 0x07F9B2 ;
        @ConfigEntry.ColorPicker
        public int STRONGHOLD_COLOR              = 0x606060 ;
        @ConfigEntry.ColorPicker
        public int OCEAN_MONUMENT_COLOR          = 0x000070 ;
        @ConfigEntry.ColorPicker
        public int OCEAN_RUIN_COLD_COLOR         = 0x202070 ;
        @ConfigEntry.ColorPicker
        public int OCEAN_RUIN_WARM_COLOR         = 0x0000AC ;
        @ConfigEntry.ColorPicker
        public int FORTRESS_COLOR                = 0x5e3830 ;
        @ConfigEntry.ColorPicker
        public int NETHER_FOSSIL_COLOR           = 0x49907B ;
        @ConfigEntry.ColorPicker
        public int END_CITY_COLOR                = 0x8080FF ;
        @ConfigEntry.ColorPicker
        public int BURIED_TREASURE_COLOR         = 0xFAF0C0 ;
        @ConfigEntry.ColorPicker
        public int BASTION_REMNANT_COLOR         = 0x403636 ;
        @ConfigEntry.ColorPicker
        public int VILLAGE_PLAINS_COLOR          = 0x8DB360 ;
        @ConfigEntry.ColorPicker
        public int VILLAGE_DESERT_COLOR          = 0xFA9418 ;
        @ConfigEntry.ColorPicker
        public int VILLAGE_SAVANNA_COLOR         = 0xBDB25F ;
        @ConfigEntry.ColorPicker
        public int VILLAGE_SNOWY_COLOR           = 0xFFFFFF ;
        @ConfigEntry.ColorPicker
        public int VILLAGE_TAIGA_COLOR           = 0x0B6659 ;
        @ConfigEntry.ColorPicker
        public int RUINED_PORTAL_STANDARD_COLOR  = 0xDD0808 ;
        @ConfigEntry.ColorPicker
        public int RUINED_PORTAL_DESERT_COLOR    = 0xD25F12 ;
        @ConfigEntry.ColorPicker
        public int RUINED_PORTAL_JUNGLE_COLOR    = 0x2C4205 ;
        @ConfigEntry.ColorPicker
        public int RUINED_PORTAL_SWAMP_COLOR     = 0x2FFFDA ;
        @ConfigEntry.ColorPicker
        public int RUINED_PORTAL_MOUNTAIN_COLOR  = 0x789878 ;
        @ConfigEntry.ColorPicker
        public int RUINED_PORTAL_OCEAN_COLOR     = 0x202038 ;
        @ConfigEntry.ColorPicker
        public int RUINED_PORTAL_NETHER_COLOR    = 0xbf3b3b ;
        @ConfigEntry.ColorPicker
        public int ANCIENT_CITY_COLOR            = 0xc7c7c7 ;
        @ConfigEntry.ColorPicker
        public int NONE_COLOR                    = 0x000000 ;

//    @Comment("custom mapping for the structures and there RGB values")
//    public static List<StructureAndRGBPair> customStructuresRGB = Arrays.asList(new StructureAndRGBPair("modid:structureid","0x123456"), new StructureAndRGBPair("modid:structureid","0x024680"));
    }


    /////////////////
    // Extra
    public static class ExtraDataConfig {

        @Comment("used for shifting the width of images to fit into chunks.")
        public int segmentImageWidth = 512;
        @Comment("used for shifting the height of images to fit into chunks.")
        public int segmentImageHeight = 512;
        @Comment("Internal cache size")
        public int imageCacheSize = 4;

        @Comment("Use custom generator for the overworld")
        public boolean customOverWorldGenerator = true;

        @Comment("use custom generator for the nether")
        public boolean customNetherGenerator = false;

        @Comment("use custom generator for the end")
        public boolean customEndGenerator = false;
    }
}


