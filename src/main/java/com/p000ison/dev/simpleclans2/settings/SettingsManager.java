/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:29
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
    private double teleportFuzzyness;
    private int timeUntilTeleport;

    private boolean onlyPvPinWar;
    private boolean saveCivilians;
    private boolean globalFFForced;
    private double killWeightRival, killWeightNeutral, killWeightCivilian;

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
        teleportFuzzyness = teleportation.getDouble("teleport-fuzzyness");
        timeUntilTeleport = teleportation.getInt("teleport-waiting-time");


        ConfigurationSection pvp = config.getConfigurationSection("pvp");

        onlyPvPinWar = pvp.getBoolean("only-pvp-in-war");
        saveCivilians = pvp.getBoolean("save-civilians");
        globalFFForced = pvp.getBoolean("global-ff-forced");

        ConfigurationSection weights = pvp.getConfigurationSection("weights");

        killWeightRival = weights.getDouble("rival");
        killWeightNeutral = weights.getDouble("neutral");
        killWeightCivilian = weights.getDouble("civilian");


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
        return teleportFuzzyness;
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

    public double getKillWeightRival()
    {
        return killWeightRival;
    }

    public double getKillWeightNeutral()
    {
        return killWeightNeutral;
    }

    public double getKillWeightCivilian()
    {
        return killWeightCivilian;
    }
}
