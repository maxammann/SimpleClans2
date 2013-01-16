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

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.Balance;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.chat.Row;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.clanplayer.OnlineClanPlayer;
import com.p000ison.dev.simpleclans2.api.clanplayer.PlayerFlags;
import com.p000ison.dev.simpleclans2.api.rank.Rank;
import com.p000ison.dev.simpleclans2.clan.CraftClan;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.DateHelper;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.sqlapi.TableObject;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumn;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumnGetter;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumnSetter;
import com.p000ison.dev.sqlapi.annotation.DatabaseTable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Set;

/**
 * Represents a ClanPlayer
 */
@DatabaseTable(name = "sc2_players")
public class CraftClanPlayer implements ClanPlayer, TableObject {

    private SimpleClans plugin;

    private PlayerFlags flags;
    private Clan clan;
    private Rank rank;
    private OnlineClanPlayer onlineVersion = null;

    @DatabaseColumn(position = 0, databaseName = "id", id = true)
    private long id = -1;
    @DatabaseColumn(position = 1, databaseName = "name", lenght = 16, unique = true)
    private String name;
    @DatabaseColumn(position = 6, databaseName = "banned", defaultValue = "0")
    private boolean banned;
    @DatabaseColumn(position = 5, databaseName = "trusted", defaultValue = "0")
    private boolean trusted;
    @DatabaseColumn(position = 2, databaseName = "leader", defaultValue = "0")
    private boolean leader;
    private long lastSeen, joinDate;
    @DatabaseColumn(position = 8, databaseName = "neutral_kills", defaultValue = "0")
    private int neutralKills;
    @DatabaseColumn(position = 9, databaseName = "rival_kills", defaultValue = "0")
    private int rivalKills;
    @DatabaseColumn(position = 10, databaseName = "civilian_kills", defaultValue = "0")
    private int civilianKills;
    @DatabaseColumn(position = 11, databaseName = "deaths", defaultValue = "0")
    private int deaths;

    private boolean update;

    public CraftClanPlayer(SimpleClans plugin) {
        flags = new CraftPlayerFlags();
        this.plugin = plugin;
    }

    public CraftClanPlayer(SimpleClans plugin, String name) {
        flags = new CraftPlayerFlags();
        this.plugin = plugin;
        this.name = name;
    }

    @Override
    @DatabaseColumnGetter(databaseName = "clan")
    public long getClanID() {
        return clan == null ? -1L : clan.getID();
    }

