package com.mjimmer.imagestwoworld;

import com.mjimmer.imagestwoworld.world.level.levelgen.I2WChunkGenerator;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class I2WEvents {
    public static boolean isServerI2W = false;
    public static boolean isClientI2W = false;

    public static boolean hasHeightMap = false;
    public static boolean hasBiomeMap = false;
    public static boolean hasStructureMap = false;

    public static void init() {
        onServerStarting();
        onServerStopped();
        onServerWorldLoad();
        onServerPlayerJoin();
        }

    private static void onServerStarting(){
        ServerLifecycleEvents.SERVER_STARTING.register( server -> {
            I2W.LOGGER.log(Level.INFO, "Server is starting is this to late for the chunk generator ?" );
            File index = Paths.get("",  I2W.CONFIG_PATH , "gen").toFile();
            try {
                FileUtils.cleanDirectory(index);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if( I2W.CONFIGS.HEIGHTMAP.customHeightMap ){
            //    if(!hasHeightMap){
                    if( I2W.hHelper.scanImage() ) {
                      //  hasHeightMap = true;
                    };
              //  }
                I2W.LOGGER.log(Level.INFO, "HeightMap Done");
            }
            if( I2W.CONFIGS.BIOMES.customBiomeImage ){
                //if(!hasBiomeMap) {
                    if (I2W.bHelper.scanImage()) {
                    //    hasBiomeMap = true;
                        I2W.bHelper.registerBiomes();
                  //  }
                }
                I2W.LOGGER.log(Level.INFO, "Biomes Done");
            }
            if( I2W.CONFIGS.STRUCTURES.customStructures ) {
                // if(!hasStructureMap) {
                    if( I2W.sHelper.scanImage() ){
                  //      hasStructureMap = true;
                        I2W.sHelper.registerStructures();
                    }
                //}
                I2W.LOGGER.log(Level.INFO, "Structures Done");
            }
        });
    }

    private static void onServerStopped(){
        ServerLifecycleEvents.SERVER_STOPPED.register( server -> {
            I2W.LOGGER.log(Level.INFO, "Server is Done !" );
        });
    }

    private static void onServerWorldLoad() {
        ServerWorldEvents.LOAD.register((server, level) -> {
            ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();
            isServerI2W = chunkGenerator instanceof I2WChunkGenerator;
            // I think that if I check everything here it is going to be too late. Meaning that the genertor has already
            // started and might not have the data that is needed from the images.
        });
    }

    private static void onServerPlayerJoin() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (server.overworld().getChunkSource().getGenerator() instanceof I2WChunkGenerator) {
//                PacketHander.sendTo(handler.getPlayer(), PacketHander.WORLDTYPE_PACKET.getIdentifier(), PacketByteBufs.empty());
                spawnPlayer(handler.getPlayer());
            }
        });
    }

    private static void spawnPlayer(ServerPlayer player) {
        I2WSavedData savedData = I2WSavedData.get((ServerLevel)player.level );
        if (!savedData.generated && !I2W.CONFIGS.GENERAL.spwanCords.equals("0,64,0")) {
            String spawnPos =  I2W.CONFIGS.GENERAL.spwanCords;
            String[] configPos = spawnPos.split(",");
            double[] pos = new double[3];
            try {
                pos[0] = Double.parseDouble(configPos[0]);
                pos[1] = Double.parseDouble(configPos[1]);
                pos[2] = Double.parseDouble(configPos[2]);
            } catch (Exception ex) {
                pos = new double[] { 0, 64, 0 };
            }

            player.teleportTo(pos[0]+0.5, pos[1]+1.6, pos[2]+0.5);
            player.setRespawnPosition(
                    player.level.dimension(),
                    new BlockPos(pos[0]+0.5,pos[1]+1.6, pos[2]+0.5 ),
                    0,
                    true,
                    false
            );
        }
    }
}
