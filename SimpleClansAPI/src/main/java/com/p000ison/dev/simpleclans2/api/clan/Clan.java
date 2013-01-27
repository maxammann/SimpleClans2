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

package com.p000ison.dev.simpleclans2.api.clan;

import com.p000ison.dev.simpleclans2.api.Balance;
import com.p000ison.dev.simpleclans2.api.KDR;
import com.p000ison.dev.simpleclans2.api.UpdateAble;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.rank.Rank;
import org.bukkit.command.CommandSender;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 */
public interface Clan extends KDR, Comparable<Clan>, Balance, UpdateAble, Serializable, Iterable<ClanPlayer> {

    /**
     * Returns a formated string of the date this clan was founded
     *
     * @return Returns a formatted date
     */
    String getFoundedDateFormatted();

    /**
     * Gets the tag of a clan. This is unique and can contain colors.
     *
     * @return The tag.
     */
    String getTag();

    /**
     * Sets the tag of this clan
     *
     * @param tag The tag
     */
    void setTag(String tag);

    /**
     * Gets the unique id of this clan.
     *
     * @return The id.
     */
    long getID();

    /**
     * Returns the JSONFlags of this clan. {@link ClanFlags} contains the flags of this clan.
     *
     * @return The flags of this clan.
     * @see ClanFlags
     */
    ClanFlags getFlags();

    /**
     * Gets the name of this clan.
     *
     * @return The name of this clan.
     */
    String getName();

    /**
     * Sets the name of this clan
     *
     * @param name The name of this clan
     */
    void setName(String name);

    /**
     * Gets the time in milliseconds in the unix time
     *
     * @return The time
     */
    Date getLastActionDate();

    /**
     * Updates the last action to NOW!
     */
    void updateLastAction();

    /**
     * Returns the date then this clan was founded in milliseconds in the unix time.
     *
     * @return The found date,
     */
    Date getFoundedDate();

    /**
     * Checks if this clan is verified.
     *
     * @return Weather this clan is verified.
     */
    boolean isVerified();

    /**
     * Sets this clan verified.
     *
     * @param verified Weather this clan should be verified
     */
    void setVerified(boolean verified);

    /**
     * Checks if friendly fire is on.
     *
     * @return Weather friendly fire is on of of.
     */
    boolean isFriendlyFireOn();

    /**
     * Sets friendly fire for this clan.
     *
     * @param friendlyFire Weather friendly fire is on or off.
     */
    void setFriendlyFire(boolean friendlyFire);

    /**
     * Returns a set of allies
     *
     * @return The allies.
     */
    Set<Clan> getAllies();

    /**
     * Returns a set of rivals
     *
     * @return The rival clans.
     */
    Set<Clan> getRivals();

    /**
     * Returns a set of warring clans
     *
     * @return The warring clans.
     */
    Set<Clan> getWarringClans();

    /**
     * Checks weather the clan is a ally.
     *
     * @param id The id of the other clan.
     * @return Weather they are allies or not.
     */
    boolean isAlly(long id);

    /**
     * Checks weather the clan  is a ally.
     *
     * @param clan The other clan
     * @return Weather they are allies or not.
     */
    boolean isAlly(Clan clan);

    /**
     * Checks weather the clan with the id is a rival.
     *
     * @param id The id of the other clan.
     * @return Weather they are rivals or not.
     */
    boolean isRival(long id);

    /**
     * Checks weather the clan is a rival.
     *
     * @param clan The other clan.
     * @return Weather they are rivals or not.
     */
    boolean isRival(Clan clan);

    /**
     * Checks weather the clan is warring with this one.
     *
     * @param id The id of the other clan.
     * @return Weather they are warring or not.
     */
    boolean isWarring(long id);

    /**
     * Checks weather the clan is warring with this one.
     *
     * @param clan The other clan.
     * @return Weather they are warring or not.
     */
    boolean isWarring(Clan clan);

    /**
     * Gets the days this clan is inactive
     *
     * @return The days this clan is inactive
     */
    int getInactiveDays();

    /**
     * Sets the leader of this clan
     *
     * @param clanPlayer The player to set
     * @return Weather it was successfully
     */
    boolean setLeader(ClanPlayer clanPlayer);

    /**
     * Demotes a leader to a normal player
     *
     * @param clanPlayer The player to demote
     * @return Weather it was successfully
     */
    boolean demote(ClanPlayer clanPlayer);

    /**
     * Checks if the player is a member of this clan. This means no leader.
     *
     * @param cp The player
     * @return Checks if the player is a member
     */
    boolean isMember(ClanPlayer cp);

    /**
     * Checks if the player the leader of this clan.
     *
     * @param cp The player
     * @return Checks if the player is the leader
     */
    boolean isLeader(ClanPlayer cp);

    /**
     * Checks if the player is any member of this clan. This means member/leader
     *
     * @param cp The player
     * @return Checks if the player is any member
     */
    boolean isAnyMember(ClanPlayer cp);

    /**
     * Gets all the members excluded the leader
     *
     * @return A Set of the members
     */
    Set<ClanPlayer> getMembers();

    /**
     * Returns all members including the leader
     *
     * @return A set of all members of this clan
     */
    Set<ClanPlayer> getAllMembers();

    /**
     * Returns a set of all leaders
     *
     * @return A Set of all leaders
     */
    Set<ClanPlayer> getLeaders();

