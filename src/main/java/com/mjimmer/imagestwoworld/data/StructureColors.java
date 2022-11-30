package com.mjimmer.imagestwoworld.data;

import com.mjimmer.imagestwoworld.config.I2WConfigHelper;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.Structures;
import  net.minecraft.world.level.levelgen.structure.Structure;


public enum StructureColors {
    PILLAGER_OUTPOST       ( I2WConfigHelper.PILLAGER_OUTPOST_COLOR         (), Structures.PILLAGER_OUTPOST ),
    MINESHAFT              ( I2WConfigHelper.MINESHAFT_COLOR                (), Structures.MINESHAFT ),
    MINESHAFT_MESA         ( I2WConfigHelper.MINESHAFT_MESA_COLOR           (), Structures.MINESHAFT_MESA),
    WOODLAND_MANSION       ( I2WConfigHelper.WOODLAND_MANSION_COLOR         (), Structures.WOODLAND_MANSION ),
    JUNGLE_TEMPLE          ( I2WConfigHelper.JUNGLE_TEMPLE_COLOR            (), Structures.JUNGLE_TEMPLE),
    DESERT_PYRAMID         ( I2WConfigHelper.DESERT_PYRAMID_COLOR           (), Structures.DESERT_PYRAMID),
    IGLOO                  ( I2WConfigHelper.IGLOO_COLOR                    (), Structures.IGLOO),
    SHIPWRECK              ( I2WConfigHelper.SHIPWRECK_COLOR                (), Structures.SHIPWRECK),
    SHIPWRECK_BEACHED      ( I2WConfigHelper.SHIPWRECK_BEACHED_COLOR        (), Structures.SHIPWRECK_BEACHED),
    SWAMP_HUT              ( I2WConfigHelper.SWAMP_HUT_COLOR                (), Structures.SWAMP_HUT),
    STRONGHOLD             ( I2WConfigHelper.STRONGHOLD_COLOR               (), Structures.STRONGHOLD),
    OCEAN_MONUMENT         ( I2WConfigHelper.OCEAN_MONUMENT_COLOR           (), Structures.OCEAN_MONUMENT),
    OCEAN_RUIN_COLD        ( I2WConfigHelper.OCEAN_RUIN_COLD_COLOR          (), Structures.OCEAN_RUIN_COLD),
    OCEAN_RUIN_WARM        ( I2WConfigHelper.OCEAN_RUIN_WARM_COLOR          (), Structures.OCEAN_RUIN_WARM),
    FORTRESS               ( I2WConfigHelper.FORTRESS_COLOR                 (), Structures.FORTRESS),
    NETHER_FOSSIL          ( I2WConfigHelper.NETHER_FOSSIL_COLOR            (), Structures.NETHER_FOSSIL),
    END_CITY               ( I2WConfigHelper.END_CITY_COLOR                 (), Structures.END_CITY),
    BURIED_TREASURE        ( I2WConfigHelper.BURIED_TREASURE_COLOR          (), Structures.BURIED_TREASURE),
    BASTION_REMNANT        ( I2WConfigHelper.BASTION_REMNANT_COLOR          (), Structures.BASTION_REMNANT),
    VILLAGE_PLAINS         ( I2WConfigHelper.VILLAGE_PLAINS_COLOR           (), Structures.VILLAGE_PLAINS),
    VILLAGE_DESERT         ( I2WConfigHelper.VILLAGE_DESERT_COLOR           (), Structures.VILLAGE_DESERT),
    VILLAGE_SAVANNA        ( I2WConfigHelper.VILLAGE_SAVANNA_COLOR          (), Structures.VILLAGE_SAVANNA),
    VILLAGE_SNOWY          ( I2WConfigHelper.VILLAGE_SNOWY_COLOR            (), Structures.VILLAGE_SNOWY),
    VILLAGE_TAIGA          ( I2WConfigHelper.VILLAGE_TAIGA_COLOR            (), Structures.VILLAGE_TAIGA),
    RUINED_PORTAL_STANDARD ( I2WConfigHelper.RUINED_PORTAL_STANDARD_COLOR   (), Structures.RUINED_PORTAL_STANDARD),
    RUINED_PORTAL_DESERT   ( I2WConfigHelper.RUINED_PORTAL_DESERT_COLOR     (), Structures.RUINED_PORTAL_DESERT),
    RUINED_PORTAL_JUNGLE   ( I2WConfigHelper.RUINED_PORTAL_JUNGLE_COLOR     (), Structures.RUINED_PORTAL_JUNGLE),
    RUINED_PORTAL_SWAMP    ( I2WConfigHelper.RUINED_PORTAL_SWAMP_COLOR      (), Structures.RUINED_PORTAL_SWAMP),
    RUINED_PORTAL_MOUNTAIN ( I2WConfigHelper.RUINED_PORTAL_MOUNTAIN_COLOR   (), Structures.RUINED_PORTAL_MOUNTAIN),
    RUINED_PORTAL_OCEAN    ( I2WConfigHelper.RUINED_PORTAL_OCEAN_COLOR      (), Structures.RUINED_PORTAL_OCEAN),
    RUINED_PORTAL_NETHER   ( I2WConfigHelper.RUINED_PORTAL_NETHER_COLOR     (), Structures.RUINED_PORTAL_NETHER),
    ANCIENT_CITY           ( I2WConfigHelper.ANCIENT_CITY_COLOR             (), Structures.ANCIENT_CITY ),
    NONE                   ( I2WConfigHelper.NONE_COLOR                     (), null);
    final int rgb;
    final Holder<Structure> configuredStructureFeature;

    StructureColors(int rgb,  Holder<Structure>  configuredStructureFeature)
    {
        this.rgb = rgb;
        this.configuredStructureFeature =  configuredStructureFeature;
    }
    public  Holder<Structure> getConfiguredStructureFeature() {
        return configuredStructureFeature;
    }
    public int getRGB() {
        return rgb;
    }

}
