package com.mjimmer.imagestwoworld;

import com.mjimmer.imagestwoworld.config.I2WConfigHelper;
import com.mjimmer.imagestwoworld.gen.biome.BiomeSourceHelper;
import com.mjimmer.imagestwoworld.gen.heightmap.HeightMapHelper;
import com.mjimmer.imagestwoworld.gen.structures.StructuresHelper;
import com.mjimmer.imagestwoworld.world.level.levelgen.presets.I2WWorldPreset;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class I2W implements ModInitializer {
	public static String MOD_NAME = "i2w";
	public static String PRESET_ID = "i2w_preset";
	public static String BIOMESOURCE_ID = "i2w_biomes";
	public static String CHUNKGENERATOR_ID = "i2w_biomes";
	public static String CONFIG_PATH = "config/" + MOD_NAME;
	public static final Logger LOGGER = LogManager.getLogger();
	public static I2WConfigHelper CONFIGS = new I2WConfigHelper();
	public static  HeightMapHelper hHelper;
	public static  BiomeSourceHelper bHelper;
	public static  StructuresHelper sHelper;

	public void onInitialize() {
		// register the custom preset
//		if (isPhysicalClient()) {
//			PacketHander.registerClientListener();
//		}
		CONFIGS.initialize();
		bHelper = new BiomeSourceHelper();
		hHelper = new HeightMapHelper();
		if( CONFIGS.STRUCTURES.customStructures ) {
			sHelper = new StructuresHelper();
		}
		// register the preset.
		I2WWorldPreset.register();
		I2WEvents.init();
	}

	public static boolean isPhysicalClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}
	public static boolean isLogicalClient(Level level) {return level.isClientSide;}

	public static boolean isPhysicalServer() {
		return !isPhysicalClient();
	}
	public static boolean isLogicalServer(Level level) {
		return !isLogicalClient(level);
	}

	public static HeightMapHelper getHeightMapHelper(){return hHelper;}
	public static BiomeSourceHelper biomeSourceHelper(){return bHelper;}
	public static StructuresHelper structuresHelper(){return sHelper;}
}

// CLIENT SCREEN
// selectWorld.moreWorldOptions