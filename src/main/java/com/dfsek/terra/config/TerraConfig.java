package com.dfsek.terra.config;

import com.dfsek.terra.config.base.ConfigPack;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public abstract class TerraConfig {
    private final ConfigPack config;
    private final YamlConfiguration yaml;

    public TerraConfig(File file, ConfigPack config) throws IOException, InvalidConfigurationException {
        yaml = new YamlConfiguration();
        yaml.load(file);
        this.config = config;
    }

    public TerraConfig(InputStream stream, ConfigPack config) throws IOException, InvalidConfigurationException {
        yaml = new YamlConfiguration();
        yaml.load(new InputStreamReader(stream));
        this.config = config;
    }

    public ConfigPack getConfig() {
        return config;
    }

    public abstract String getID();


    public @NotNull Set<String> getKeys(boolean deep) {
        return yaml.getKeys(deep);
    }


    public @NotNull Map<String, Object> getValues(boolean deep) {
        return yaml.getValues(deep);
    }


    public boolean contains(@NotNull String path) {
        return yaml.contains(path);
    }


    public boolean contains(@NotNull String path, boolean ignoreDefault) {
        return yaml.contains(path, ignoreDefault);
    }


    public boolean isSet(@NotNull String path) {
        return yaml.isSet(path);
    }


    public @Nullable Object get(@NotNull String path) {
        return yaml.get(path);
    }


    public @Nullable Object get(@NotNull String path, @Nullable Object def) {
        return yaml.get(path, def);
    }


    public @Nullable String getString(@NotNull String path) {
        return yaml.getString(path);
    }


    public @Nullable String getString(@NotNull String path, @Nullable String def) {
        return yaml.getString(path, def);
    }


    public boolean isString(@NotNull String path) {
        return yaml.isString(path);
    }


    public int getInt(@NotNull String path) {
        return yaml.getInt(path);
    }


    public int getInt(@NotNull String path, int def) {
        return yaml.getInt(path, def);
    }


    public boolean isInt(@NotNull String path) {
        return yaml.isInt(path);
    }


    public boolean getBoolean(@NotNull String path) {
        return yaml.getBoolean(path);
    }


    public boolean getBoolean(@NotNull String path, boolean def) {
        return yaml.getBoolean(path, def);
    }


    public boolean isBoolean(@NotNull String path) {
        return yaml.isBoolean(path);
    }


    public double getDouble(@NotNull String path) {
        return yaml.getDouble(path);
    }


    public double getDouble(@NotNull String path, double def) {
        return yaml.getDouble(path, def);
    }


    public boolean isDouble(@NotNull String path) {
        return yaml.isDouble(path);
    }


    public long getLong(@NotNull String path) {
        return yaml.getLong(path);
    }


    public long getLong(@NotNull String path, long def) {
        return yaml.getLong(path, def);
    }


    public boolean isLong(@NotNull String path) {
        return yaml.isLong(path);
    }


    public @Nullable List<?> getList(@NotNull String path) {
        return yaml.getList(path);
    }


    public @Nullable List<?> getList(@NotNull String path, @Nullable List<?> def) {
        return yaml.getList(path, def);
    }


    public boolean isList(@NotNull String path) {
        return yaml.isList(path);
    }


    public @NotNull List<String> getStringList(@NotNull String path) {
        return yaml.getStringList(path);
    }


    public @NotNull List<Integer> getIntegerList(@NotNull String path) {
        return yaml.getIntegerList(path);
    }


    public @NotNull List<Boolean> getBooleanList(@NotNull String path) {
        return yaml.getBooleanList(path);
    }


    public @NotNull List<Double> getDoubleList(@NotNull String path) {
        return yaml.getDoubleList(path);
    }


    public @NotNull List<Float> getFloatList(@NotNull String path) {
        return yaml.getFloatList(path);
    }


    public @NotNull List<Long> getLongList(@NotNull String path) {
        return yaml.getLongList(path);
    }


    public @NotNull List<Byte> getByteList(@NotNull String path) {
        return yaml.getByteList(path);
    }


    public @NotNull List<Character> getCharacterList(@NotNull String path) {
        return yaml.getCharacterList(path);
    }


    public @NotNull List<Short> getShortList(@NotNull String path) {
        return yaml.getShortList(path);
    }


    public @NotNull List<Map<?, ?>> getMapList(@NotNull String path) {
        return yaml.getMapList(path);
    }


    public <T> @Nullable T getObject(@NotNull String path, @NotNull Class<T> clazz) {
        return yaml.getObject(path, clazz);
    }


    public <T> @Nullable T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        return yaml.getObject(path, clazz, def);
    }


    public <T extends ConfigurationSerializable> @Nullable T getSerializable(@NotNull String path, @NotNull Class<T> clazz) {
        return yaml.getSerializable(path, clazz);
    }


    public <T extends ConfigurationSerializable> @Nullable T getSerializable(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        return yaml.getSerializable(path, clazz, def);
    }


    public @Nullable ItemStack getItemStack(@NotNull String path) {
        return yaml.getItemStack(path);
    }


    public @Nullable ItemStack getItemStack(@NotNull String path, @Nullable ItemStack def) {
        return yaml.getItemStack(path, def);
    }


    public boolean isItemStack(@NotNull String path) {
        return yaml.isItemStack(path);
    }


    public @Nullable Color getColor(@NotNull String path) {
        return yaml.getColor(path);
    }


    public @Nullable Color getColor(@NotNull String path, @Nullable Color def) {
        return yaml.getColor(path, def);
    }


    public boolean isColor(@NotNull String path) {
        return yaml.isColor(path);
    }


    public @Nullable Location getLocation(@NotNull String path) {
        return yaml.getLocation(path);
    }


    public @Nullable Location getLocation(@NotNull String path, @Nullable Location def) {
        return yaml.getLocation(path, def);
    }


    public boolean isLocation(@NotNull String path) {
        return yaml.isLocation(path);
    }
}
