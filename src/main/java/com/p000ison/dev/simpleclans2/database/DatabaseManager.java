/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:29
 */


package com.p000ison.dev.simpleclans2.database;

import com.alta189.simplesave.Database;
import com.alta189.simplesave.DatabaseFactory;
import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;
import com.alta189.simplesave.mysql.MySQLConfiguration;
import com.p000ison.dev.simpleclans2.data.KillEntry;
import com.p000ison.dev.simpleclans2.database.tables.ClanPlayerTable;
import com.p000ison.dev.simpleclans2.database.tables.ClanTable;

/**
 * Represents a DatabaseManager. This handles all the database stuff.
 */
public class DatabaseManager {

    private Database db = null;

    public DatabaseManager() throws TableRegistrationException, ConnectionException
    {

        MySQLConfiguration config = new MySQLConfiguration();
        config.setHost("localhost");
        config.setUser("root");
        config.setPassword("");
        config.setDatabase("sc");
        config.setPort(3306);
        db = DatabaseFactory.createNewDatabase(config);

        db.registerTable(ClanTable.class);
        db.registerTable(ClanPlayerTable.class);
        db.registerTable(KillEntry.class);

        db.connect();
    }

    /**
     * Checks if the database is connected.
     *
     * @return Weather the database is connected
     */
    public boolean isConnected()
    {
        if (db == null) {
            return false;
        }

        return db.isConnected();
    }

    /**
     * Retrieve the database
     *
     * @return The Database link.
     */
    public Database getDatabase()
    {
        return db;
    }
}
