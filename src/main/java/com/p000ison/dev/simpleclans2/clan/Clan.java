/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.util.DateHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Clan
 */
public class Clan {

    private SimpleClans plugin;

    private long id = -1;
    private String tag, name;
    private ClanFlags flags;
    private long foundedDate;
    private long lastActionDate;
    private boolean verified, friendlyFire;

    /**
     * Creates a new clan
     *
     * @param plugin
     */
    public Clan(SimpleClans plugin)
    {
        flags = new ClanFlags();
        this.plugin = plugin;
    }

    public Clan(SimpleClans plugin, String tag, String name)
    {
        this(plugin);
        this.tag = tag;
        this.name = name;
    }

    public Clan(SimpleClans plugin, long id, String tag, String name)
    {
        this(plugin, tag, name);
        this.id = id;
    }


    /**
     * Gets the tag of a clan. This is unique and can contain colors.
     *
     * @return The tag.
     */
    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    /**
     * Gets the unique id of this clan.
     *
     * @return The id.
     */
    public long getId()
    {
        return id;
    }

    /**
     * Sets the id of this clan.
     *
     * @param id The id.
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * Returns the Flags of this clan. {@link ClanFlags} contains the flags of this clan.
     *
     * @return The flags of this clan.
     * @see ClanFlags
     */
    public ClanFlags getFlags()
    {
        return flags;
    }

    /**
     * Sets the flags of this clan to a new object.
     *
     * @param flag The flag to set
     */
    public void setFlags(ClanFlags flag)
    {
        this.flags = flags;
    }

    /**
     * Gets the name of this clan.
     *
     * @return The name of this clan.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this clan
     *
     * @param name The name of this clan
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the time in milliseconds in the unix time
     *
     * @return The time
     */
    public long getLastActionDate()
    {
        return lastActionDate;
    }

    /**
     * Updates the last action to NOW!
     */
    public void updateLastAction()
    {
        this.setLastActionDate(System.currentTimeMillis());
    }

    /**
     * Returns the date then this clan was founded in milliseconds in the unix time.
     *
     * @return The found date,
     */
    public long getFoundedDate()
    {
        return foundedDate;
    }

    /**
     * Sets when the clan was founded.
     *
     * @param foundedDate The time when it was founded.
     */
    public void setFoundedDate(long foundedDate)
    {
        this.foundedDate = foundedDate;
    }

    /**
     * Checks if this clan is verified.
     *
     * @return Weather this clan is verified.
     */
    public boolean isVerified()
    {
        return verified;
    }

    /**
     * Sets this clan verified.
     *
     * @param verified
     */
    public void setVerified(boolean verified)
    {
        this.verified = verified;
    }

    /**
     * Checks if friendly fire is on.
     *
     * @return Weather friendly fire is on of of.
     */
    public boolean isFriendlyFireOn()
    {
        return friendlyFire;
    }

    /**
     * Sets friendly fire for this clan.
     *
     * @param friendlyFire Weather friendly fire is on or off.
     */
    public void setFriendlyFire(boolean friendlyFire)
    {
        this.friendlyFire = friendlyFire;
    }

    /**
     * Returns a set of allies
     *
     * @return The allies.
     */
    public Set<Clan> getAllies()
    {
        return plugin.getClanManager().convertIdSetToClanSet(flags.getAllies());
    }

    /**
     * Returns a set of rivals
     *
     * @return The rival clans.
     */
    public Set<Clan> getRivals()
    {
        return plugin.getClanManager().convertIdSetToClanSet(flags.getRivals());
    }

    /**
     * Returns a set of warring clans
     *
     * @return The warring clans.
     */
    public Set<Clan> getWarringClans()
    {
        return plugin.getClanManager().convertIdSetToClanSet(flags.getWarringClans());
    }

