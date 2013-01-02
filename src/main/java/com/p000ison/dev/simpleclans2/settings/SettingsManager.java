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
 *     Last modified: 11.10.12 15:46
 */


package com.p000ison.dev.simpleclans2.settings;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.updater.UpdateType;
import com.p000ison.dev.simpleclans2.util.ExceptionHelper;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.simpleclans2.util.Logging;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import com.p000ison.dev.sqlapi.DatabaseConfiguration;
import com.p000ison.dev.sqlapi.mysql.MySQLConfiguration;
import com.p000ison.dev.sqlapi.sqlite.SQLiteConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;
import java.util.logging.Level;

/**
 * Represents a SettingsManager
 */
public class SettingsManager {
    private SimpleClans plugin;
    private FileConfiguration config;

    private DatabaseConfiguration databaseConfiguration;

    private boolean dropAll, drop;
    private Set<Integer> keepOnTeleport = new HashSet<Integer>();
    private double teleportFuzzyness;
    private int timeUntilTeleport;

    private boolean onlyPvPinWar;
    private boolean saveCivilians;
    private boolean globalFFForced;
    private double killWeightRival, killWeightNeutral, killWeightCivilian;

    private int elementsPerPage;
    private String clanCommand, bbCommand, rankCommand, bankCommand;
    private String serverName;
    private boolean globalFF;
    private int autoSave;
    private String helpFormat;
    private Charset charset;

    private UpdateType buildChannel;
    private boolean longBuildReport;
    private boolean updaterEnabled;

    private boolean reportErrors;
    private String email;

    private int maxTagLenght, minTagLenght, maxNameLenght, minNameLenght;
    private char[] disallowedColors;
    private Set<String> disallowedTags;
    private boolean requireVerification;
    private double purchaseCreationPrice, purchaseVerificationPrice, purchaseTeleportPrice, purchaseInvite;
    private int purgeInactivePlayersDays, purgeInactiveClansDays, purgeUnverifiedClansDays;
    private boolean showUnverifiedClansOnList;
    private int minimalSizeToAlly, minimalSizeToRival;
    private boolean setHomeOnlyOnce;
    private Set<Long> unRivalAbleClans;
    private double rivalLimitPercent;
    private boolean modifyTagCompletely;

    private String clanBB, clanPlayerBB, defaultBB;
    private int maxBBDisplayLines, maxBBLenght;

    private String defaultCape;
    private boolean capesEnabled;

    private String clanAnnounce, clanPlayerAnnounce, defaultAnnounce;

    private boolean voteForDemote, voteForPromote;

    private boolean motdBBEnabled;
    private int motdBBLines;
    private String motdBBFormat;


    private ChatColor headingPageColor, subPageColor, clanColor, leaderColor, trustedColor, untrustedColor;
    private boolean trustMembersByDefault;

    public SettingsManager(SimpleClans plugin)
    {
        this.plugin = plugin;
        init();
    }

    private void init()
    {
        config = plugin.getConfig();
        config.options().copyDefaults(true);
        config.options().header("Available options for the 'build-channel' settings are rb and dev. Use 'rb' to update only recommended builds, dev to update to dev versions or beta to update only to beta builds!");
        save();
        load();
    }

