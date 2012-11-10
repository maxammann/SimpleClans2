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
 *     Last modified: 04.11.12 01:01
 */

package com.p000ison.dev.simpleclans2.database.data;

/**
 * Represents a Conflicts
 */
public class Conflicts {
    private long attacker, victim;
    private int conflicts;

    public Conflicts(long attacker, long victim, int conflicts)
    {
        this.attacker = attacker;
        this.victim = victim;
        this.conflicts = conflicts;
    }

    public long getAttacker()
    {
        return attacker;
    }

    public long getVictim()
    {
        return victim;
    }

    public int getConflicts()
    {
        return conflicts;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conflicts conflicts = (Conflicts) o;

        return attacker == conflicts.attacker && victim == conflicts.victim;
    }

    @Override
    public int hashCode()
    {
        int result = (int) (attacker ^ (attacker >>> 32));
        result = 31 * result + (int) (victim ^ (victim >>> 32));
        return result;
    }
}
