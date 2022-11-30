package com.mjimmer.imagestwoworld;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;


public class I2WSavedData extends SavedData {
    private static final String NAME = "i2w";
    private static final String GENERATED_KEY = "generated";

    public boolean generated;

    public I2WSavedData() {
        generated = false;
    }

    public static I2WSavedData get(ServerLevel world) {
        return world.getServer().overworld().getDataStorage().computeIfAbsent(
                I2WSavedData::readNbt,
                I2WSavedData::new,
                NAME
        );
    }

    private static I2WSavedData readNbt(CompoundTag nbt) {
        I2WSavedData savedData = new I2WSavedData();
        savedData.generated = nbt.getBoolean(GENERATED_KEY);
        return savedData;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putBoolean(GENERATED_KEY, true);
        return nbt;
    }

    public void setGenerated() {
        generated = true;
        this.setDirty();
    }
}
