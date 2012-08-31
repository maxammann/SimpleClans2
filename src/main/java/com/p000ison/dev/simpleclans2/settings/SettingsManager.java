/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2.settings;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.logging.Level;

/**
 * Represents a SettingsManager
 */
public class SettingsManager {
    private SimpleClans plugin;
    private FileConfiguration config;

    private boolean dropAll, drop;
    private Set<Integer> keepOnTeleport = new HashSet<Integer>();
    private double teleportfuzzyness;
    private int timeUntilTeleport;

    public SettingsManager(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

    private void init()
    {
        config = plugin.getConfig();
    }

    private void load()
    {

        ConfigurationSection settings = config.getConfigurationSection("settings");

        drop = settings.getBoolean("drop-items-on-teleport");
        dropAll = settings.getBoolean("drop-all-items-on-teleport");
        teleportfuzzyness = settings.getDouble("teleport-fuzzyness");
        timeUntilTeleport = settings.getInt("teleport-waiting-time");


        //prepare variables
        List<String> keepOnTeleportRaw = settings.getStringList("keep-items-on-teleport");

        //do "magic" stuff to load this

        //checks and parses the materials
        for (String materialRaw : keepOnTeleportRaw) {

            Material material;

            if (materialRaw.matches("[0-9]+")) {
                material = Material.getMaterial(Integer.parseInt(materialRaw));
            } else {
                material = Material.valueOf(materialRaw.toUpperCase());
            }

            if (material != null) {
                keepOnTeleport.add(material.getId());
            } else {
                Logging.debug("The item or id: %s was not found!", Level.WARNING, material);
            }
        }
    }

    public void save()
    {
        plugin.saveConfig();
    }

    public void reload()
    {
        plugin.reloadConfig();
    }

    public boolean dropItemOnTeleport(Material mat)
    {
        return (dropAll || !keepOnTeleport.contains(mat.getId()));
    }

    public boolean dropItems()
    {
        return drop;
    }

    public double getTeleportFuzzyness()
    {
        return teleportfuzzyness;
    }

    public int getTimeUntilTeleport()
    {
        return timeUntilTeleport;
    }
}
