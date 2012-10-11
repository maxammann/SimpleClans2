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


package com.p000ison.dev.simpleclans2.database.data;

import java.sql.Timestamp;

/**
 * Represents a KillEntry
 */

public class KillEntry {

    private String attacker;
    private String attackerTag;
    private String victim;
    private String victimTag;
    private boolean war;
    private Timestamp date;
    private byte killType;

    public KillEntry(String attacker, String attackerTag, String victim, String victimTag, boolean war, Timestamp date, byte killType)
    {
        this.attacker = attacker;
        this.attackerTag = attackerTag;
        this.victim = victim;
        this.victimTag = victimTag;
        this.war = war;
        this.date = date;
        this.killType = killType;
    }

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
        return attackerTag;
    }

    @Override
    public String toString()
    {
        return "KillEntry {" + attacker + ',' + attackerTag + ',' + victim + ',' + victimTag + ',' + killType + ',' + war + ',' + date.toString() + '}';
    }

    public boolean isWar()
    {
        return war;
    }

    public void setWar(boolean war)
    {
        this.war = war;
    }

    public Timestamp getDate()
    {
        return date;
    }

    public byte getKillType()
    {
        return killType;
    }

}
