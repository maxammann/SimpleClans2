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
 *     Last modified: 27.02.13 14:35
 */

package com.p000ison.dev.simpleclans2.claiming;

import com.p000ison.dev.sqlapi.Database;
import com.p000ison.dev.sqlapi.query.PreparedSelectQuery;

/**
 * Represents a DatabaseManager
 */
public class DatabaseManager {
    private final Database database;
    private final ChunkCache<Claim> cache;

    private final PreparedSelectQuery<Claim> selectClaim;

    public DatabaseManager(final Database database) {
        this.database = database;
        init();
        selectClaim = database.<Claim>select().from(Claim.class).where().preparedEquals("x").and().preparedEquals("z").select().prepare();

        this.cache = new ChunkCache<Claim>(10, 300, 60) {
            @Override
            public Claim load(ChunkLocation key) throws Exception {
                selectClaim.set(0, key.getX());
                selectClaim.set(1, key.getZ());
                return selectClaim.getResults().get(0);
            }
        };
    }

    public void clean() {
        cache.clean();
    }

    public Claim getStoredClaim(ChunkLocation location) {
        return cache.getData(location);
    }

    public void init() {
        database.registerTable(Claim.class);
    }

    public Database getDatabase() {
        return database;
    }
}