    @DatabaseColumnSetter(position = 3, databaseName = "clan", defaultValue = "-1")
    @SuppressWarnings("unused")
    private void setClanId(long id) {
        if (id <= 0) {
            this.clan = null;
        }
        this.clan = plugin.getClanManager().getClan(id);
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    @Override
    public void setClan(Clan clan) {
        this.clan = clan;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isBanned() {
        return banned;
    }

    @Override
    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    @Override
    public boolean isLeader() {
        return leader;
    }

    @Override
    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    @Override
    public boolean isTrusted() {
        return trusted;
    }

    @Override
    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    @Override
    public boolean isFriendlyFireOn() {
        return getFlags().isFriendlyFireEnabled();
    }

    @Override
    public void setFriendlyFire(boolean friendlyFire) {
        getFlags().setFriendlyFire(friendlyFire);
    }

    @Override
    public Date getLastSeenDate() {
        return !this.isOnline() ? new Date(lastSeen) : new Date();
    }

    public long getLastSeenTime() {
        return !this.isOnline() ? lastSeen : System.currentTimeMillis();
    }

    @Override
    public void updateLastSeen() {
        this.lastSeen = System.currentTimeMillis();
    }

    @Override
    public Date getJoinDate() {
        return new Date(joinDate);
    }

    public void setJoinTime(long joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public int getNeutralKills() {
        return neutralKills;
    }

    @Override
    public void addNeutralKill() {
        neutralKills++;
    }

    @Override
    public int getRivalKills() {
        return rivalKills;
    }

    @Override
    public void addRivalKill() {
        rivalKills++;
    }

    @Override
    public int getCivilianKills() {
        return civilianKills;
    }

    @Override
    public void addCivilianKill() {
        civilianKills++;
    }

    @Override
    public int getDeaths() {
        return deaths;
    }

    @Override
    public void addDeath() {
        this.setDeaths(this.getDeaths() + 1);
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public PlayerFlags getFlags() {
        return flags;
    }

    @Override
    public void addPastClan(String string) {
        flags.addPastClan(string);
    }

    @Override
    public Set<String> getPastClans() {
        return flags.getPastClans();
    }

    @Override
    public void setFlags(PlayerFlags flags) {
        this.flags = flags;
    }

    @Override
    public int getInactiveDays() {
        return (int) Math.round(DateHelper.differenceInDays(lastSeen, System.currentTimeMillis()));
    }

    @Override
    public double getWeightedKills() {
        return (getRivalKills() * plugin.getSettingsManager().getKillWeightRival()) + (getNeutralKills() * plugin.getSettingsManager().getKillWeightNeutral()) + (getCivilianKills() * plugin.getSettingsManager().getKillWeightCivilian());
    }

    @Override
    public float getKDR() {
        int totalDeaths = getDeaths();

        if (totalDeaths == 0) {
            totalDeaths = 1;
        }

        return ((float) getWeightedKills()) / ((float) totalDeaths);
    }

    @Override
    public Player toPlayer() {
        return plugin.getServer().getPlayerExact(name);
    }

    @Override
    public boolean isOnline() {
        return onlineVersion != null && GeneralHelper.isOnline(toPlayer());
    }

    public boolean equals(Object obj) {
        if (obj instanceof ClanPlayer) {
            ClanPlayer clanPlayer = ((ClanPlayer) obj);

            if (clanPlayer.getID() == id || clanPlayer.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return (int) id;
    }

    @Override
    public void setNeutralKills(int neutralKills) {
        this.neutralKills = neutralKills;
    }

    @Override
    public void setRivalKills(int rivalKills) {
        this.rivalKills = rivalKills;
    }

    @Override
    public void setCivilianKills(int civilianKills) {
        this.civilianKills = civilianKills;
    }

    @Override
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    @Override
    public Rank getRank() {
        return rank;
    }

    @Override
    public boolean hasRankPermission(String permission) {
        return rank != null && getRank().hasPermission(permission);
    }

    @Override
    public boolean hasRankPermission(int id) {
        return rank != null && getRank().hasPermission(id);
    }

    @Override
    public boolean isRankPermissionNegative(int id) {
        return rank != null && getRank().isNegative(id);
    }

    @Override
    public boolean isRankPermissionNegative(String permission) {
        return rank != null && getRank().isNegative(permission);
    }

    @Override
    public void assignRank(Rank rank) {
        this.rank = rank;
    }

    @Override
    public void unassignRank() {
        this.rank = null;
    }

    @Override
    public void update() {
        this.update = true;
    }

    public boolean needsUpdate() {
        return this.update;
    }

    @Override
    public void update(boolean update) {
        this.update = update;
    }

    @Override
    public String getLastSeenFormatted() {
        long current = System.currentTimeMillis();
        long difference = DateHelper.differenceInMilliseconds(getLastSeenTime(), current);

        //if the difference is more than a day
        if (difference > DateHelper.DAY) {

            long days = Math.round(DateHelper.differenceInDays(getLastSeenTime(), current));
            return days == 1 ? "1 " + Language.getTranslation("day") : days + " " + Language.getTranslation("days");

        } else if (difference > DateHelper.HOUR) {

            long hours = Math.round(DateHelper.differenceInHours(getLastSeenTime(), current));
            return hours == 1 ? "1 " + Language.getTranslation("hour") : hours + " " + Language.getTranslation("hours");

        } else if (difference > DateHelper.MINUTE) {

            long minutes = Math.round(DateHelper.differenceInMinutes(getLastSeenTime(), current));
            return minutes == 1 ? "1 " + Language.getTranslation("minute") : minutes + " " + Language.getTranslation("minutes");

        } else /*if (difference > DateHelper.SECOND)*/ {

            long seconds = Math.round(DateHelper.differenceInSeconds(getLastSeenTime(), current));
            return seconds == 1 ? "1 " + Language.getTranslation("second") : seconds + " " + Language.getTranslation("seconds");

        }
    }

    @Override
    public boolean unset() {
        boolean change = false;
        if (getClan() != null) {
            setClan(null);
            change = true;
        }
        if (isLeader()) {
            this.setLeader(false);
            change = true;
        }
        if (isTrusted()) {
            this.setTrusted(false);
            change = true;
        }

        return change;
    }

    public String toString() {
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

    public void removeOnlineVersion() {
        this.onlineVersion = null;
    }

    public void setupOnlineVersion() {
        this.onlineVersion = new CraftOnlineClanPlayer(plugin, this);
    }

    @Override
    public com.p000ison.dev.simpleclans2.api.clanplayer.OnlineClanPlayer getOnlineVersion() {
        return onlineVersion;
    }

    public void updatePermissions() {
        if (onlineVersion == null) {
            return;
        }

        onlineVersion.removePermissions();
        onlineVersion.setupPermissions();
    }

    public void removePermissions() {
        if (onlineVersion == null) {
            return;

        }
        onlineVersion.removePermissions();
    }

    @Override
    public void serverAnnounce(String message) {
        SimpleClans.serverAnnounceRaw(ChatBlock.parseColors(plugin.getSettingsManager().getClanPlayerAnnounce().replace("+player", this.getName()).replace("+message", message)));
    }

    @Override
    public String getRankedName() {
        return (this.isLeader() ? plugin.getSettingsManager().getLeaderColor() : ((this.isTrusted() ? plugin.getSettingsManager().getTrustedColor() : plugin.getSettingsManager().getUntrustedColor()))) + this.getName();
    }

    @Override
    public Row getStatisticRow() {
        String name = this.getRankedName();
        int rival = this.getRivalKills();
        int neutral = this.getNeutralKills();
        int civilian = this.getCivilianKills();
        int deaths = this.getDeaths();
        String kdr = CraftClan.DECIMAL_FORMAT.format(this.getKDR());

        return new Row(name, ChatColor.YELLOW + kdr, ChatColor.WHITE.toString() + rival, ChatColor.GRAY.toString() + neutral, ChatColor.DARK_GRAY.toString() + civilian, ChatColor.DARK_RED.toString() + deaths);
    }

    @Override
    public void showProfile(CommandSender sender, Clan clan) {
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
        String kdr = ChatColor.YELLOW + CraftClan.DECIMAL_FORMAT.format(this.getKDR());
        String rawPastClans = GeneralHelper.arrayToString(ChatColor.RESET + ", ", this.getPastClans());
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

    @Override
    public String getFormattedJoinDate() {
        return new java.text.SimpleDateFormat("MMM dd, yyyy h:mm a").format(new Date(this.joinDate));
    }

    @Override
    public String getFormattedLastSeenDate() {
        return new java.text.SimpleDateFormat("MMM dd, yyyy h:mm a").format(new Date(this.lastSeen));
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount < 0.0D) {
            throw new IllegalArgumentException("The amount can not be negative if you withdraw something!");
        }

        double balance = SimpleClans.getBalance(name);

        return balance >= amount && SimpleClans.withdrawBalance(name, amount);
    }

    @Override
    public void deposit(double amount) {
        if (amount < 0.0D) {
            throw new IllegalArgumentException("The amount can not be negative if you deposit something!");
        }
        SimpleClans.depositBalance(name, amount);
    }

    @Override
    public boolean transfer(Balance account, double amount) {
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
    public double getBalance() {
        return SimpleClans.getBalance(name);
    }

    @Override
    public Row getRosterRow() {
        String name = this.getColor() + this.getName();
        String lastSeen = (GeneralHelper.isOnline(toPlayer()) ? ChatColor.GREEN + Language.getTranslation("online") : ChatColor.WHITE + this.getLastSeenFormatted());
        return new Row(name, ChatColor.YELLOW + (getRank() == null ? Language.getTranslation("none") : getRank().getTag()), lastSeen);
    }

    @Override
    public ChatColor getColor() {
        return this.isTrusted() ? this.isLeader() ? plugin.getSettingsManager().getLeaderColor() : plugin.getSettingsManager().getTrustedColor() : plugin.getSettingsManager().getUntrustedColor();
    }

    @Override
    public long getLastUpdated() {
        return getLastSeenTime();
    }

    @Override
    public boolean isCapeEnabled() {
        return getFlags() == null || getFlags().isCapeEnabled();
    }

    @Override
    public void setCapeEnabled(boolean enabled) {
        if (getFlags() == null) {
            return;
        }
        getFlags().setCapeEnabled(enabled);
    }

    @Override
    public boolean isBBEnabled() {
        return getFlags() == null || getFlags().isBBEnabled();
    }

    @Override
    public void setBBEnabled(boolean enabled) {
        if (getFlags() == null) {
            return;
        }
        getFlags().setBBEnabled(enabled);
    }

    @DatabaseColumnSetter(position = 4, databaseName = "ranks", saveValueAfterLoading = true)
    private void setDatabaseRank(String json) {
        if (json == null || getClan() == null) {
            return;
        }
        int rankId = Integer.valueOf(json.substring(1, json.length() - 1));
        this.rank = getClan().getRank(rankId);
    }

    @DatabaseColumnGetter(databaseName = "ranks")
    private String getDatabaseRank() {
        return getRank() == null ? null : ('[' + String.valueOf(getRank().getID()) + ']');
    }

    @DatabaseColumnGetter(databaseName = "join_date")
    private Date getDatabaseJoinDate() {
        return new Date(joinDate);
    }

    @DatabaseColumnSetter(position = 7, databaseName = "join_date")
    private void setDatabaseJoinDate(Date date) {
        if (date == null) {
            this.joinDate = System.currentTimeMillis();
            return;
        }
        this.joinDate = date.getTime();
    }

    @DatabaseColumnGetter(databaseName = "last_seen")
    private Date getDatabaseLastSeen() {
        return new Date(lastSeen);
    }

    @DatabaseColumnSetter(position = 7, databaseName = "last_seen")
    private void setDatabaseLastSeen(Date date) {
        if (date == null) {
            this.lastSeen = System.currentTimeMillis();
            return;
        }
        this.lastSeen = date.getTime();
    }

    @DatabaseColumnGetter(databaseName = "flags")
    private String getDatabaseFlags() {
        if (getFlags() == null) {
            return null;
        }
        return flags.serialize();
    }

    @DatabaseColumnSetter(position = 12, databaseName = "flags")
    private void setDatabaseFlags(String json) {
        if (flags == null) {
            this.flags = new CraftPlayerFlags();
        }
        flags.deserialize(json);
    }
}
