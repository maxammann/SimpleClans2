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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    private boolean onlyPvPinWar;
    private boolean saveCivilians;
    private boolean globalFFForced;

    public SettingsManager(SimpleClans plugin)
    {
        this.plugin = plugin;
        init();
    }

    private void init()
    {
        config = plugin.getConfig();

        config.options().copyDefaults(true);
        save();

        load();
    }

    private void load()
    {

        ConfigurationSection teleportation = config.getConfigurationSection("teleportation");

        drop = teleportation.getBoolean("drop-items-on-teleport");
        dropAll = teleportation.getBoolean("drop-all-items-on-teleport");
        teleportfuzzyness = teleportation.getDouble("teleport-fuzzyness");
        timeUntilTeleport = teleportation.getInt("teleport-waiting-time");

        ConfigurationSection pvp = config.getConfigurationSection("pvp");

        onlyPvPinWar = pvp.getBoolean("only-pvp-in-war");
        saveCivilians = pvp.getBoolean("save-civilians");
        globalFFForced = pvp.getBoolean("global-ff-forced");


        //prepare variables
        List<String> keepOnTeleportRaw = teleportation.getStringList("keep-items-on-teleport");

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

    public boolean isSaveCivilians()
    {
        return saveCivilians;
    }

    public boolean isOnlyPvPinWar()
    {
        return onlyPvPinWar;
    }

    public boolean isGlobalFFForced()
    {
        return globalFFForced;
    }
}