    private void load()
    {
        Logging.debug("Loading Settings...");
        long start = System.currentTimeMillis();
        try {
            ConfigurationSection general = config.getConfigurationSection("general");

            elementsPerPage = general.getInt("elements-per-page");
            serverName = ChatBlock.parseColors(general.getString("server-name"));
            globalFF = general.getBoolean("global-ff");
            autoSave = general.getInt("auto-save");
            helpFormat = ChatBlock.parseColors(general.getString("help-format"));

            try {
                charset = Charset.forName(general.getString("language-charset"));
            } catch (IllegalCharsetNameException e) {
                Logging.debug(Level.WARNING, "The charset was not found! Using default!");
                charset = Charset.defaultCharset();
            } catch (UnsupportedCharsetException e) {
                Logging.debug(Level.WARNING, "The charset is not supported! Using default!");
                charset = Charset.defaultCharset();
            }

            ConfigurationSection updater = config.getConfigurationSection("updater");

            UpdateType updateType = UpdateType.getUpdateType(updater.getString("build-channel"));
            this.longBuildReport = updater.getBoolean("long-build-report");
            this.updaterEnabled = updater.getBoolean("enabled");

            if (updateType == null) {
                Logging.debug("Invalid build-channel! Switching to recommended!");
                updateType = UpdateType.STABLE;
            }

            this.buildChannel = updateType;

            ConfigurationSection reporting = general.getConfigurationSection("reporting");
            reportErrors = reporting.getBoolean("errors");
            String tmpEmail = reporting.getString("email");

            if (tmpEmail != null && !tmpEmail.isEmpty() && GeneralHelper.isValidEmailAddress(tmpEmail)) {
                email = tmpEmail;
            }

            ConfigurationSection commands = config.getConfigurationSection("commands");

            clanCommand = commands.getString("clan");
            bbCommand = commands.getString("bb");
            rankCommand = commands.getString("rank");
            bankCommand = commands.getString("bank");

            ConfigurationSection databaseSection = config.getConfigurationSection("database");

            String mode = databaseSection.getString("mode");
            if (mode.equalsIgnoreCase("mysql")) {
                ConfigurationSection mysqlDatabaseSection = databaseSection.getConfigurationSection("mysql");

                this.databaseConfiguration = new MySQLConfiguration(mysqlDatabaseSection.getString("username"),
                        mysqlDatabaseSection.getString("password"),
                        mysqlDatabaseSection.getString("host"),
                        mysqlDatabaseSection.getInt("port"), mysqlDatabaseSection.getString("database"));
            } else if (mode.equalsIgnoreCase("sqlite")) {
                ConfigurationSection sqliteDatabaseSection = databaseSection.getConfigurationSection("sqlite");

                this.databaseConfiguration = new SQLiteConfiguration(new File(sqliteDatabaseSection.getString("location")));
            } else {
                throw new UnsupportedOperationException("The database mode was not found!");
            }


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
            maxNameLenght = clan.getInt("max-name-lenght");
            minNameLenght = clan.getInt("min-name-lenght");
            List<Character> disallowedColorsList = clan.getCharacterList("disallowed-colors");
            disallowedTags = new HashSet<String>(config.getStringList("disallowed-tags"));
            maxBBLenght = clan.getInt("max-bb-lenght");
            requireVerification = clan.getBoolean("require-verification");
            showUnverifiedClansOnList = clan.getBoolean("show-unverified-clans-on-list");
            minimalSizeToAlly = clan.getInt("minimal-size-to-ally");
            minimalSizeToRival = clan.getInt("minimal-size-to-rival");
            setHomeOnlyOnce = clan.getBoolean("set-home-only-once");
            unRivalAbleClans = new HashSet<Long>(clan.getLongList("unrivalable-clans"));
            rivalLimitPercent = clan.getDouble("rival-limit-percent");
            modifyTagCompletely = clan.getBoolean("modify-tag-completely");
            trustMembersByDefault = clan.getBoolean("trust-members-by-default");

            ConfigurationSection clanEconomy = clan.getConfigurationSection("economy");

            purchaseCreationPrice = clanEconomy.getDouble("purchase-creation");
            purchaseVerificationPrice = clanEconomy.getDouble("purchase-verification");
            purchaseTeleportPrice = clanEconomy.getDouble("purchase-teleport");
            purchaseInvite = clanEconomy.getDouble("purchase-invite");

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

            ConfigurationSection clanAnnounceSection = clan.getConfigurationSection("announce");

            clanAnnounce = clanAnnounceSection.getString("clan");
            clanPlayerAnnounce = clanAnnounceSection.getString("clanplayer");
            defaultAnnounce = clanBBSection.getString("default");


            ConfigurationSection motd = clan.getConfigurationSection("motd");
            motdBBEnabled = motd.getBoolean("bb");
            motdBBLines = motd.getInt("lines");
            motdBBFormat = ChatBlock.parseColors(motd.getString("format"));

            ConfigurationSection voting = clan.getConfigurationSection("voting");
            voteForDemote = voting.getBoolean("demote");
            voteForPromote = voting.getBoolean("promote");

            ConfigurationSection paging = config.getConfigurationSection("paging");

            ConfigurationSection pageColors = paging.getConfigurationSection("colors");

            headingPageColor = ChatColor.getByChar(pageColors.getString("heading-color"));
            subPageColor = ChatColor.getByChar(pageColors.getString("sub-color"));
            clanColor = ChatColor.getByChar(pageColors.getString("clan-color"));
            leaderColor = ChatColor.getByChar(pageColors.getString("leader-color"));
            trustedColor = ChatColor.getByChar(pageColors.getString("trusted-color"));
            untrustedColor = ChatColor.getByChar(pageColors.getString("untrusted-color"));

            ConfigurationSection spout = clan.getConfigurationSection("spout");
            ConfigurationSection capes = spout.getConfigurationSection("capes");

            defaultCape = capes.getString("default");
            capesEnabled = capes.getBoolean("enabled");

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
                    material = Material.valueOf(materialRaw.toUpperCase(Locale.US));
                }

                if (material != null) {
                    keepOnTeleport.add(material.getId());
                } else {
                    Logging.debug(Level.WARNING, "The item or id: %s was not found!", materialRaw);
                }
            }

            //prepares the disallowed colors
            disallowedColors = new char[disallowedColorsList.size()];