    /**
     * Adds a player as member to this clan
     *
     * @param clanPlayer The player
     */
    void addMember(ClanPlayer clanPlayer);

    /**
     * Gets the total kdr of all members
     *
     * @return The total KDR
     */
    @Override
    float getKDR();

    /**
     * The size of this clan
     *
     * @return The total size of this clan
     */
    int getSize();

    /**
     * Adds a message to the bb
     *
     * @param announcer The announcer of this message
     * @param msg       The message which should be posted
     */
    void addBBMessage(ClanPlayer announcer, String msg);

    /**
     * Adds a message to the bb
     *
     * @param announcer The announcer clan of this message
     * @param msg       The message which should be posted
     */
    void addBBMessage(Clan announcer, String msg);

    /**
     * Announces a message to all clan members
     *
     * @param announcer The announcer of this message
     * @param msg       The message which should be announced
     */
    void announce(ClanPlayer announcer, String msg);

    /**
     * Announces a message to all clan members
     *
     * @param announcer The announcer clan of this message
     * @param msg       The message which should be announced
     */
    void announce(Clan announcer, String msg);

    /**
     * Announces a message to all clan members
     *
     * @param msg The message which should be announced
     */
    void announce(String msg);

    /**
     * Gets a clean comparable tag of this clan
     *
     * @return The clean tag
     */
    String getCleanTag();

    void addBBMessage(String msg);

    void clearBB();

    /**
     * Returns whether this object equals an other.
     * You should implement this!
     *
     * @param otherClan The other clan
     * @return Whether they are equal
     */
    @Override
    boolean equals(Object otherClan);

    /**
     * Returns the hashCode of this object.
     * You should implement this!
     *
     * @return The hashCode
     */
    @Override
    int hashCode();

    void addRival(Clan rival);

    void addAlly(Clan ally);

    void addWarringClan(Clan warringClan);

    void removeRival(Clan rival);

    void removeAlly(Clan ally);

    void removeWarringClan(Clan warringClan);

    /**
     * Removes a member from this clan
     *
     * @param clanPlayer The member to remove
     */
    void removeMember(ClanPlayer clanPlayer);

    /**
     * Disbands this (performs all necessary steps)
     */
    void disband();

    /**
     * Checks if this clan has allies
     *
     * @return Weather this clan has allies
     */
    boolean hasAllies();

    /**
     * Checks if this clan has rivals
     *
     * @return Weather this clan has rivals
     */
    boolean hasRivals();

    /**
     * Checks if this clan has warring clans
     *
     * @return Weather this clan has warring clans
     */
    boolean hasWarringClans();

    /**
     * Checks if all leaders are online
     *
     * @return Weather all players are online
     */
    boolean allLeadersOnline();

    /**
     * Checks if all leaders are online
     *
     * @param ignore Null or a player to ignore
     * @return Weather all players are online
     */
    boolean allLeadersOnline(ClanPlayer ignore);

    /**
     * This return a array of kills this clan made. This needs only one iteration.
     * <p/>
     * <p><strong>Total Deaths: </strong><i>Index 0</i></p>
     * <p><strong>Total Rival Kills: </strong><i>Index 1</i></p>
     * <p><strong>Total Civilian Kills: </strong><i>Index 2</i></p>
     * <p><strong>Total Neutral Kills: </strong><i>Index 3</i></p>
     *
     * @return A array of the kills of this clan from index 0 - 3
     */
    int[] getTotalKills();

    /**
     * Adds a rank to this clan
     *
     * @param rank The rank to add
     */
    void addRank(Rank rank);

    /**
     * Removes a rank and removes them also from the players
     *
     * @param rank The rank to look for
     * @return Weather is was successfully
     */
    long deleteRank(Rank rank);

    /**
     * Searches for a rank and removes it.
     *
     * @param tag The search query
     * @return Weather it was successfully
     */
    long deleteRank(String tag);

    /**
     * Gets a set of all ranks of this clan
     *
     * @return All ranks of this clan
     */
    Set<Rank> getRanks();

    /**
     * Returns a rank of this clan
     *
     * @param id The id to look for
     * @return The rank
     */
    Rank getRank(long id);

    /**
     * Searches for a rank saved in this clan
     *
     * @param tag The search query
     * @return The rank
     */
    Rank getRank(String tag);

    Rank getRankByName(String name);

    /**
     * Turns the most important information about this clan into a string
     *
     * @return A string with information about this clan
     */
    @Override
    String toString();

    /**
     * Compares this clan to another clan based on the inactive days.
     *
     * @param anotherClan Another clan
     */
    @Override
    int compareTo(Clan anotherClan);

    /**
     * Returns all members, including the leaders
     *
     * @return All members of this clan
     */
    Set<ClanPlayer> getAllAllyMembers();

    /**
     * Check whether the clan has crossed the rival limit
     * <p/>
     * <strong>limit = AllRivalAbleClans * rivalPercentLimit</strong>
     *
     * @return Weather the clan has reached the maximum rivalries
     */
    boolean reachedRivalLimit();

    /**
     * Announces a message to the server
     *
     * @param message The message
     */
    void serverAnnounce(String message);

    /**
     * Displays a profile of the clan the CommandSender
     *
     * @param sender The retriever
     */
    void showClanProfile(CommandSender sender);
}
