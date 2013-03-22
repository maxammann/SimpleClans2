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
 *     Last modified: 09.01.13 19:15
 */

package com.p000ison.dev.simpleclans2.api.clanplayer;

import com.p000ison.dev.simpleclans2.api.Balance;
import com.p000ison.dev.simpleclans2.api.KDR;
import com.p000ison.dev.simpleclans2.api.UpdateAble;
import com.p000ison.dev.simpleclans2.api.chat.Row;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 */
public interface ClanPlayer extends KDR, Balance, UpdateAble, Serializable {
    /**
     * Returns the id of the clan the player is in
     *
     * @return The id
     */
    long getClanID();

    /**
     * Gets the clan the player is in.
     *
     * @return The player's clan
     */
    Clan getClan();

    /**
     * Sets the players clan
     *
     * @param clan The clan to set
     */
    void setClan(Clan clan);

    /**
     * Gets the name of this clanPlayer.
     *
     * @return The name of this clanplayer
     */
    String getName();

    /**
     * Checks if this player is banned
     *
     * @return Weather this player is banend
     */
    boolean isBanned();

    /**
     * Bans/unbans a player
     *
     * @param banned Weather to ban or unban
     */
    void setBanned(boolean banned);

    /**
     * Weather this player is a leader
     *
     * @return Weather this player is a leader
     */
    boolean isLeader();

    /**
     * Sets this player to a leader
     *
     * @param leader Weather the player should be a leader
     */
    void setLeader(boolean leader);

    /**
     * Weather the player is trusted
     *
     * @return Weather the player is trusted
     */
    boolean isTrusted();

    /**
     * Sets weather the player is trusted
     */
    void setTrusted(boolean trusted);

    /**
     * Checks if the player has friendly fire on
     *
     * @return Weather the player has friendly fire on
     */
    boolean isFriendlyFireOn();

    /**
     * Sets friendly fire on or off
     *
     * @param friendlyFire Weather friendly fire should be on or off
     */
    void setFriendlyFire(boolean friendlyFire);

    /**
     * Gets the date the player was last seen
     *
     * @return The date this player was last seen
     */
    Date getLastSeenDate();

    void sendMessage(String message);

    void updateLastSeen();

    Date getJoinDate();

    int getNeutralKills();

    void addNeutralKill();

    int getRivalKills();

    void addRivalKill();

    int getCivilianKills();

    void addCivilianKill();

    int getDeaths();

    void addDeath();

    long getID();

    PlayerFlags getFlags();

    void addPastClan(String string);

    Set<String> getPastClans();

    void setFlags(PlayerFlags flags);

    /**
     * Gets the days this clanPlayer is inactive
     *
     * @return The days this clanPlayer is inactive
     */
    int getInactiveDays();

    /**
     * Returns weighted kill score for this player (kills multiplied by the
     * different weights)
     *
     * @return Returns weighted kills.
     */
    double getWeightedKills();

    /**
     * Returns a player object. May be null if the player is not online
     *
     * @return Returns a player object.
     */
    Player toPlayer();

    boolean isOnline();

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    void setNeutralKills(int neutralKills);

    void setRivalKills(int rivalKills);

    void setCivilianKills(int civilianKills);

    void setDeaths(int deaths);

    Rank getRank();

    boolean hasRankPermission(String permission);

    boolean hasRankPermission(int id);

    boolean isRankPermissionNegative(int id);

    boolean isRankPermissionNegative(String permission);

    void assignRank(Rank rank);

    void unassignRank();

    String getLastSeenFormatted();

    boolean unset();

    @Override
    String toString();

    OnlineClanPlayer getOnlineVersion();

    void serverAnnounce(String message);

    /**
     * Gets the name of this player, with the colors this player has. Like leader, trusted, untrusted...
     *
     * @return The colorized name of this player
     */
    String getRankedName();

    Row getStatisticRow();

    /**
     * @param sender The retriever of the message
     * @param clan   The clan of the retriever to display the relation to this clanPlayer
     */
    void showProfile(CommandSender sender, Clan clan);

    /**
     * Returns a formated string of the date this clanplayer has first joined
     *
     * @return Returns a formatted date
     */
    String getFormattedJoinDate();

    /**
     * Returns a formated string of the date this clanplayer was last seen
     *
     * @return Returns a formatted date
     */
    String getFormattedLastSeenDate();

    Row getRosterRow();

    ChatColor getColor();

    boolean isCapeEnabled();

    void setCapeEnabled(boolean enabled);

    boolean isBBEnabled();

    void setBBEnabled(boolean enabled);
}
