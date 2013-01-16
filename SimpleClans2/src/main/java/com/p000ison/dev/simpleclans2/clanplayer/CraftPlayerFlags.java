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

import com.p000ison.dev.simpleclans2.JSONFlags;
import com.p000ison.dev.simpleclans2.api.clanplayer.PlayerFlags;

import java.util.Set;

/**
 * Represents a Flag of a player
 */
@SuppressWarnings("unchecked")
public class CraftPlayerFlags extends JSONFlags implements PlayerFlags {

    private static final long serialVersionUID = 3232671374494730728L;

    @Override
    public void addPastClan(String clan) {
        super.getSet("pastClans").add(clan);
    }

    @Override
    public void removePastClan(String clan) {
        Set warring = super.getSet("pastClans");

        if (warring.contains(clan)) {
            warring.remove(clan);
        }
    }

    @Override
    public Set<String> getPastClans() {
        return super.getSet("pastClans");
    }

    @Override
    public void setFriendlyFire(boolean bool) {
        super.setBoolean("ff", bool);
    }

    @Override
    public boolean isFriendlyFireEnabled() {
        return super.getBoolean("ff");
    }

    @Override
    public boolean isCapeEnabled() {
        return super.getBoolean("cape");
    }

    @Override
    public void setCapeEnabled(boolean enabled) {
        super.setBoolean("cape", enabled);
    }

    @Override
    public boolean isBBEnabled() {
        return super.getBoolean("bb");
    }

    @Override
    public void setBBEnabled(boolean enabled) {
        super.setBoolean("bb", enabled);
    }
}
