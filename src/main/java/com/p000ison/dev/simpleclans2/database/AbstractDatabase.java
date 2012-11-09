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
 *     Last modified: 09.11.12 17:39
 */

package com.p000ison.dev.simpleclans2.database;

import com.p000ison.dev.simpleclans2.database.configuration.DatabaseConfiguration;
import com.p000ison.dev.simpleclans2.database.configuration.MySQLConfiguration;
import com.p000ison.dev.simpleclans2.database.configuration.SQLiteConfiguration;
import com.p000ison.dev.simpleclans2.util.Logging;

import java.sql.*;

/**
 * Represents a AbstractDatabase
 */
public abstract class AbstractDatabase implements Database {
    private Connection connection;
    protected DatabaseConfiguration databaseConfiguration;

    public AbstractDatabase(DatabaseConfiguration databaseConfiguration) throws SQLException
    {
        this.databaseConfiguration = databaseConfiguration;
        connection = initialize();
    }

    protected abstract Connection initialize() throws SQLException;

    @Override
    public Connection getConnection()
    {
        try {
            if (connection == null || connection.isClosed()) {
                return null;
            }
        } catch (SQLException e) {
            Logging.debug(e, true);
        }

        return connection;
    }

    @Override
    public boolean checkConnection()
    {
        return getConnection() != null;
    }

    @Override
    public PreparedStatement prepareStatement(String statement)
    {
        try {
            return connection.prepareStatement(statement);
        } catch (SQLException ex) {
            Logging.debug("Error at creating the statement: " + statement + "(" + ex.getMessage() + ")");
        }
        return null;
    }

    @Override
    public void close()
    {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            Logging.debug("Failed to close database2 connection! " + e.getMessage());
        }
    }

    @Override
    public ResultSet query(String query)
    {
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            Logging.debug(e, "Error at SQL Query", true);
            Logging.debug("Query: " + query);
        }

        return null;
    }

    @Override
    public boolean update(String query)
    {
        try {
            Statement statement = getConnection().createStatement();
            boolean result = statement.executeUpdate(query) == 0;
            statement.close();
            return result;
        } catch (SQLException e) {
            Logging.debug(e, "Error at SQL UPDATE Query", true);
            Logging.debug("Query: " + query);
            return false;
        }
    }


    /**
     * Execute a statement
     *
     * @param query The query string
     * @return weather is was successfully
     */
    @Override
    public boolean execute(String query)
    {
        try {
            Statement statement = getConnection().createStatement();
            boolean result = statement.execute(query);
            statement.close();
            return result;
        } catch (SQLException e) {
            Logging.debug(e, "Error at SQL EXECUTE Query", true);
            Logging.debug("Query: " + query);
            return false;
        }
    }

    /**
     * Check whether a table exists
     *
     * @param table The table name
     * @return Weather is exists
     */
    @Override
    public boolean existsTable(String table)
    {
        try {
            ResultSet tables = getConnection().getMetaData().getTables(null, null, table, null);
            boolean empty = tables.next();
            tables.close();
            return empty;
        } catch (SQLException e) {
            Logging.debug("Failed to check if table '" + table + "' exists: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check whether a colum exists
     *
     * @param table  The table
     * @param column The colmn
     * @return Weather is exists
     */
    @Override
    public boolean existsColumn(String table, String column)
    {
        try {
            ResultSet columns = getConnection().getMetaData().getColumns(null, null, table, column);
            boolean empty = columns.next();
            columns.close();
            return empty;
        } catch (SQLException e) {
            Logging.debug("Failed to check if colum '" + column + "' exists: " + e.getMessage(), e);
            return false;
        }
    }

    public DatabaseConfiguration getDatabaseConfiguration()
    {
        return databaseConfiguration;
    }

    public static Database createDatabase(DatabaseConfiguration config) throws SQLException
    {
        if (config instanceof MySQLConfiguration) {
            return new MySQLDatabase((MySQLConfiguration) config);
        } else if (config instanceof SQLiteConfiguration) {
            return new SQLiteDatabase((SQLiteConfiguration) config);
        }

        return null;
    }
}