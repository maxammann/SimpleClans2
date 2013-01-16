package com.p000ison.dev.simpleclans2.api;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a Configuration
 */
public class Configuration extends YamlConfiguration {
    private File file;

    public Configuration(File file) throws IOException, InvalidConfigurationException {
        this.file = file;

        load();
    }

    private void load() throws IOException, InvalidConfigurationException {
        if (!file.getParentFile().exists() && !file.getParentFile().mkdir()) {
            throw new IOException("Failed at creating SimpleClans folder!");
        }

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Failed at creating SimpleClans config!");
            }
        }

        load(file);
    }

    public void setDefault(File file) throws IOException, InvalidConfigurationException {
        setDefaults(new Configuration(file));
        this.options().copyDefaults(true);
    }

    public void setDefault(String jarPath, Plugin plugin) throws IOException, InvalidConfigurationException {
        InputStream defConfigStream = plugin.getResource(jarPath);

        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            setDefaults(defConfig);
        }
        this.options().copyDefaults(true);
    }

    public void reload() throws IOException, InvalidConfigurationException {
        load();
    }

    public void save() throws IOException {
        this.save(file);
    }
}
