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

package com.p000ison.dev.simpleclans2.database.data.statements;


import com.p000ison.dev.simpleclans2.database.data.DataManager;
import com.p000ison.dev.simpleclans2.database.data.Executable;
import com.p000ison.dev.simpleclans2.database.data.KillType;
import com.p000ison.dev.simpleclans2.util.Logging;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Represents a KillStatement
 */
public class KillStatement implements Executable {

    private long attacker;
    private long attackerClan;
    private long victim;
    private long victimClan;
    private boolean war;
    private long date;
    private byte killType;

    public KillStatement(long attacker, long attackerTag, long victim, long victimTag, boolean war, KillType killType)
    {
        this.attacker = attacker;
        this.attackerClan = attackerTag;
        this.victim = victim;
        this.victimClan = victimTag;
        this.war = war;
        this.date = System.currentTimeMillis();
        this.killType = killType.getType();
    }

    @Override
    public boolean execute(DataManager dataManager)
    {
        try {
            PreparedStatement kill = dataManager.INSERT_KILL;
            kill.setLong(1, attacker);
            kill.setLong(2, attackerClan);
            kill.setLong(3, victim);
            kill.setLong(4, victimClan);
            kill.setBoolean(5, war);
            kill.setByte(6, killType);
            kill.setTimestamp(7, new Timestamp(date));

            return kill.executeUpdate() != 0;
        } catch (SQLException e) {
            Logging.debug(e, true, "Failed to insert kill for victim attacker %s and victim %s.", attacker, victim);
            return false;
        }
    }
}
