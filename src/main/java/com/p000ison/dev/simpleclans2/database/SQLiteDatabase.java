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
 *     Last modified: 09.11.12 17:36
 */

package com.p000ison.dev.simpleclans2.database;

import com.p000ison.dev.simpleclans2.database.configuration.SQLiteConfiguration;
import com.p000ison.dev.simpleclans2.util.Logging;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Represents a SQLiteDatabase
 */
public class SQLiteDatabase extends AbstractDatabase {

    public SQLiteDatabase(SQLiteConfiguration databaseConfiguration) throws SQLException
    {
        super(databaseConfiguration);
    }

    @Override
    protected Connection initialize() throws SQLException
    {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + ((SQLiteConfiguration) databaseConfiguration).getDatabaseFile());
        } catch (ClassNotFoundException e) {
            Logging.debug("Driver class not found! " + e.getMessage());
            return null;
        }
    }
}