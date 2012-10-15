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

package com.p000ison.dev.simpleclans2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Represents a Database
 */
public interface Database {

    /**
     * Gets the connection
     *
     * @return connection
     */
    Connection getConnection();

    /**
     * Checks the connection
     *
     * @return whether connection can be established
     */
    boolean checkConnection();

    /**
     * Close connection
     */
    void close();

    /**
     * Execute a query
     *
     * @param query The query
     * @return The {@link ResultSet}
     */
    ResultSet query(String query);


    /**
     * Execute an update statement
     *
     * @param query The query
     */
    boolean update(String query);


    /**
     * Execute a statement
     *
     * @param query The query
     * @return Weather is was successfully
     */
    boolean execute(String query);


    /**
     * Returns a prepared statement
     *
     * @param statement The statement
     * @return The {@link PreparedStatement}
     */
    PreparedStatement prepareStatement(String statement);

    /**
     * Check whether a table exists
     *
     * @param table The table
     * @return Weather the table exists.
     */
    boolean existsTable(String table);


    /**
     * Check whether a colmn exists
     *
     * @param table The table
     * @param colmn The colmn
     * @return Weather the colmn exists
     */
    boolean existsColumn(String table, String colmn);
}

