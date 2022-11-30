package com.mjimmer.imagestwoworld.data;

public interface ColorConverter<T> {
    T value(int color);
    T defaultValue(int biomeX, int biomeZ);
}