    /**
     * Checks weather the clan is a ally.
     *
     * @param id The id of the other clan.
     * @return Weather they are allies or not.
     */
    public boolean isAlly(long id)
    {
        Set<Long> allies = flags.getAllies();

        if (allies == null) {
            return false;
        }

        return allies.contains(id);
    }

    /**
     * Checks weather the clan  is a ally.
     *
     * @param clan The other clan
     * @return Weather they are allies or not.
     */
    public boolean isAlly(Clan clan)
    {
        if (clan == null) {
            return false;
        }

        return isAlly(clan.getId());
    }

    /**
     * Checks weather the clan with the id is a rival.
     *
     * @param id The id of the other clan.
     * @return Weather they are rivals or not.
     */
    public boolean isRival(long id)
    {
        Set<Long> rivals = flags.getAllies();

        if (rivals == null) {
            return false;
        }

        return rivals.contains(id);
    }

    /**
     * Checks weather the clan is warring with this one.
     *
     * @param id The id of the other clan.
     * @return Weather they are warring or not.
     */
    public boolean isWarring(long id)
    {
        Set<Long> warring = flags.getWarringClans();

        if (warring == null) {
            return false;
        }

        return warring.contains(id);
    }

    /**
     * Checks weather the clan is warring with this one.
     *
     * @param clan The other clan.
     * @return Weather they are warring or not.
     */
    public boolean isWarring(Clan clan)
    {

        if (clan == null) {
            return false;
        }

        return isWarring(clan.getId());
    }

    /**
     * Gets the days this clan is inactive
     *
     * @return The days this clan is inactive
     */
    public double getInactiveDays()
    {
        return DateHelper.differenceInDays(lastActionDate, System.currentTimeMillis());
    }


    /**
     * Sets the date when the last action happened.
     *
     * @param lastActionDate The date when the last action happened.
     */
    public void setLastActionDate(long lastActionDate)
    {
        this.lastActionDate = lastActionDate;
    }

    public void setLeader(ClanPlayer clanPlayer)
    {
        clanPlayer.setLeader(true);
    }

    public boolean isMember()
    {
        for (ClanPlayer cp : plugin.getClanPlayerManager().getClanPlayers()) {
            if (cp.getClanId() == id && !cp.isLeader()) {
                return true;
            }
        }

        return false;
    }

    public boolean isLeader(ClanPlayer cp)
    {
        return cp.getClanId() == id && cp.isLeader();
    }

    public boolean isAnyMember()
    {
        for (ClanPlayer cp : plugin.getClanPlayerManager().getClanPlayers()) {
            if (cp.getClanId() == id) {
                return true;
            }
        }

        return false;
    }

    public Set<ClanPlayer> getMembers()
    {
        Set<ClanPlayer> allMembers = new HashSet<ClanPlayer>();


        for (ClanPlayer cp : plugin.getClanPlayerManager().getClanPlayers()) {
            if (cp.getClanId() == id && !cp.isLeader()) {
                allMembers.add(cp);
            }
        }

        return allMembers;
    }

    public Set<ClanPlayer> getAllMembers()
    {
        Set<ClanPlayer> allMembers = new HashSet<ClanPlayer>();


        for (ClanPlayer cp : plugin.getClanPlayerManager().getClanPlayers()) {
            if (cp.getClanId() == id) {
                allMembers.add(cp);
            }
        }

        return allMembers;
    }


    public Set<ClanPlayer> getLeaders()
    {
        Set<ClanPlayer> leaders = new HashSet<ClanPlayer>();


        for (ClanPlayer cp : plugin.getClanPlayerManager().getClanPlayers()) {
            if (cp.getClanId() == id && cp.isLeader()) {
                leaders.add(cp);
            }
        }

        return leaders;
    }

    public void addMember(ClanPlayer clanPlayer)
    {
        Clan previous = clanPlayer.getClan();

        if (previous != null) {
            clanPlayer.addPastClan(previous);
        }

        clanPlayer.setClan(this);
    }
}
