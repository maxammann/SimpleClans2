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


package com.p000ison.dev.simpleclans2.data;

import java.util.Date;

/**
 * Represents a KillEntry
 */

public class KillEntry {


    private int id;


    private String attacker;


    private String attacker_tag;


    private String victim;


    private String victim_tag;


    private boolean war;


    private long date;

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
