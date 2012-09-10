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
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2.settings;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.util.ExceptionHelper;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.ChatColor;
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

    private int elementsPerPage;
    private String clanCommand;
    private String serverName;

    private int maxTagLenght, minTagLenght;
    private Character[] disallowedColors;
    private Set<String> disallowedTags;
    private boolean requireVerification;
    private boolean purchaseCreation, purchaseVerification;
    private double purchasePrice;
    private int purgeInactivePlayersDays, purgeInactiveClansDays, purgeUnverifiedClansDays;
    private boolean showUnverifiedClansOnList;
    private int minimalSizeToAlly;

    private String clanBB, clanPlayerBB, defaultBB;
    private int maxBBDisplayLines, maxBBLenght;

    private boolean voteForDemote;

    private ChatColor headingPageColor, subPageColor, clanColor, leaderColor, trustedColor, untrustedColor;

    public SettingsManager(SimpleClans plugin)
    {
        this.plugin = plugin;
        init();
    }

    private void init()
    {
        config = plugin.getConfig();


        save();

        load();
    }

    private void load()
    {
        Logging.debug("Loading Settings!");
        long start = System.currentTimeMillis();
        try {
            ConfigurationSection general = config.getConfigurationSection("general");

            elementsPerPage = general.getInt("elements-per-page");
            clanCommand = general.getString("clan-command");
            setServerName(general.getString("server-name"));

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


            ConfigurationSection clan = config.getConfigurationSection("clan");

            maxTagLenght = clan.getInt("max-tag-lenght");
            minTagLenght = clan.getInt("min-tag-lenght");
            List<Character> disallowedColorsList = clan.getCharacterList("disallowed-colors");
            disallowedColors = disallowedColorsList.toArray(new Character[disallowedColorsList.size()]);
            disallowedTags = new HashSet<String>(config.getStringList("disallowed-tags"));
            maxBBLenght = clan.getInt("max-bb-lenght");
            requireVerification = clan.getBoolean("require-verification");
            showUnverifiedClansOnList = clan.getBoolean("show-unverified-clans-on-list");
            minimalSizeToAlly = clan.getInt("minimal-size-to-ally");

            ConfigurationSection clanEconomy = clan.getConfigurationSection("economy");

            purchaseCreation = clanEconomy.getBoolean("purchase-creation");
            purchasePrice = clanEconomy.getDouble("purchase-price");

            ConfigurationSection clanPurge = clan.getConfigurationSection("purge");

            purgeInactivePlayersDays = clanPurge.getInt("inactive-player-data-days");
            purgeInactiveClansDays = clanPurge.getInt("inactive-clan-days");
            purgeUnverifiedClansDays = clanPurge.getInt("unverified-clan-days");


            ConfigurationSection clanBBSection = clan.getConfigurationSection("bb");

            clanBB = clanBBSection.getString("clan");
            clanPlayerBB = clanBBSection.getString("clanplayer");
            defaultBB = clanBBSection.getString("default");
            maxBBDisplayLines = clanBBSection.getInt("max-display-lines");
            maxBBLenght = clanBBSection.getInt("max-lines");

            ConfigurationSection voting = clan.getConfigurationSection("voting");
            voteForDemote = voting.getBoolean("demote");

            ConfigurationSection paging = config.getConfigurationSection("paging");

            ConfigurationSection pageColors = paging.getConfigurationSection("colors");

            headingPageColor = ChatColor.getByChar(pageColors.getString("heading-color"));
            subPageColor = ChatColor.getByChar(pageColors.getString("sub-color"));
            clanColor = ChatColor.getByChar(pageColors.getString("clan-color"));
            leaderColor = ChatColor.getByChar(pageColors.getString("leader-color"));
            trustedColor = ChatColor.getByChar(pageColors.getString("trusted-color"));
            untrustedColor = ChatColor.getByChar(pageColors.getString("untrusted-color"));


            //prepare variables
            List<String> keepOnTeleportRaw = teleportation.getStringList("keep-items-on-teleport");

            //do "magic" stuff to load this

            if (getServerName() == null) {
                setServerName(plugin.getServer().getServerName());
            }

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
        } catch (Exception e) {
            ExceptionHelper.handleException(e, getClass());
        }

        long finish = System.currentTimeMillis();

        Logging.debug("Loading settings finished! Took %s ms!", finish - start);
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

    public int getElementsPerPage()
    {
        return elementsPerPage;
    }

    public int getMaxTagLenght()
    {
        return maxTagLenght;
    }

    public int getMinTagLenght()
    {
        return minTagLenght;
    }

    public Character[] getDisallowedColors()
    {
        return disallowedColors;
    }

    public boolean isTagDisallowed(String tag)
    {
        return disallowedTags.contains(tag);
    }

    public int getMaxBBLenght()
    {
        return maxBBLenght;
    }

    public boolean requireVerification()
    {
        return requireVerification;
    }

    public boolean isPurchaseVerification()
    {
        return purchaseVerification;
    }

    public boolean purchaseCreation()
    {
        return purchaseCreation;
    }

    public String getClanCommand()
    {
        return clanCommand;
    }

    public double getPurchasePrice()
    {
        return purchasePrice;
    }

    public int getPurgeInactivePlayersDays()
    {
        return purgeInactivePlayersDays;
    }

    public int getPurgeInactiveClansDays()
    {
        return purgeInactiveClansDays;
    }

    public int getPurgeUnverifiedClansDays()
    {
        return purgeUnverifiedClansDays;
    }

    public ChatColor getHeadingPageColor()
    {
        return headingPageColor;
    }

    public ChatColor getSubPageColor()
    {
        return subPageColor;
    }

    public String getServerName()
    {
        return serverName;
    }

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public boolean isShowUnverifiedClansOnList()
    {
        return showUnverifiedClansOnList;
    }

    public int getMinimalSizeToAlly()
    {
        return minimalSizeToAlly;
    }

    public String getClanBB()
    {
        return clanBB;
    }

    public String getClanPlayerBB()
    {
        return clanPlayerBB;
    }

    public String getDefaultBB()
    {
        return defaultBB;
    }

    public int getMaxBBDisplayLines()
    {
        return maxBBDisplayLines;
    }

    public ChatColor getClanColor()
    {
        return clanColor;
    }

    public ChatColor getLeaderColor()
    {
        return leaderColor;
    }

    public ChatColor getTrustedColor()
    {
        return trustedColor;
    }

    public ChatColor getUntrustedColor()
    {
        return untrustedColor;
    }

    public boolean isVoteForDemote()
    {
        return voteForDemote;
    }
}
