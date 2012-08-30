/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2.data;

import com.alta189.simplesave.Field;
import com.alta189.simplesave.Id;
import com.alta189.simplesave.Table;

import java.util.Date;

/**
 * Represents a KillEntry
 */
@Table("sc2_kills")
public class KillEntry {

    @Id
    private int id;

    @Field
    private String attacker;

    @Field
    private String attacker_tag;

    @Field
    private String victim;

    @Field
    private String victim_tag;

    @Field
    private boolean war;

    @Field
    private long date;

    @Field
    private byte kill_type;

    public String getAttacker()
    {
        return attacker;
    }

    public void setAttacker(String attacker)
    {
        this.attacker = attacker;
    }

    public String getAttackerTag()
    {
        return attacker_tag;
    }

    @Override
    public String toString()
    {
        return new StringBuilder("KillEntry {").append(attacker).append(',').append(attacker_tag).append(',').append(victim).append(',').append(victim_tag).append(',').append(kill_type).append(',').append(war).append(',').append(new Date(date).toString()).append('}').toString();
    }

    public void setAttackerTag(String attacker_tag)
    {
        this.attacker_tag = attacker_tag;
    }

    public String getVictim()
    {
        return victim;
    }

    public void setVictim(String victim)
    {
        this.victim = victim;
    }

    public String getVictimTag()
    {
        return victim_tag;
    }

    public void setVictimTag(String victim_tag)
    {
        this.victim_tag = victim_tag;
    }

    public boolean isWar()
    {
        return war;
    }

    public void setWar(boolean war)
    {
        this.war = war;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate(long date)
    {
        this.date = date;
    }

    public byte getKillType()
    {
        return kill_type;
    }

    public void setKillType(byte kill_type)
    {
        this.kill_type = kill_type;
    }
}