            for (int i = 0; i < disallowedColorsList.size(); i++) {
                disallowedColors[i] = disallowedColorsList.get(i);
            }

        } catch (RuntimeException e) {
            ExceptionHelper.handleException(e, getClass());
            return;
        }

        long finish = System.currentTimeMillis();

        Logging.debug("Loading the settings finished! Took %s ms!", finish - start);
    }

    public void loadPermissions()
    {
        File file = new File(plugin.getDataFolder(), "permissions.yml");
        FileConfiguration permissionsConfig = YamlConfiguration.loadConfiguration(file);
        permissionsConfig.options().copyDefaults(true);

        List<String> defaultPermissions = new ArrayList<String>();
        defaultPermissions.add("test.test");
        permissionsConfig.addDefault("permissions.123", defaultPermissions);

        ConfigurationSection permissions = permissionsConfig.getConfigurationSection("permissions");

        for (String clanId : permissions.getKeys(false)) {
            long id;
            try {
                id = Long.parseLong(clanId);
            } catch (NumberFormatException e) {
                Logging.debug("Failed at parsing clan id in the permissions.yml: %s", clanId);
                continue;
            }

            Clan clan = plugin.getClanManager().getClan(id);

            if (clan == null) {
                Logging.debug("The clan with the id %s does not exist!", id);
                continue;
            }

            List<String> clanPermissions = permissions.getStringList(clanId);

            if (clanPermissions.isEmpty()) {
                continue;
            }

            Map<String, Boolean> permSet = new HashMap<String, Boolean>();

            for (String permission : clanPermissions) {
                if (permission.charAt(0) == '^') {
                    permSet.put(permission.substring(1), false);
                } else {
                    permSet.put(permission, true);
                }
            }
            clan.setupPermissions(permSet);
            clan.updatePermissions();
        }

        try {
            permissionsConfig.save(file);
        } catch (IOException e) {
            Logging.debug(e, false);
        }
    }

    public void save()
    {
        plugin.saveConfig();
    }

    public void reload()
    {
        plugin.reloadConfig();
        config = plugin.getConfig();
        load();
    }

    public DatabaseConfiguration getDatabaseConfiguration()
    {
        return databaseConfiguration;
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

    public char[] getDisallowedColors()
    {
        return disallowedColors.clone();
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

    public String getClanCommand()
    {
        return clanCommand;
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

    public ChatColor getHeaderPageColor()
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

    public String getClanAnnounce()
    {
        return clanAnnounce;
    }

    public String getClanPlayerAnnounce()
    {
        return clanPlayerAnnounce;
    }

    public String getDefaultAnnounce()
    {
        return defaultAnnounce;
    }

    public boolean isSetHomeOnlyOnce()
    {
        return setHomeOnlyOnce;
    }

    public boolean isGlobalFF()
    {
        return globalFF;
    }

    public void setGlobalFF(boolean globalFF)
    {
        this.globalFF = globalFF;
        config.getConfigurationSection("general").set("global-ff", globalFF);
        plugin.saveConfig();
    }

    public double getPurchaseCreationPrice()
    {
        return purchaseCreationPrice;
    }

    public double getPurchaseVerificationPrice()
    {
        return purchaseVerificationPrice;
    }

    public double getPurchaseTeleportPrice()
    {
        return purchaseTeleportPrice;
    }

    public boolean isPurchaseCreation()
    {
        return purchaseCreationPrice > 0D;
    }

    public boolean isPurchaseInvite()
    {
        return purchaseInvite > 0D;
    }

    public boolean isPurchaseVerification()
    {
        return purchaseVerificationPrice > 0D;
    }

    public boolean isPurchaseTeleport()
    {
        return purchaseTeleportPrice > 0D;
    }

    public boolean isCapesEnabled()
    {
        return capesEnabled;
    }

    public String getDefaultCape()
    {
        return defaultCape;
    }

    public int getAutoSave()
    {
        return autoSave;
    }

    public int getMaxNameLenght()
    {
        return maxNameLenght;
    }

    public int getMinNameLenght()
    {
        return minNameLenght;
    }

    public boolean isMotdBBEnabled()
    {
        return motdBBEnabled;
    }

    public int getMotdBBLines()
    {
        return motdBBLines;
    }

    public String getMotdBBFormat()
    {
        return motdBBFormat;
    }

    public double getInvitationPrice()
    {
        return purchaseInvite;
    }

    public String getHelpFormat()
    {
        return helpFormat;
    }

    public boolean isVoteForPromote()
    {
        return voteForPromote;
    }

    public boolean isUnRivalAble(Clan clan)
    {
        return unRivalAbleClans.contains(clan.getID());
    }

    public int getMinimalSizeToRival()
    {
        return minimalSizeToRival;
    }

    public double getRivalLimitPercent()
    {
        return rivalLimitPercent;
    }

    public boolean isReportErrors()
    {
        return reportErrors;
    }

    public boolean isModifyTagCompletely()
    {
        return modifyTagCompletely;
    }

    public boolean isTrustMembersByDefault()
    {
        return trustMembersByDefault;
    }

    public String getEmail()
    {
        return email;
    }

    public String getBBCommand()
    {
        return bbCommand;
    }

    public String getRankCommand()
    {
        return rankCommand;
    }

    public String getBankCommand()
    {
        return bankCommand;
    }

    public UpdateType getBuildChannel()
    {
        return buildChannel;
    }

    public boolean isLongBuildReport()
    {
        return longBuildReport;
    }

    public boolean isUpdaterEnabled()
    {
        return updaterEnabled;
    }

    public Charset getCharset()
    {
        return charset;
    }
}
