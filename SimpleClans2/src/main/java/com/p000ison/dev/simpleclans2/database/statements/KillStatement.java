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

package com.p000ison.dev.simpleclans2.database.statements;


import com.p000ison.dev.simpleclans2.database.DatabaseManager;
import com.p000ison.dev.simpleclans2.database.Executable;
import com.p000ison.dev.simpleclans2.database.KillType;
import com.p000ison.dev.sqlapi.TableObject;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumn;
import com.p000ison.dev.sqlapi.annotation.DatabaseTable;

import java.sql.Timestamp;

/**
 * Represents a KillStatement
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
@DatabaseTable(name = "sc2_kills")
public class KillStatement implements Executable, TableObject {

    @DatabaseColumn(position = 0, databaseName = "id", id = true)
    private long id;
    @DatabaseColumn(position = 1, databaseName = "attacker")
    private long attacker;
    @DatabaseColumn(position = 2, databaseName = "attacker_clan")
    private long attackerClan;
    @DatabaseColumn(position = 3, databaseName = "victim")
    private long victim;
    @DatabaseColumn(position = 4, databaseName = "victim_clan")
    private long victimClan;
    @DatabaseColumn(position = 5, databaseName = "war")
    private boolean war;
    @DatabaseColumn(position = 6, databaseName = "date")
    private Timestamp date;
    @DatabaseColumn(position = 7, databaseName = "type")
    private byte killType;

    public KillStatement(long attacker, long attackerTag, long victim, long victimTag, boolean war, KillType killType)
    {
        this.attacker = attacker;
        this.attackerClan = attackerTag;
        this.victim = victim;
        this.victimClan = victimTag;
        this.war = war;
        this.date = new Timestamp(System.currentTimeMillis());
        this.killType = killType.getType();
    }

    @Override
    public boolean execute(DatabaseManager dataManager)
    {
        dataManager.getDatabase().save(this);
        return true;
    }
}
