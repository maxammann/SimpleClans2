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
 *     Last modified: 27.02.13 17:00
 */

package com.p000ison.dev.simpleclans2.claiming;

import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.clanplayer.PlayerFlags;
import org.bukkit.Location;

/**
 * Represents a ClaimingManager
 */
public class ClaimingManager {

    private static final String POWER_KEY = "power";
    private final SimpleClansClaiming plugin;

    public ClaimingManager(SimpleClansClaiming plugin) {
        this.plugin = plugin;
    }


    public void setPower(ClanPlayer cp, double value) {
        PlayerFlags flags = cp.getFlags();

        if (flags == null) {
            return;
        }

        flags.set(POWER_KEY, value);
    }

    public double getPower(ClanPlayer cp) {
        PlayerFlags flags = cp.getFlags();

        if (flags == null) {
            return 0.0;
        }

        return flags.getDouble(POWER_KEY);
    }

    public double getPower(Clan clan) {
        double power = 0;

        for (ClanPlayer cp : clan.getAllMembers()) {
            power += getPower(cp);
        }
        return power;
    }

    public void setHomeChunk(Clan clan, ChunkLocation location) {

    }

//    public ChunkLocation getHomeChunk(Clan clan) {
//
//    }

    public Clan getClanAt(Location location) {
        Claim claim = getClaimAt(location);
        if (claim == null) {
            return null;
        }

        Clan clan = plugin.getClanManager().getClan(claim.getClanID());

        if (clan == null) {
            //delete claim
        }

        return clan;
    }

    public Claim getClaimAt(Location location) {
        return plugin.getDatabaseManager().getStoredClaim(ChunkLocation.toChunkLocation(location));
    }

    public boolean isClanAt(Location location) {
        return getClanAt(location) != null;
    }
}
