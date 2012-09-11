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
 *     Created: 03.09.12 16:37
 */

package com.p000ison.dev.simpleclans2.database;

import com.p000ison.dev.simpleclans2.util.Logging;

import java.sql.*;

/**
 * Represents a MySQLDatabase
 */
public class MySQLDatabase implements Database {
    private Connection connection;
    private String host;
    private String username;
    private String password;
    private String database;


    public MySQLDatabase(String host, String database, String username, String password)
    {
        this.database = database;
        this.host = host;
        this.username = username;
        this.password = password;

        initialize();
    }

    private void initialize()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, username, password);
        } catch (ClassNotFoundException e) {
            Logging.debug("ClassNotFoundException! " + e.getMessage());
        } catch (SQLException e) {
            Logging.debug("SQLException! " + e.getMessage());
        }
    }

    /**
     * @return The connection
     */
    @Override
    public Connection getConnection()
    {
        try {
            if (connection == null || connection.isClosed()) {
                initialize();
            }
        } catch (SQLException e) {
            initialize();
        }

        return connection;
    }

    /**
     * @return whether connection can be established
     */
    @Override
    public Boolean checkConnection()
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

    /**
     * Close connection
     */
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

    /**
     * Execute a select statement
     *
     * @param query
     * @return
     */
    @Override
    public ResultSet select(String query)
    {
        try {
            return getConnection().createStatement().executeQuery(query);
        } catch (SQLException e) {
            Logging.debug(e, "Error at SQL SELECT Query");
            Logging.debug("Query: " + query);
        }

        return null;
    }


    /**
     * Execute an update statement
     *
     * @param query
     */
    @Override
    public void update(String query)
    {
        try {
            getConnection().createStatement().executeUpdate(query);
        } catch (SQLException e) {
            Logging.debug(e, "Error at SQL UPDATE Query");
            Logging.debug("Query: " + query);

        }
    }


    /**
     * Execute a statement
     *
     * @param query The query string
     * @return weather is was successfully
     */
    @Override
    public Boolean execute(String query)
    {
        try {
            getConnection().createStatement().execute(query);
            return true;
        } catch (SQLException e) {
            Logging.debug(e, "Error at SQL EXECUTE Query");
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
    public Boolean existsTable(String table)
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
     * @param table The table
     * @param column The colmn
     * @return Weather is exists
     */
    @Override
    public Boolean existsColumn(String table, String column)
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
}
