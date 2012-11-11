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
 *     Last modified: 10.10.12 21:57
 */

package com.p000ison.dev.simpleclans2.clanplayer;

import com.p000ison.dev.simpleclans2.KDR;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.UpdateAble;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clan.bank.Balance;
import com.p000ison.dev.simpleclans2.clan.ranks.Rank;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.DateHelper;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.util.chat.Row;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Set;

/**
 * Represents a ClanPlayer
 */
public class ClanPlayer implements KDR, Balance, UpdateAble {

    private SimpleClans plugin;

    private PlayerFlags flags;
    private Clan clan;
    private Rank rank;
    private OnlineClanPlayer onlineVersion = null;

    private long id = -1;
    private String name;
    private boolean banned, leader, trusted;
    private long lastSeen, joinDate;
    private int neutralKills, rivalKills, civilianKills, deaths;

    private boolean update;

    public ClanPlayer(SimpleClans plugin, String name)
    {
        flags = new PlayerFlags();
        this.plugin = plugin;
        this.name = name;
    }

    public ClanPlayer(SimpleClans plugin, long id, String name)
    {
        this(plugin, name);
        this.id = id;
    }

    /**
     * Returns the id of the clan the player is in
     *
     * @return The id
     */
    public long getClanId()
    {
        return clan == null ? -1L : clan.getId();
    }

    /**
     * Gets the clan the player is in.
     *
     * @return The player's clan
     */
    public Clan getClan()
    {
        return clan;
    }

    /**
     * Sets the players clan
     *
     * @param clan The clan to set
     */
    public void setClan(Clan clan)
    {
        this.clan = clan;
    }

    /**
     * Gets the name of this clanPlayer.
     *
     * @return The name of this clanplayer
     */
    public String getName()
    {
        return name;
    }

    /**
     * Checks if this player is banned
     *
     * @return Weather this player is banend
     */
    public boolean isBanned()
    {
        return banned;
    }

    /**
     * Bans/unbans a player
     *
     * @param banned Weather to ban or unban
     */
    public void setBanned(boolean banned)
    {
        this.banned = banned;
    }

    /**
     * Weather this player is a leader
     *
     * @return Weather this player is a leader
     */
    public boolean isLeader()
    {
        return leader;
    }

    /**
     * Sets this player to a leader
     *
     * @param leader Weather the player should be a leader
     */
    public void setLeader(boolean leader)
    {
        this.leader = leader;
    }

    /**
     * Weather the player is trusted
     *
     * @return Weather the player is trusted
     */
    public boolean isTrusted()
    {
        return trusted;
    }

    /**
     * Sets weather the player is trusted
     */
    public void setTrusted(boolean trusted)
    {
        this.trusted = trusted;
    }

    /**
     * Checks if the player has friendly fire on
     *
     * @return Weather the player has friendly fire on
     */
    public boolean isFriendlyFireOn()
    {
        return getFlags().isFriendlyFireEnabled();
    }

    /**
     * Sets friendly fire on or off
     *
     * @param friendlyFire Weather friendly fire should be on or off
     */
    public void setFriendlyFire(boolean friendlyFire)
    {
        getFlags().setFriendlyFire(friendlyFire);
    }

    /**
     * Gets the date the player was last seen
     *
     * @return The date this player was last seen
     */
    public long getLastSeenDate()
    {
        return !this.isOnline() ? lastSeen : System.currentTimeMillis();
    }

    public void setLastSeenDate(long lastSeen)
    {
        this.lastSeen = lastSeen;
    }

    public void updateLastSeen()
    {
        this.lastSeen = System.currentTimeMillis();
    }

    public long getJoinDate()
    {
        return joinDate;
    }

    public void setJoinDate(long joinDate)
    {
        this.joinDate = joinDate;
    }

    public int getNeutralKills()
    {
        return neutralKills;
    }

    public void addNeutralKill()
    {
        neutralKills++;
    }

    public int getRivalKills()
    {
        return rivalKills;
    }

    public void addRivalKill()
    {
        rivalKills++;
    }

    public int getCivilianKills()
    {
        return civilianKills;
    }

    public void addCivilianKill()
    {
        civilianKills++;
    }

    public int getDeaths()
    {
        return deaths;
    }

