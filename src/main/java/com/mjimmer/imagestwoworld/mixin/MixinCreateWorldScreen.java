package com.mjimmer.imagestwoworld.mixin;

import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class MixinCreateWorldScreen {
    @Inject(at = @At("HEAD"), method = "createNewWorld()V")
    private void init(CallbackInfo cInfo) {
//        File index = Paths.get("",  I2W.CONFIG_PATH , "gen").toFile();
//        try {
//            FileUtils.cleanDirectory(index);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // FIXME this should be moved over to HeightMapSource
//        if( I2W.CONFIGS.HEIGHTMAP.customHeightMap ){
//            if( I2W.hHelper.scanImage() ) {
//                I2W.LOGGER.log(Level.INFO, "HeightMap Done");
//            };
//        }
//        if( I2W.CONFIGS.BIOMES.customBiomeImage ){
//            if( I2W.bHelper.scanImage() ) {
//                I2W.bHelper.registerBiomes();
//                I2W.LOGGER.log(Level.INFO, "Biomes Done");
//            }
//        }
//        if( I2W.CONFIGS.STRUCTURES.customStructures ) {
//            if( I2W.sHelper.scanImage() ){
//                I2W.sHelper.registerStructures();
//                I2W.LOGGER.log(Level.INFO, "Structures Done");
//            }
//        }
    }
}
