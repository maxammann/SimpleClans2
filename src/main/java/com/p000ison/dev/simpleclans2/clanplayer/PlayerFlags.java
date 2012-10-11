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


package com.p000ison.dev.simpleclans2.clanplayer;

import com.p000ison.dev.simpleclans2.Flags;

import java.util.Set;

/**
 * Represents a Flag of a player
 */
public class PlayerFlags extends Flags {

    /**
     * Adds a past clan to this clan.
     *
     * @param clan The clan to add.
     */
    public void addPastClan(String clan)
    {
        super.getSet("pastClans").add(clan);
    }

    /**
     * Removes a past clan to this clan.
     *
     * @param clan The id of the clan to remove.
     */
    public void removePastClan(String clan)
    {
        Set warring = super.getSet("pastClans");

        if (warring.contains(clan)) {
            warring.remove(clan);
        }
    }

    public Set<String> getPastClans()
    {
        return super.getSet("pastClans");
    }

    public void setFriendlyFire(boolean bool)
    {
        super.setBoolean("friendlyFire", bool);
    }

    public boolean isFriendlyFireEnabled()
    {
        return super.getBoolean("friendlyFire");
    }
}
