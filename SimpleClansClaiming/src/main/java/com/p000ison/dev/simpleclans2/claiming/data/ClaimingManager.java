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

package com.p000ison.dev.simpleclans2.claiming.data;

import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clan.ClanFlags;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.clanplayer.PlayerFlags;
import com.p000ison.dev.simpleclans2.claiming.Claim;
import com.p000ison.dev.simpleclans2.claiming.ClaimLocation;
import com.p000ison.dev.simpleclans2.claiming.SimpleClansClaiming;
import com.p000ison.dev.sqlapi.Database;
import com.p000ison.dev.sqlapi.query.PreparedSelectQuery;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a ClaimingManager
 */
public class ClaimingManager {

    private static final String POWER_KEY = "power", HOME_CHUNK_KEY = "homechunk";
    private final SimpleClansClaiming plugin;

    private final Database database;
    private final ChunkCache<Claim> cache;

    private final PreparedSelectQuery<Claim> selectClaimByCoords, selectClaimsByClan;

    public ClaimingManager(SimpleClansClaiming plugin, Database database) {
        this.plugin = plugin;
        this.database = database;
        database.registerTable(Claim.class);
        selectClaimByCoords = database.<Claim>select().from(Claim.class).where().preparedEquals("x").and().preparedEquals("y").and().preparedEquals("z").select().prepare();
        selectClaimsByClan = database.<Claim>select().from(Claim.class).where().preparedEquals("clan").select().prepare();

        this.cache = new ChunkCache<Claim>(10, 300, 60) {
            @Override
            public Claim load(ClaimLocation key) throws Exception {
                selectClaimByCoords.set(0, key.getX());
                selectClaimByCoords.set(1, key.getY());
                selectClaimByCoords.set(2, key.getZ());
                return selectClaimByCoords.getResults().get(0);
            }
        };
    }

    public void clean() {
        cache.clean();
    }

    public Claim getStoredClaim(ClaimLocation location) {
        return cache.getData(location);
    }

    public void loadClaims(Clan clan) {
        selectClaimsByClan.set(0, clan.getID());
        for (Claim claim : selectClaimsByClan.getResults()) {
            cache.load(new ClaimLocation(claim.getX(), claim.getZ(), claim.getY()), claim);
        }
    }

    public Set<Claim> getClaims(Clan clan) {
        Set<Claim> claims = new HashSet<Claim>();


        for (Claim claim : cache.getData().values()) {
            if (claim.getClanID() == clan.getID()) {
                claims.add(claim);
            }
        }

        return claims;
    }

    public void claimArea(Clan clan, ClaimLocation location) {
        Claim claim = new Claim(clan.getID(), location);
        database.insert(claim);
        cache.load(location, claim);
    }

    public Set<Claim> getSurroundingClaims(ClaimLocation chunkLocation) {
        Set<Claim> claims = new HashSet<Claim>();

        for (Claim claim : cache.getData().values()) {
            if (claim.getX() == chunkLocation.getX()
                    || claim.getX() + 1 == chunkLocation.getX()
                    || claim.getX() - 1 == chunkLocation.getX()

                    || claim.getY() == chunkLocation.getY()
                    || claim.getY() + 1 == chunkLocation.getY()
                    || claim.getY() - 1 == chunkLocation.getY()

                    || claim.getZ() == chunkLocation.getZ()
                    || claim.getZ() + 1 == chunkLocation.getZ()
                    || claim.getZ() - 1 == chunkLocation.getZ()) {
                claims.add(claim);
            }
        }

        return claims;
    }

    public boolean isSurrounding(ClaimLocation chunkLocation, Clan clan) {
        for (Claim claim : getSurroundingClaims(chunkLocation)) {
            if (claim.getClanID() == clan.getID()) {
                return true;
            }
        }

        return false;
    }

    public int getClaimAmount(Clan clan) {
        int i = 0;

        for (Claim claim : cache.getData().values()) {
            if (claim.getClanID() == clan.getID()) {
                i++;
            }
        }

        return i;
    }

    public void setPower(ClanPlayer cp, double value) {
        PlayerFlags flags = cp.getFlags();

        flags.set(POWER_KEY, value);
    }

    public double getPower(ClanPlayer cp) {
        PlayerFlags flags = cp.getFlags();

        return flags.getDouble(POWER_KEY);
    }

    public double getPower(Clan clan) {
        double power = 0;

        for (ClanPlayer cp : clan.getAllMembers()) {
            power += getPower(cp);
        }
        return power;
    }

    public void setHomeChunk(Clan clan, ClaimLocation location) {
        ClanFlags flags = clan.getFlags();

        flags.setString(HOME_CHUNK_KEY, location.toString());
    }

    public ClaimLocation getHomeChunk(Clan clan) {
        ClanFlags flags = clan.getFlags();
        return ClaimLocation.toLocation(flags.getString(HOME_CHUNK_KEY));
    }

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
        return getStoredClaim(toClaimLocation(location));
    }

    public ClaimLocation toClaimLocation(Location location) {
        return ClaimLocation.toClaimLocation(location, 16, 256);
    }

    public boolean isClanAt(Location location) {
        return getClanAt(location) != null;
    }
}
