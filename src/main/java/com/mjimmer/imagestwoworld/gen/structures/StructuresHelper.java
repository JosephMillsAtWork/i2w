package com.mjimmer.imagestwoworld.gen.structures;

import com.mjimmer.imagestwoworld.I2W;
import com.mjimmer.imagestwoworld.data.StructureColorConverter;
import com.mjimmer.imagestwoworld.data.StructureColors;
import com.mjimmer.imagestwoworld.data.StructureDataProvider;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.HashMap;
import java.util.HashSet;

public class StructuresHelper {

    public HashSet<StructureSet> configuredStructureFeaturesExcludeList = new HashSet<>();
    private StructureColorConverter structureColorConverter;
    private StructureDataProvider structureDataProvider;

    public StructuresHelper() {
        this.structureColorConverter = new StructureColorConverter();
        this.structureDataProvider = new StructureDataProvider(
                this.structureColorConverter,
                I2W.CONFIGS.GENERAL.mapScaleSize,
                I2W.CONFIGS.STRUCTURES.customStructuresImage
        );
    }


    public boolean scanImage(){ return
            this.getStructureDataProvider().imageManipulator().segmentImage( "structures" );
    }

    public StructureColorConverter getStructureColorConverter() {
        return structureColorConverter;
    }
    public StructureDataProvider getStructureDataProvider() {
        return structureDataProvider;
    }

    public void registerStructures(){
        for(StructureColors structureColor : StructureColors.values() ){
            structureColorConverter.registerStructure(
                    structureColor.getRGB(),
                    structureColor.getConfiguredStructureFeature()
            );
        }

        // TODO add custom(other mods biomes) stuff here
    }


    // should be a map Map<Pair<x,y> Holder<Structure>>

    // FIXME this seems to work great on startup but when loading new chunks it fails.
    public HashMap<Pair<Integer, Integer>, Holder<Structure>> getStructuresInPos(ChunkPos chunkPos){
        HashMap<Pair<Integer, Integer>, Holder<Structure>> structureArray = new HashMap<>();
        for(int x = chunkPos.getMinBlockX(); x <= chunkPos.getMaxBlockX(); x++ ) {
            for(int z = chunkPos.getMinBlockZ(); z <= chunkPos.getMaxBlockZ(); z++ ){
                if( structureDataProvider.isInImage(x, z) ){
                    Holder<Structure> configuredStructureFeature = structureDataProvider.data(x, z);
                    if( configuredStructureFeature != null ){
                        Pair<Integer, Integer> xandz = new Pair<>(x, z);
                        structureArray.put( xandz, configuredStructureFeature );
                    }
                }
            }
        }
        return structureArray;
    }

    public StructureSet getStructureByID(ResourceLocation structureID){
        return BuiltinRegistries.STRUCTURE_SETS.get( structureID );
//                configuredStructureFeatures.get( structureID );
    }
    private ResourceLocation getIdFromString(String structureID) {
        String[] str = structureID.toLowerCase().split(":");
        if (str.length!=2){
            return null;
        }else return new ResourceLocation(str[0],str[1]);
    }
}
