package com.mjimmer.imagestwoworld.world.level.levelgen.presets;

import com.mjimmer.imagestwoworld.I2W;
import com.mjimmer.imagestwoworld.config.I2WConfigHelper;
import com.mjimmer.imagestwoworld.world.level.biome.I2WBiomeSource;
import com.mjimmer.imagestwoworld.world.level.levelgen.I2WChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;


import java.util.Map;


/// FABRIC CLASS
// TODO expose settings to PresetEditor

public class I2WWorldPreset {

    public static final ResourceKey<WorldPreset> I2W_KEY = ResourceKey.create(
            Registry.WORLD_PRESET_REGISTRY, new ResourceLocation( I2W.MOD_NAME, I2W.PRESET_ID )
    );

    public static void register() {
        // register the preset
        BuiltinRegistries.register(
                BuiltinRegistries.WORLD_PRESET,
                I2W_KEY,
                build()
        );
        Registry.register(
                Registry.BIOME_SOURCE,
                new ResourceLocation( I2W.MOD_NAME, I2W.BIOMESOURCE_ID),
                I2WBiomeSource.CODEC
        );
        // register the chunk generator
        Registry.register(
                Registry.CHUNK_GENERATOR,
                new ResourceLocation( I2W.MOD_NAME, I2W.CHUNKGENERATOR_ID ),
                I2WChunkGenerator.CODEC
        );
    }

    private static WorldPreset build() {
        return new WorldPreset(
                Map.of(
                        LevelStem.OVERWORLD, new LevelStem(
                                BuiltinRegistries.DIMENSION_TYPE.getOrCreateHolderOrThrow(BuiltinDimensionTypes.OVERWORLD ),
                                overworldChunkGenerator()
                        ),
                        LevelStem.NETHER, new LevelStem(
                                BuiltinRegistries.DIMENSION_TYPE.getOrCreateHolderOrThrow(BuiltinDimensionTypes.NETHER),
                                netherChunkGenerator()
                        ),
                        LevelStem.END, new LevelStem(
                                BuiltinRegistries.DIMENSION_TYPE.getOrCreateHolderOrThrow(BuiltinDimensionTypes.END),
                                endChunkGenerator()
                        )
                )
        );
    }

    private static ChunkGenerator overworldChunkGenerator() {
        return I2WConfigHelper.customOverWorldGenerator() ? new I2WChunkGenerator(
                BuiltinRegistries.STRUCTURE_SETS,
                BuiltinRegistries.NOISE,
                new I2WBiomeSource( BuiltinRegistries.BIOME ),
                BuiltinRegistries.NOISE_GENERATOR_SETTINGS.getOrCreateHolderOrThrow(NoiseGeneratorSettings.OVERWORLD )
        ) : new NoiseBasedChunkGenerator(
                BuiltinRegistries.STRUCTURE_SETS,
                BuiltinRegistries.NOISE,
                MultiNoiseBiomeSource.Preset.OVERWORLD.biomeSource(BuiltinRegistries.BIOME),
                BuiltinRegistries.NOISE_GENERATOR_SETTINGS.getOrCreateHolderOrThrow(NoiseGeneratorSettings.OVERWORLD )
        );
    }

    private static ChunkGenerator netherChunkGenerator() {
        return I2WConfigHelper.customNetherGenerator() ? new I2WChunkGenerator(
                BuiltinRegistries.STRUCTURE_SETS,
                BuiltinRegistries.NOISE,
                MultiNoiseBiomeSource.Preset.NETHER.biomeSource(BuiltinRegistries.BIOME),
                BuiltinRegistries.NOISE_GENERATOR_SETTINGS.getOrCreateHolderOrThrow(NoiseGeneratorSettings.NETHER )
        ) : new NoiseBasedChunkGenerator(
                BuiltinRegistries.STRUCTURE_SETS,
                BuiltinRegistries.NOISE,
                MultiNoiseBiomeSource.Preset.NETHER.biomeSource(BuiltinRegistries.BIOME),
                BuiltinRegistries.NOISE_GENERATOR_SETTINGS.getOrCreateHolderOrThrow( NoiseGeneratorSettings.NETHER)
        );
    }
    private static ChunkGenerator endChunkGenerator() {
        return  I2WConfigHelper.customEndGenerator() ? new I2WChunkGenerator(
                BuiltinRegistries.STRUCTURE_SETS,
                BuiltinRegistries.NOISE,
                new TheEndBiomeSource(BuiltinRegistries.BIOME), // FIXME this is something else lol.
                BuiltinRegistries.NOISE_GENERATOR_SETTINGS.getOrCreateHolderOrThrow( NoiseGeneratorSettings.END )
        ) : new NoiseBasedChunkGenerator(
                BuiltinRegistries.STRUCTURE_SETS,
                BuiltinRegistries.NOISE,
                new TheEndBiomeSource(BuiltinRegistries.BIOME),
                BuiltinRegistries.NOISE_GENERATOR_SETTINGS.getOrCreateHolderOrThrow(NoiseGeneratorSettings.END)
        );
    }
}
