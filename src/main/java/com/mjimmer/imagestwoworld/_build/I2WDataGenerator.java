package com.mjimmer.imagestwoworld._build;

import com.mjimmer.imagestwoworld.world.level.levelgen.presets.I2WWorldPreset;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.WorldPresetTags;

public class I2WDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(
                new FabricTagProvider.DynamicRegistryTagProvider<>(
                        fabricDataGenerator,
                        BuiltinRegistries.WORLD_PRESET.key()
                ) {
            @Override
            protected void generateTags() {
                getOrCreateTagBuilder(WorldPresetTags.NORMAL).add(I2WWorldPreset.I2W_KEY);
            }
        });
    }
}


