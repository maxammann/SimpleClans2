/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Last modified: 07.01.13 14:39
 */

package com.p000ison.dev.simpleclans2.chat;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventPriority;

import java.util.HashSet;
import java.util.Set;

public class SettingsManager {
    private SimpleClansChat plugin;
    private FileConfiguration config;
    private boolean compatibilityMode, cancellingMode;
    private boolean completeMode;
    private String completeModeFormat;
    private String clanChannelFormat;
    private String allyChannelFormat;
    private boolean depreciationMode;
    private Set<Variable> variables = new HashSet<Variable>();
    private EventPriority priority;

    public SettingsManager(SimpleClansChat plugin) {
        this.plugin = plugin;
        init();
    }

    private void init() {
        this.config = this.plugin.getConfig();

        this.config.options().copyDefaults(true);
        save();
        load();
    }

    public void reload() {
        this.plugin.reloadConfig();
        this.config = this.plugin.getConfig();
        load();
    }

    public void load() {
        ConfigurationSection settings = this.config.getConfigurationSection("settings");

        this.compatibilityMode = settings.getBoolean("compatibility-mode");
        this.completeMode = settings.getBoolean("complete-mode");
        this.cancellingMode = settings.getBoolean("cancelling-mode");
        this.depreciationMode = settings.getBoolean("depreciation-mode");
        priority = EventPriority.valueOf(settings.getString("priority").toUpperCase());

        ConfigurationSection format = this.config.getConfigurationSection("format");

        this.completeModeFormat = format.getString("complete-mode");

        ConfigurationSection channels = this.config.getConfigurationSection("channels");

        this.clanChannelFormat = channels.getString("clan");
        this.allyChannelFormat = channels.getString("ally");

        ConfigurationSection variablesSection = format.getConfigurationSection("variables");

        for (String variable : variablesSection.getKeys(false)) {
            variables.add(new Variable('-' + variable, variablesSection.getString(variable)));
        }
    }

    public Variable getVariable(String query) {
        for (Variable variable : variables) {
            if (variable.getVariable().equals(query)) {
                return variable;
            }
        }
        return null;
    }

    public void save() {
        this.plugin.saveConfig();
    }

    public boolean isCompatibilityMode() {
        return this.compatibilityMode;
    }

    public boolean isCompleteMode() {
        return this.completeMode;
    }

    public String getCompleteModeFormat() {
        return this.completeModeFormat;
    }

    public String getClanChannelFormat() {
        return clanChannelFormat;
    }

    public String getAllyChannelFormat() {
        return allyChannelFormat;
    }

    public boolean isDepreciationMode() {
        return depreciationMode;
    }

    public boolean isCancellingMode() {
        return cancellingMode;
    }

    public EventPriority getPriority() {
        return priority;
    }
}