/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2.clanplayer;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a ClanPlayer
 */
public class ClanPlayer {

    private SimpleClans plugin;

    private long id;
    private String name;
    private Clan clan;
    private boolean banned, leader, trusted, friendlyFire;
    private long lastSeen, joinDate;
    private int neutralKills, rivalKills, civilianKills, deaths;
    private PlayerFlags flags;
    private Set<Long> pastClans = new HashSet<Long>();

    public ClanPlayer(SimpleClans plugin, String name)
    {
        flags = new PlayerFlags();
        this.plugin = plugin;
        this.name = name;
    }

    /**
     * Returns the id of the clan the player is in
     *
     * @return The id
     */
    public long getClanId()
    {
        return clan.getId();
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

    public String getName()
    {
        return name;
    }

    public boolean isBanned()
    {
        return banned;
    }

    public void setBanned(boolean banned)
    {
        this.banned = banned;
    }

    public boolean isLeader()
    {
        return leader;
    }

    public void setLeader(boolean leader)
    {
        this.leader = leader;
    }

    public boolean isTrusted()
    {
        return trusted;
    }

    public void setTrusted(boolean trusted)
    {
        this.trusted = trusted;
    }

    public boolean isFriendlyFireOn()
    {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire)
    {
        this.friendlyFire = friendlyFire;
    }

    public long getLastSeenDate()
    {
        return lastSeen;
    }

    public void setLastSeenDate(long lastSeen)
    {
        this.lastSeen = lastSeen;
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

    public void addNeutralKills(int neutralKills)
    {
        this.neutralKills++;
    }

    public int getRivalKills()
    {
        return rivalKills;
    }

    public void addRivalKills()
    {
        this.rivalKills++;
    }

    public int getCivilianKills()
    {
        return civilianKills;
    }

    public void addCivilianKills()
    {
        this.civilianKills++;
    }

    public int getDeaths()
    {
        return deaths;
    }

    public void addDeath()
    {
        this.deaths++;
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

    public void addPastClan(Clan clan)
    {
        pastClans.add(clan.getId());
    }

    public Set<Long> getPastClans()
    {
        return pastClans;
    }
}