    public void addDeath()
    {
        this.setDeaths(this.getDeaths() + 1);
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public PlayerFlags getFlags()
    {
        return flags;
    }

    public void addPastClan(String string)
    {
        flags.addPastClan(string);
    }

    public Set<String> getPastClans()
    {
        return flags.getPastClans();
    }

    public void setFlags(PlayerFlags flags)
    {
        this.flags = flags;
    }

    /**
     * Gets the days this clanPlayer is inactive
     *
     * @return The days this clanPlayer is inactive
     */
    public int getInactiveDays()
    {
        return (int) Math.round(DateHelper.differenceInDays(lastSeen, System.currentTimeMillis()));
    }

    /**
     * Returns weighted kill score for this player (kills multiplied by the
     * different weights)
     *
     * @return Returns weighted kills.
     */
    public double getWeightedKills()
    {
        return (getRivalKills() * plugin.getSettingsManager().getKillWeightRival()) + (getNeutralKills() * plugin.getSettingsManager().getKillWeightNeutral()) + (getCivilianKills() * plugin.getSettingsManager().getKillWeightCivilian());
    }

    /**
     * Returns weighted-kill/death ratio
     *
     * @return The KDR.
     */
    public float getKDR()
    {
        int totalDeaths = getDeaths();

        if (totalDeaths == 0) {
            totalDeaths = 1;
        }

        return ((float) getWeightedKills()) / ((float) totalDeaths);
    }

    /**
     * Returns a player object. May be null if the player is not online
     *
     * @return Returns a player object.
     */
    public Player toPlayer()
    {
        if (onlineVersion == null) {
            return null;
        }
        return plugin.getServer().getPlayerExact(name);
    }

    public boolean isOnline()
    {
        return onlineVersion != null && GeneralHelper.isOnline(toPlayer());
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ClanPlayer) {
            ClanPlayer clanPlayer = ((ClanPlayer) obj);

            if (clanPlayer.getId() == id || clanPlayer.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return (int) id;
    }

    public void setNeutralKills(int neutralKills)
    {
        this.neutralKills = neutralKills;
    }

    public void setRivalKills(int rivalKills)
    {
        this.rivalKills = rivalKills;
    }

    public void setCivilianKills(int civilianKills)
    {
        this.civilianKills = civilianKills;
    }

    public void setDeaths(int deaths)
    {
        this.deaths = deaths;
    }

    public Rank getRank()
    {
        return rank;
    }

    public boolean hasRankPermission(String permission)
    {
        return rank != null && getRank().hasPermission(permission);
    }

    public boolean hasRankPermission(int id)
    {
        return rank != null && getRank().hasPermission(id);
    }

    public boolean isRankPermissionNegative(int id)
    {
        return rank != null && getRank().isNegative(id);
    }

    public boolean isRankPermissionNegative(String permission)
    {
        return rank != null && getRank().isNegative(permission);
    }

    public void setRank(Rank rank)
    {
        this.rank = rank;
    }

    public void update()
    {
        this.update = true;
    }

    public boolean needsUpdate()
    {
        return this.update;
    }

    public void update(boolean update)
    {
        this.update = update;
    }

    public String getLastSeen()
    {
        long current = System.currentTimeMillis();
        long difference = DateHelper.differenceInMilliseconds(getLastSeenDate(), current);

        //if the difference is more than a day
        if (difference > DateHelper.DAY) {

            long days = Math.round(DateHelper.differenceInDays(getLastSeenDate(), current));
            return days == 1 ? "1 " + Language.getTranslation("day") : days + " " + Language.getTranslation("days");

        } else if (difference > DateHelper.HOUR) {

            long hours = Math.round(DateHelper.differenceInHours(getLastSeenDate(), current));
            return hours == 1 ? "1 " + Language.getTranslation("hour") : hours + " " + Language.getTranslation("hours");

        } else if (difference > DateHelper.MINUTE) {

            long minutes = Math.round(DateHelper.differenceInMinutes(getLastSeenDate(), current));
            return minutes == 1 ? "1 " + Language.getTranslation("minute") : minutes + " " + Language.getTranslation("minutes");

        } else /*if (difference > DateHelper.SECOND)*/ {

            long seconds = Math.round(DateHelper.differenceInSeconds(getLastSeenDate(), current));
            return seconds == 1 ? "1 " + Language.getTranslation("second") : seconds + " " + Language.getTranslation("seconds");

        }
    }

    public void unset()
    {
        setClan(null);
        this.setLeader(false);
        this.setTrusted(false);
    }

    @Override
    public String toString()
    {
        return "ClanPlayer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", clan=" + clan +
                ", leader=" + leader +
                ", update=" + update +
                ", rank=" + rank +
                ", deaths=" + deaths +
                ", civilianKills=" + civilianKills +
                ", rivalKills=" + rivalKills +
                ", neutralKills=" + neutralKills +
                ", lastSeen=" + lastSeen +
                ", trusted=" + trusted +
                ", banned=" + banned +
                '}';
    }

    public void removeOnlineVersion()
    {
        this.onlineVersion = null;
    }

    public void setupOnlineVersion()
    {
        this.onlineVersion = new OnlineClanPlayer(plugin, this);
    }

    public OnlineClanPlayer getOnlineVersion()
    {
        return onlineVersion;
    }

    public void updatePermissions()
    {
        if (onlineVersion == null) {
            return;
        }

        onlineVersion.removePermissions();
        onlineVersion.setupPermissions();
    }

    public void removePermissions()
    {
        if (onlineVersion == null) {
            return;

        }
        onlineVersion.removePermissions();
    }

    public void serverAnnounce(String message)
    {
        SimpleClans.serverAnnounceRaw(ChatBlock.parseColors(plugin.getSettingsManager().getClanPlayerAnnounce().replace("+player", this.getName()).replace("+message", message)));
    }

    /**
     * Gets the name of this player, with the colors this player has. Like leader, trusted, untrusted...
     *
     * @return The colorized name of this player
     */
    public String getRankedName()
    {
        return (this.isLeader() ? plugin.getSettingsManager().getLeaderColor() : ((this.isTrusted() ? plugin.getSettingsManager().getTrustedColor() : plugin.getSettingsManager().getUntrustedColor()))) + this.getName();
    }

    public Row getStatisticRow()
    {
        String name = this.getRankedName();
        int rival = this.getRivalKills();
        int neutral = this.getNeutralKills();
        int civilian = this.getCivilianKills();
        int deaths = this.getDeaths();
        String kdr = Clan.DECIMAL_FORMAT.format(this.getKDR());

        return new Row(name, ChatColor.YELLOW + kdr, ChatColor.WHITE.toString() + rival, ChatColor.GRAY.toString() + neutral, ChatColor.DARK_GRAY.toString() + civilian, ChatColor.DARK_RED.toString() + deaths);
    }


    /**
     * @param sender The retriever of the message
     * @param clan   The clan of the retriever to display the relation to this clanPlayer
     */
    public void showProfile(CommandSender sender, Clan clan)
    {
        ChatColor headColor = plugin.getSettingsManager().getHeaderPageColor();
        ChatColor subColor = plugin.getSettingsManager().getSubPageColor();

        ChatBlock.sendBlank(sender);
        ChatBlock.sendHead(sender, Language.getTranslation("s.player.info", plugin.getSettingsManager().getClanColor() + this.getName()), null);
        ChatBlock.sendBlank(sender);

        String clanName;

        if (getClan() != null) {
            clanName = Language.getTranslation("clan.lookup", getClan().getTag(), getClan().getName());
        } else {
            clanName = ChatColor.WHITE + Language.getTranslation("none");
        }

        String rankName;

        if (getRank() != null) {
            rankName = Language.getTranslation("clan.lookup", rank.getTag(), getRank().getName());
        } else {
            rankName = ChatColor.WHITE + Language.getTranslation("none");
        }

        String status = getClan() == null ? ChatColor.WHITE + Language.getTranslation("free.agent") : (this.isLeader() ? plugin.getSettingsManager().getLeaderColor() + Language.getTranslation("leader") : (this.isTrusted() ? plugin.getSettingsManager().getTrustedColor() + Language.getTranslation("trusted") : plugin.getSettingsManager().getUntrustedColor() + Language.getTranslation("untrusted")));
        String joinDate = ChatColor.WHITE.toString() + this.getFormattedJoinDate();
        String lastSeen = ChatColor.WHITE.toString() + this.getFormattedLastSeenDate();
        String inactive = ChatColor.WHITE.toString() + this.getInactiveDays() + subColor + "/" + ChatColor.WHITE + plugin.getSettingsManager().getPurgeInactivePlayersDays() + " days";
        String rival = ChatColor.WHITE.toString() + this.getRivalKills();
        String neutral = ChatColor.WHITE.toString() + this.getNeutralKills();
        String civilian = ChatColor.WHITE.toString() + this.getCivilianKills();
        String deaths = ChatColor.WHITE.toString() + this.getDeaths();
        String kdr = ChatColor.YELLOW + Clan.DECIMAL_FORMAT.format(this.getKDR());
        String rawPastClans = GeneralHelper.arrayToString(", ", this.getPastClans());
        String pastClans = ChatColor.WHITE + (rawPastClans == null ? Language.getTranslation("none") : rawPastClans);

        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("clan.0"), clanName));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("rank.0"), rankName));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("status.0"), status));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("kdr.0"), kdr));
        ChatBlock.sendMessage(sender, "  " + subColor + Language.getTranslation("kill.totals") + " " + headColor + "[" + Language.getTranslation("rival") + ":" + rival + " " + headColor + Language.getTranslation("neutral") + ":" + neutral + " " + headColor + Language.getTranslation("civilian") + ":" + civilian + headColor + "]");
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("deaths.0"), deaths));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("join.date.0"), joinDate));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("last.seen.0"), lastSeen));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("past.clans.0"), pastClans));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("inactive.0"), inactive));

        if (getClan() != null) {
            if (!sender.getName().equals(name)) {
                String killType = ChatColor.GRAY + Language.getTranslation("neutral");

                if (getClan() == null) {
                    killType = ChatColor.DARK_GRAY + Language.getTranslation("civilian");
                } else if (clan != null && clan.isRival(getClan())) {
                    killType = ChatColor.WHITE + Language.getTranslation("rival");
                }

                ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("kill.type.0"), killType));
            }
        }
    }

    /**
     * Returns a formated string of the date this clanplayer has first joined
     *
     * @return Returns a formatted date
     */
    public String getFormattedJoinDate()
    {
        return Clan.DATE_FORMAT.format(new Date(this.joinDate));
    }

    /**
     * Returns a formated string of the date this clanplayer was last seen
     *
     * @return Returns a formatted date
     */
    public String getFormattedLastSeenDate()
    {
        return Clan.DATE_FORMAT.format(new Date(this.lastSeen));
    }

    @Override
    public boolean withdraw(double amount)
    {
        if (amount < 0.0D) {
            throw new IllegalArgumentException("The amount can not be negative if you withdraw something!");
        }

        double balance = SimpleClans.getBalance(name);

        if (balance < amount) {
            return false;
        }

        return SimpleClans.withdrawBalance(name, amount);
    }

    @Override
    public void deposit(double amount)
    {
        if (amount < 0.0D) {
            throw new IllegalArgumentException("The amount can not be negative if you deposit something!");
        }
        SimpleClans.depositBalance(name, amount);
    }

    @Override
    public boolean transfer(Balance account, double amount)
    {
        if (amount > 0.0D) {
            amount = Math.abs(amount);

            if (!this.withdraw(amount)) {
                return false;
            }

            account.deposit(amount);
        } else {
            amount = Math.abs(amount);
            if (!account.withdraw(amount)) {
                return false;
            }

            this.deposit(amount);
        }

        return true;
    }

    @Override
    public double getBalance()
    {
        return SimpleClans.getBalance(name);
    }

    public Row getRosterRow()
    {
        String name = plugin.getSettingsManager().getLeaderColor() + this.getName();
        String lastSeen = (GeneralHelper.isOnline(toPlayer()) ? ChatColor.GREEN + Language.getTranslation("online") : ChatColor.WHITE + this.getLastSeen());
        return new Row(name, ChatColor.YELLOW + this.getRank().getTag(), lastSeen);
    }

    @Override
    public long getLastUpdated()
    {
        return getLastSeenDate();
    }

    public boolean isCapeEnabled()
    {
        return getFlags() == null || getFlags().isCapeEnabled();
    }

    public void setCapeEnabled(boolean enabled)
    {
        if (getFlags() == null) {
            return;
        }
        getFlags().setCapeEnabled(enabled);
    }

    public boolean isBBEnabled()
    {
        return getFlags() == null || getFlags().isBBEnabled();
    }

    public void setBBEnabled(boolean enabled)
    {
        if (getFlags() == null) {
            return;
        }
        getFlags().setBBEnabled(enabled);
    }
}
